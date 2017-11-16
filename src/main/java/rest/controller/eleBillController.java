package rest.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import rest.mybatis.dao.eBillDao.AdvancesReceivedMapper;
import rest.mybatis.dao.eBillDao.EbIssueTMapper;
import rest.mybatis.model.eBillModel.AdvancesReceived;
import rest.mybatis.model.eBillModel.EbIssueT;
import rest.utils.CommonUtils;
import rest.utils.MyThreadLocalTool;

@Controller
public class eleBillController {

	@Autowired
	private EbIssueTMapper ebIssueTMapper;
	@Autowired
	private AdvancesReceivedMapper advancesReceivedMapper;
	//本地线程，保证文件类型，不会出现多线程并发问题，导致数据错乱
	private MyThreadLocalTool<String> fileTypeThread= new MyThreadLocalTool<String>();
	
	@RequestMapping(value = "/uploadBillFile", method = RequestMethod.POST)
	@ResponseBody
	public void uploadBillFile(HttpServletRequest req,MultipartHttpServletRequest multiReq) throws IOException, Exception{
		//获得上传的文件
		MultipartFile uploadfile = multiReq.getFile("upfile");
		//期数开始时间 --保留数据
		String issueStartDate = req.getParameter("issueStartDate");
		//期数结束时间--保留数据
		String issueEndDate = req.getParameter("issueEndDate");
		//期数ID
		String issueId  = req.getParameter("issueObjId");
		//上传文件类别
		String fileType  = req.getParameter("upfileType");
		fileTypeThread.getTl().set(fileType);
		
		String fileName = uploadfile.getOriginalFilename();
		String fileTail = getTail(fileName);
		if(null != fileTail && fileTail.equals("xls")){
			//保存xls文件
			saveXlsFile(uploadfile,issueId,fileTypeThread.getTl().get());
			
		}else if(null != fileTail && fileTail.equals("xlsx")){
			//保存xlsx文件
			saveXlsxFile(uploadfile,issueId,fileTypeThread.getTl().get());
		}else{
			
		}
	
	}
	
	/**
	 * TODO 处理预收表 xls 格式
	 * @param uploadfile
	 * @param issueId
	 * @param fileType
	 * @throws IOException
	 * @throws Exception
	 */
	public void saveXlsFile(MultipartFile uploadfile,String issueId,String fileType) throws IOException, Exception{
		
		InputStream is = uploadfile.getInputStream();
		
		Workbook workbook = Workbook.getWorkbook(is);
		
		if(null != workbook){
			Sheet sheet = workbook.getSheet(0);
			if(null != sheet){
				List list = new ArrayList();
				for(int i=1; i<sheet.getRows(); i++){
					Cell[] row = sheet.getRow(i);
					if(null != fileType && fileType.equals("file_yuhsou")){
						AdvancesReceived advancesReceived = saveAdvancesReceived(row,null,issueId);
						list.add(advancesReceived);
					}else if(null != fileType && fileType.equals("file_rebate")){
						
					}else if(null != fileType && fileType.equals("file_unrebate")){
						
					}else if(null != fileType && fileType.equals("file_netsum")){
						
					}else if(null != fileType && fileType.equals("file_statistic")){
						
					}else if(null != fileType && fileType.equals("file_useamount")){
						
					}
				}
				advancesReceivedMapper.insertBatch(list);
			}
		}
	}
	
	/**
	 * TODO 处理预收表xlsx格式
	 * @param uploadfile
	 * @throws IOException 
	 */
	public void saveXlsxFile(MultipartFile uploadfile,String issueId,String fileType) throws IOException,Exception{
		
		InputStream is = uploadfile.getInputStream();
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		if(null != xssfWorkbook){
			XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
			if(null != sheet){
				List list = new ArrayList();
				for(int i=1; i<sheet.getLastRowNum(); i++ ){
					XSSFRow r = sheet.getRow(i);
					if(null != r){
						if(null != fileType && fileType.equals("file_yuhsou")){
							AdvancesReceived advancesReceived = saveAdvancesReceived(null,r,issueId);
							list.add(advancesReceived);
						}else if(null != fileType && fileType.equals("file_rebate")){
							
						}else if(null != fileType && fileType.equals("file_unrebate")){
							
						}else if(null != fileType && fileType.equals("file_netsum")){
							
						}else if(null != fileType && fileType.equals("file_statistic")){
							
						}else if(null != fileType && fileType.equals("file_useamount")){
							
						}
						
						
					}
				}
				advancesReceivedMapper.insertBatch(list);
			}
		}
	}
	/**
	 * TODO 保存预收表
	 * @param r
	 * @param issueId
	 */
	public AdvancesReceived saveAdvancesReceived(Cell[] c,XSSFRow r,String issueId){
		
		AdvancesReceived ar = new AdvancesReceived();
		String uuid = CommonUtils.obtainUUID();
		if(null == c && null != r){
			ar.setId(uuid);
			ar.setReconciliationObj(getStr(r.getCell(0)));  //对账对象
			ar.setCollectionType(getStr(r.getCell(1)));//收款类型
			ar.setCollectionNumber(getStr(r.getCell(2)));//收款单号
			ar.setCollectionDays(CommonUtils.strToDate(getStr(r.getCell(3))));//收款日期
			ar.setCurrency(getStr(r.getCell(4)));//币别
			ar.setExchangeRate(Double.parseDouble(getStr(r.getCell(5))));//汇率
			ar.setConsumer(getStr(r.getCell(6)));//客户
			ar.setSalesman(getStr(r.getCell(7)));//业务员
			ar.setDepartment(getStr(r.getCell(8)));//部门
			ar.setAdvancesReceivedAbstract(getStr(r.getCell(9)));//摘要
			ar.setCollectionSum(Double.parseDouble(getStr(r.getCell(10))));//收款金额
			ar.setUncheckedSum(Double.parseDouble(getStr(r.getCell(11))));//未核销金额
			ar.setAdvancesReceivedOperator(null);//操作人
			ar.setAdvancesReceivedOperateDate(new Date());//操作时间
			ar.setFrIssueId(issueId);//期数ID
			ar.setAdvancesReceivedIsLive(1);//是否删除
		}else if(null != c && null == r){
			ar.setId(uuid);
			ar.setReconciliationObj(c[0].getContents());  //对账对象
			ar.setCollectionType(c[1].getContents());//收款类型
			ar.setCollectionNumber(c[2].getContents());//收款单号
			ar.setCollectionDays(CommonUtils.strToDate(c[3].getContents()));//收款日期
			ar.setCurrency(c[4].getContents());//币别
			ar.setExchangeRate(Double.parseDouble(c[5].getContents()));//汇率
			ar.setConsumer(c[6].getContents());//客户
			ar.setSalesman(c[7].getContents());//业务员
			ar.setDepartment(c[8].getContents());//部门
			ar.setAdvancesReceivedAbstract(c[9].getContents());//摘要
			ar.setCollectionSum(Double.parseDouble(c[10].getContents()));//收款金额
			ar.setUncheckedSum(Double.parseDouble(c[11].getContents()));//未核销金额
			ar.setAdvancesReceivedOperator(null);//操作人
			ar.setAdvancesReceivedOperateDate(new Date());//操作时间
			ar.setFrIssueId(issueId);//期数ID
			ar.setAdvancesReceivedIsLive(1);//是否删除
		}
		return ar;
		
	}
	
	/**
	 * TODO 取出单元格值转换为字符串
	 * 	CELL_TYPE_NUMERIC 数值型 0
		CELL_TYPE_STRING 字符串型 1
		CELL_TYPE_FORMULA 公式型 2
		CELL_TYPE_BLANK 空值 3
		CELL_TYPE_BOOLEAN 布尔型 4
		CELL_TYPE_ERROR 错误 5
	 * @param xssfRow
	 * @return
	 */
    @SuppressWarnings("static-access")
    private String getStr(XSSFCell xssfRow) {
    	if(null == xssfRow || "".equals(xssfRow)){
    		return null;
    	}
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_STRING) {
            return xssfRow.getStringCellValue();
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            return String.valueOf(xssfRow.getNumericCellValue());
        } else if(xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN){
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if(xssfRow.getCellType() == xssfRow.CELL_TYPE_BLANK){
        	return null;
        } else if(xssfRow.getCellType() == xssfRow.CELL_TYPE_FORMULA){
        	return xssfRow.getStringCellValue();
        }
        return null;
    }
	
	/**
	 * TODO 获得文件后缀
	 * @param fileName
	 * @return
	 */
	private String getTail(String fileName){
		
		if(null == fileName || "".equals(fileName) || !fileName.contains(".")){
			return null;
		}else{
			return fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		}
	}
	
	@RequestMapping(value = "/searchEbIssue", method = RequestMethod.GET)
	@ResponseBody
	public EbIssueT searchEbIssueUnpublished(){
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("issueState", 0); //未发布状态的期数
		
		List<EbIssueT> ei = ebIssueTMapper.queryIssueByMap(paramMap);
		return null != (EbIssueT)ei.get(0) ? (EbIssueT)ei.get(0) : new EbIssueT();
	}
}
