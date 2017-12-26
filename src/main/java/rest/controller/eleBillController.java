package rest.controller;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import rest.mybatis.dao.eBillDao.AdvancesReceivedMapper;
import rest.mybatis.dao.eBillDao.EbFoundsUseAmountMapper;
import rest.mybatis.dao.eBillDao.EbIssueTMapper;
import rest.mybatis.dao.eBillDao.EbNetAmountMapper;
import rest.mybatis.dao.eBillDao.EbRebateTMapper;
import rest.mybatis.dao.eBillDao.EbSalesStatisticsTMapper;
import rest.mybatis.dao.eBillDao.EbUnrebateTMapper;
import rest.mybatis.model.eBillModel.AdvancesReceived;
import rest.mybatis.model.eBillModel.EbFoundsUseAmount;
import rest.mybatis.model.eBillModel.EbIssueT;
import rest.mybatis.model.eBillModel.EbNetAmount;
import rest.mybatis.model.eBillModel.EbRebateT;
import rest.mybatis.model.eBillModel.EbSalesStatisticsT;
import rest.mybatis.model.eBillModel.EbUnrebateT;
import rest.service.HandleTransactionalService;
import rest.utils.CommonUtils;
import rest.utils.MyException;
import rest.utils.MyThreadLocalTool;

@Controller
public class eleBillController {

	@Autowired
	private EbIssueTMapper ebIssueTMapper;                            //期数
	@Autowired
	private AdvancesReceivedMapper advancesReceivedMapper;            //预收表
	@Autowired
	private EbUnrebateTMapper ebUnrebateTMapper;                      //未核销的留返利
	@Autowired
	private EbRebateTMapper ebRebateTMapper;                          //留返利
	@Autowired
	private EbNetAmountMapper ebNetAmountMapper;                      //应收净额
	@Autowired
	private EbSalesStatisticsTMapper ebSalesStatisticsTMapper;        //销售业务统计
	@Autowired
	private EbFoundsUseAmountMapper ebFoundsUseAmountMapper;          //资金占用
	@Autowired
	private HandleTransactionalService handleTransactionalService;    //处理事务的service
	//本地线程，保证文件类型，不会出现多线程并发问题，导致数据错乱
	private MyThreadLocalTool<String> fileTypeThread= new MyThreadLocalTool<String>();
	private MyThreadLocalTool<User> userThread= new MyThreadLocalTool<User>();
	
	@RequestMapping(value = "/uploadBillFile", method = RequestMethod.POST)
	@ResponseBody
	public String uploadBillFile(HttpServletRequest req,MultipartHttpServletRequest multiReq){
		//获得当前登录用户信息
		HttpSession session = req.getSession();
		SecurityContext context = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
		User user = (User)context.getAuthentication().getPrincipal();
		userThread.getTl().set(user);
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
		
		
		return fileTypeThread.getTl().get();
	}
	
	/**
	 * TODO 处理预收表 xls格式
	 * @param uploadfile
	 * @param issueId
	 * @param fileType
	 * @throws IOException
	 * @throws Exception
	 */
	public void saveXlsFile(MultipartFile uploadfile,String issueId,String fileType){
		
		try {
			InputStream is = uploadfile.getInputStream();
			
			Workbook workbook = Workbook.getWorkbook(is);
			
			if(null != workbook){
				Sheet sheet = workbook.getSheet(0);
				if(null != sheet){
					List list = new ArrayList();
					for(int i=1; i<sheet.getRows(); i++){
						Cell[] row = sheet.getRow(i);
						if(null != fileType && fileType.equals("file_yushou")){
							AdvancesReceived advancesReceived = saveAdvancesReceived(row,null,issueId);
							list.add(advancesReceived);
						}else if(null != fileType && fileType.equals("file_rebate")){
							EbRebateT ebRebateT = saveEbRebateT(row,null,issueId);
							list.add(ebRebateT);
						}else if(null != fileType && fileType.equals("file_unrebate")){
							EbUnrebateT unrebate = saveEbUnrebateT(row,null,issueId);
							list.add(unrebate);
						}else if(null != fileType && fileType.equals("file_netsum")){
							EbNetAmount ebNetAmount = saveEbNetAmount(row,null,issueId);
							list.add(ebNetAmount);
						}else if(null != fileType && fileType.equals("file_statistic")){
							EbSalesStatisticsT ebSalesStatisticsT = saveEbSalesStatisticsT(row,null,issueId);
							list.add(ebSalesStatisticsT);
						}else if(null != fileType && fileType.equals("file_useamount")){
							EbFoundsUseAmount ebFoundsUseAmount = saveEbFoundsUseAmount(row,null,issueId);
							list.add(ebFoundsUseAmount);
						}
					}
					
					handleTransactionalService.handleTransactional(list,issueId,fileType);
				}
			}
		} catch (MyException me) {
			// TODO Auto-generated catch block
			me.printStackTrace();
			throw me;
			
		}catch( IOException ie){
			ie.printStackTrace();
		    throw new MyException("处理 xls格式文件发生IO异常，上传文件失败");
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException("处理 xls格式文件发生异常，上传文件失败");
		}
	}
	
	/**
	 * TODO 处理预收表xlsx格式
	 * @param uploadfile
	 * @throws IOException 
	 */
	
	public void saveXlsxFile(MultipartFile uploadfile,String issueId,String fileType){
		
		try {
			InputStream is = uploadfile.getInputStream();
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			if(null != xssfWorkbook){
				XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
				if(null != sheet){
					List list = new ArrayList();
					for(int i=1; i<= sheet.getLastRowNum(); i++ ){
						XSSFRow r = sheet.getRow(i);
						if(null != r){
							if(null != fileType && fileType.equals("file_yushou")){
								AdvancesReceived advancesReceived = saveAdvancesReceived(null,r,issueId);
								list.add(advancesReceived);
							}else if(null != fileType && fileType.equals("file_rebate")){
								EbRebateT ebRebateT = saveEbRebateT(null,r,issueId);
								list.add(ebRebateT);
							}else if(null != fileType && fileType.equals("file_unrebate")){
								EbUnrebateT unrebate = saveEbUnrebateT(null,r,issueId);
								list.add(unrebate);
							}else if(null != fileType && fileType.equals("file_netsum")){
								EbNetAmount ebNetAmount = saveEbNetAmount(null,r,issueId);
								list.add(ebNetAmount);
							}else if(null != fileType && fileType.equals("file_statistic")){
								EbSalesStatisticsT ebSalesStatisticsT = saveEbSalesStatisticsT(null,r,issueId);
								list.add(ebSalesStatisticsT);
							}else if(null != fileType && fileType.equals("file_useamount")){
								EbFoundsUseAmount ebFoundsUseAmount = saveEbFoundsUseAmount(null,r,issueId);
								list.add(ebFoundsUseAmount);
							}
							
							
						}
					}
					handleTransactionalService.handleTransactional(list,issueId,fileType);
				}
			}
		} catch (MyException me) {
			// TODO Auto-generated catch block
			me.printStackTrace();
			throw me;
		}catch(IOException ie){
			ie.printStackTrace();
			throw new MyException("处理 xlsx格式文件发生IO异常，上传文件失败");
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException("处理 xls格式文件发生异常，上传文件失败");
		}
	}
	
	/**
	 * @author 刘宗峰
	 * @TODO  解析资金占用费计算表文件
	 * @param c
	 * @param r
	 * @param issueId
	 * @return
	 */
	public EbFoundsUseAmount saveEbFoundsUseAmount(Cell[] c,XSSFRow r,String issueId){
		
		EbFoundsUseAmount e = null;
		try {
			e = new EbFoundsUseAmount();
			String uuid = CommonUtils.obtainUUID();
			if(null == c && null != r){ //保存xlsx
				e.setId(uuid);                             //资金占用费表主键uuid
				e.setReturnNumber(getStr(r.getCell(0)));   //回款编号
				e.setYearDu(getStr(r.getCell(1)));         //年度
				e.setDuration(getStr(r.getCell(2)));       //期间
				e.setReturnDate(CommonUtils.strToDate(getStr(r.getCell(3))));//回款日期
				e.setCurrency(getStr(r.getCell(4)));       //货币
				e.setReturnRemarks(getStr(r.getCell(5)));   //回款备注
				e.setConsumer(getStr(r.getCell(6)));        //客户
				e.setConsignorName(getStr(r.getCell(7)));   //货主名称
				e.setReconciliationObj(getStr(r.getCell(8)));   //对账对象
				e.setOrderNumber(getStr(r.getCell(9)));    //订单编号
				e.setOrderDate(CommonUtils.strToDate(getStr(r.getCell(10))));//订货日期
				e.setIssuanceNo(getStr(r.getCell(11)));                      //发货单号
				e.setIssuanceDate(CommonUtils.strToDate(getStr(r.getCell(12))));//发货日期
				e.setOrderAmount(Double.parseDouble((getStrDouble(r.getCell(14)))));//订单金额
				e.setInvoicedAmount(Double.parseDouble(getStrDouble(r.getCell(14))));//开票金额
				e.setBackLibSum(Double.parseDouble(getStrDouble(r.getCell(15))));//回库销账金额
				e.setRebateSum(Double.parseDouble(getStrDouble(r.getCell(16))));// 留利销账金额
				e.setSpecialSum(Double.parseDouble(getStrDouble(r.getCell(17))));//特殊销账金额
				e.setOrderAmount(Double.parseDouble(getStrDouble(r.getCell(18))));//订单留利金额
				e.setReturnAmount(Double.parseDouble(getStrDouble(r.getCell(19))));//回款金额
				e.setOrderDiscountSum(Double.parseDouble(getStrDouble(r.getCell(20))));//订单折后金额
				e.setTotalAgencyAmount(Double.parseDouble(getStrDouble(r.getCell(21))));//代理金额合计
				e.setSpecialOffer(Double.parseDouble(getStrDouble(r.getCell(22))));//特价
				e.setPaymentDaysStart(CommonUtils.strToDate(getStr(r.getCell(23))));//账期起点日期
				e.setAmountReceivable(Double.parseDouble(getStrDouble(r.getCell(24))));//应收余额
				e.setPaymentDays(getStr(r.getCell(25)));//账期
				e.setFoundsUseAmount(Double.parseDouble(getStrDouble(r.getCell(26))));//资金占用费金额
				e.setOrderRemarks(getStr(r.getCell(27)));//订单备注
				e.setSalesman(getStr(r.getCell(28)));//业务员
				e.setServiceTypeVarchar(getStr(r.getCell(29)));//业务类型字符串型
				e.setCompany(getStr(r.getCell(30)));//公司
				e.setOperator(userThread.getTl().get().getUsername());//数据操作人
				e.setOperateDate(new Date());//操作时间
				e.setFrIssueId(issueId);//账单期数id
				e.setIsLive(1);//是否作废：0作废，1未作废
				
				
			}else if(null != c && null == r){//保存xls
				e.setId(uuid);                             //资金占用费表主键uuid
				e.setReturnNumber(c[0].getContents());   //回款编号
				e.setYearDu(c[1].getContents());         //年度
				e.setDuration(c[2].getContents());       //期间
				e.setReturnDate(CommonUtils.strToDate(c[3].getContents()));//回款日期
				e.setCurrency(c[4].getContents());       //货币
				e.setReturnRemarks(c[5].getContents());   //回款备注
				e.setConsumer(c[6].getContents());        //客户
				e.setConsignorName(c[7].getContents());   //货主名称
				e.setReconciliationObj(c[8].getContents());   //对账对象
				e.setOrderNumber(c[9].getContents());    //订单编号
				e.setOrderDate(CommonUtils.strToDate(c[10].getContents()));//订货日期
				e.setIssuanceNo(c[11].getContents());                      //发货单号
				e.setIssuanceDate(CommonUtils.strToDate(c[12].getContents()));//发货日期
				e.setOrderAmount(Double.parseDouble(preHandleData(c[13])));//订单金额
				e.setInvoicedAmount(Double.parseDouble(preHandleData(c[14])));//开票金额
				e.setBackLibSum(Double.parseDouble(preHandleData(c[15])));//回库销账金额
				e.setRebateSum(Double.parseDouble(preHandleData(c[16])));// 留利销账金额
				e.setSpecialSum(Double.parseDouble(preHandleData(c[17])));//特殊销账金额
				e.setOrderAmount(Double.parseDouble(preHandleData(c[18])));//订单留利金额
				e.setReturnAmount(Double.parseDouble(preHandleData(c[19])));//回款金额
				e.setOrderDiscountSum(Double.parseDouble(preHandleData(c[20])));//订单折后金额
				e.setTotalAgencyAmount(Double.parseDouble(preHandleData(c[21])));//代理金额合计
				e.setSpecialOffer(Double.parseDouble(preHandleData(c[22])));//特价
				e.setPaymentDaysStart(CommonUtils.strToDate(c[23].getContents()));//账期起点日期
				e.setAmountReceivable(Double.parseDouble(preHandleData(c[24])));//应收余额
				e.setPaymentDays(c[25].getContents());//账期
				e.setFoundsUseAmount(Double.parseDouble(preHandleData(c[26])));//资金占用费金额
				e.setOrderRemarks(c[27].getContents());//订单备注
				e.setSalesman(c[28].getContents());//业务员
				e.setServiceTypeVarchar(c[29].getContents());//业务类型字符串型
				e.setCompany(c[30].getContents());//公司
				e.setOperator(userThread.getTl().get().getUsername());//数据操作人
				e.setOperateDate(new Date());//操作时间
				e.setFrIssueId(issueId);//账单期数id
				e.setIsLive(1);//是否作废：0作废，1未作废
			}
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new MyException("解析资金占用费计算表文件，失败！");
		}
		return e;
	}
	
	/**
	 * @author 刘宗峰
	 * @TODO 解析销售业务统计总表文件
	 * @param c
	 * @param r
	 * @param issueId
	 * @return
	 */
	public EbSalesStatisticsT saveEbSalesStatisticsT(Cell[] c,XSSFRow r,String issueId){
		
		EbSalesStatisticsT e = null;
		try {
			e = new EbSalesStatisticsT();
			String uuid = CommonUtils.obtainUUID();
			if(null == c && null != r){ //保存xlsx
				
				e.setId(uuid);   //销售业务统计总表主键uuid
				e.setDataType(getStr(r.getCell(0))); //数据分类
				e.setOrderNo(getStr(r.getCell(1))); // 销售订单编号
				e.setOrderDetailNo(getStr(r.getCell(2))); //订单明细编号
				e.setOrderLineNo(Integer.parseInt(getStrDouble(r.getCell(3))));//订单行号
				e.setOrderDate(CommonUtils.strToDate(getStr(r.getCell(4))));//订单日期
				e.setPurchaseUnits(getStr(r.getCell(5)));//购货单位
				e.setMaterialCode(getStr(r.getCell(6)));//物料编码
				e.setMaterialName(getStr(r.getCell(7)));//物料名称
				e.setSpecTyp(getStr(r.getCell(8)));//规格型号
				e.setCurrencyCode(getStr(r.getCell(9)));//货币内码
				e.setExchangeRate(Double.parseDouble(getStrDouble(r.getCell(10)))); //汇率
				e.setOrderNum(Double.parseDouble(getStrDouble(r.getCell(11))));//订货数量
				e.setOrderAmount(Double.parseDouble(getStrDouble(r.getCell(12))));//订货金额
				e.setAgencyAmount(Double.parseDouble(getStrDouble(r.getCell(13)))); //代理金额
				e.setBusinessNo(getStr(r.getCell(14)));//业务单号
				e.setBusinessDate(CommonUtils.strToDate(getStr(r.getCell(15))));//业务日期
				e.setSendNum(Double.parseDouble(getStrDouble(r.getCell(16))));//发货数量
				e.setSendAmount(Double.parseDouble(getStrDouble(r.getCell(17))));//发货金额
				e.setInvoiceNum(Double.parseDouble(getStrDouble(r.getCell(18))));//发票数量
				e.setInvoiceAmount(Double.parseDouble(getStrDouble(r.getCell(19))));//发票金额
				e.setPaylibaryAmount(Double.parseDouble(getStrDouble(r.getCell(20))));//回库销账金额
				e.setSpecialAmount(Double.parseDouble(getStrDouble(r.getCell(21))));//特殊销账金额
				e.setRebateAmount(Double.parseDouble(getStrDouble(r.getCell(22))));//留返利销账金额
				e.setPaymentAmount(Double.parseDouble(getStrDouble(r.getCell(23))));//回款销账金额
				e.setPerformance(Double.parseDouble(getStrDouble(r.getCell(24)))); //履约质保
				e.setProfitAmount(Double.parseDouble(getStrDouble(r.getCell(25))));//留利金额
				e.setReceivableAmount(Double.parseDouble(getStrDouble(r.getCell(26))));//应收余额
				e.setShipperName(getStr(r.getCell(27)));//货主名称
				e.setServiceType(getStr(r.getCell(28)));//业务类型
				e.setReconciliationObj(getStr(r.getCell(29)));//对账对象
				e.setWorker(getStr(r.getCell(30)));//职员
				e.setDept(getStr(r.getCell(31)));//部门
				e.setArea(getStr(r.getCell(32)));//区域
				e.setSysProject(getStr(r.getCell(33)));//sysProject
				e.setRemarks(getStr(r.getCell(34))); //备注
				e.setChangeRemarks(getStr(r.getCell(35)));//修改备注
				e.setUseUnit(getStr(r.getCell(36)));//使用单位
				e.setVipdept(getStr(r.getCell(37)));//VIP部门
				e.setVipdeptManage(getStr(r.getCell(38)));//VIP部门经理
				e.setTrade(getStr(r.getCell(39)));//行业
				e.setPlin(getStr(r.getCell(40)));//产品线
				e.setEndSendDate(CommonUtils.strToDate(getStr(r.getCell(41))));//最后发货日期
				e.setOperator(Integer.parseInt(null == userThread.getTl().get().getUsername() ? "1":userThread.getTl().get().getUsername()));//操作人
				e.setOperateDate(new Date());//操作时间
				e.setIsLive(1);//是否有效：1有效，0无效；默认为1
				e.setFrIssueId(issueId);//外键期数id
				
			}else if(null != c && null == r){//保存xls
				e.setId(uuid);   //销售业务统计总表主键uuid
				e.setDataType(c[0].getContents()); //数据分类
				e.setOrderNo(c[1].getContents()); // 销售订单编号
				e.setOrderDetailNo(c[2].getContents()); //订单明细编号
				e.setOrderLineNo(Integer.parseInt(preHandleData(c[3])));//订单行号
				e.setOrderDate(CommonUtils.strToDate(c[4].getContents()));//订单日期
				e.setPurchaseUnits(c[5].getContents());//购货单位
				e.setMaterialCode(c[6].getContents());//物料编码
				e.setMaterialName(c[7].getContents());//物料名称
				e.setSpecTyp(c[8].getContents());//规格型号
				e.setCurrencyCode(c[9].getContents());//货币内码
				e.setExchangeRate(Double.parseDouble(preHandleData(c[10]))); //汇率
				e.setOrderNum(Double.parseDouble(preHandleData(c[11])));//订货数量
				e.setOrderAmount(Double.parseDouble(preHandleData(c[12])));//订货金额
				e.setAgencyAmount(Double.parseDouble(preHandleData(c[13]))); //代理金额
				e.setBusinessNo(c[14].getContents());//业务单号
				e.setBusinessDate(CommonUtils.strToDate(c[15].getContents()));//业务日期
				e.setSendNum(Double.parseDouble(preHandleData(c[16])));//发货数量
				e.setSendAmount(Double.parseDouble(preHandleData(c[17])));//发货金额
				e.setInvoiceNum(Double.parseDouble(preHandleData(c[18])));//发票数量
				e.setInvoiceAmount(Double.parseDouble(preHandleData(c[19])));//发票金额
				e.setPaylibaryAmount(Double.parseDouble(preHandleData(c[20])));//回库销账金额
				e.setSpecialAmount(Double.parseDouble(preHandleData(c[21])));//特殊销账金额
				e.setRebateAmount(Double.parseDouble(preHandleData(c[22])));//留返利销账金额
				e.setPaymentAmount(Double.parseDouble(preHandleData(c[23])));//回款销账金额
				e.setPerformance(Double.parseDouble(preHandleData(c[24]))); //履约质保
				e.setProfitAmount(Double.parseDouble(preHandleData(c[25])));//留利金额
				e.setReceivableAmount(Double.parseDouble(preHandleData(c[26])));//应收余额
				e.setShipperName(c[27].getContents());//货主名称
				e.setServiceType(c[28].getContents());//业务类型
				e.setReconciliationObj(c[29].getContents());//对账对象
				e.setWorker(c[30].getContents());//职员
				e.setDept(c[31].getContents());//部门
				e.setArea(c[32].getContents());//区域
				e.setSysProject(c[33].getContents());//sysProject
				e.setRemarks(c[34].getContents()); //备注
				e.setChangeRemarks(c[35].getContents());//修改备注
				e.setUseUnit(c[36].getContents());//使用单位
				e.setVipdept(c[37].getContents());//VIP部门
				e.setVipdeptManage(c[38].getContents());//VIP部门经理
				e.setTrade(c[39].getContents());//行业
				e.setPlin(c[40].getContents());//产品线
				e.setEndSendDate(CommonUtils.strToDate(c[41].getContents()));//最后发货日期
				e.setOperator(Integer.parseInt(null == userThread.getTl().get().getUsername() ? "1":userThread.getTl().get().getUsername()));//操作人
				e.setOperateDate(new Date());//操作时间
				e.setIsLive(1);//是否有效：1有效，0无效；默认为1
				e.setFrIssueId(issueId);//外键期数id
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			throw new MyException("解析销售业务统计总表文件,失败！");
		}
		return e;
	}
	/**
	 * @author 刘宗峰
	 * @TODO 解析应收净额文件
	 * @param c
	 * @param r
	 * @param issueId
	 * @return
	 */
	public EbNetAmount saveEbNetAmount(Cell[] c,XSSFRow r,String issueId){
		
		EbNetAmount ebNetAmount = null;
		try {
			ebNetAmount = new EbNetAmount();
			String uuid = CommonUtils.obtainUUID();
			if(null == c && null != r){ //保存xlsx
				ebNetAmount.setId(uuid);                                                         //应收净额表主键uuid
				ebNetAmount.setReconciliationObj(getStr(r.getCell(0)));                          //对账对象
				ebNetAmount.setWorker(getStr(r.getCell(1)));                                     //职员
				ebNetAmount.setSalesOrderNo(getStr(r.getCell(2)));                               //销售订单编号
				ebNetAmount.setConsumer(getStr(r.getCell(3)));                                   //客户
				ebNetAmount.setBuildingUser(getStr(r.getCell(4)));                               //使用单位
				ebNetAmount.setConsignorName(getStr(r.getCell(5)));                              //货主名称
				ebNetAmount.setSystemItem(getStr(r.getCell(6)));                                 //系统项目
				ebNetAmount.setOrderDate(CommonUtils.strToDate(getStr(r.getCell(7))));           //订单日期
				ebNetAmount.setOrderSum(Double.parseDouble(getStrDouble(r.getCell(8))));               //订单金额
				ebNetAmount.setReceivedPaymentsSum(Double.parseDouble(getStrDouble(r.getCell(9))));    //回款金额
				ebNetAmount.setPerformanceGuarantee(getStr(r.getCell(10)));                      //履约质保
				ebNetAmount.setBackLibSum(Double.parseDouble(getStrDouble(r.getCell(11))));            //回库销账金额
				ebNetAmount.setSpecialHandingSum(Double.parseDouble(getStrDouble(r.getCell(12))));     //特殊处理金额
				ebNetAmount.setUndeliverySum(Double.parseDouble(getStrDouble(r.getCell(13))));         //未发货金额
				ebNetAmount.setUnsettleAccountsSum(Double.parseDouble(getStrDouble(r.getCell(14))));   //未结账款余额
				ebNetAmount.setInvoiceClassfication(getStr(r.getCell(15)));    //发票分类
				ebNetAmount.setInvoiceNo(getStr(r.getCell(16)));                                 //发票单号
				ebNetAmount.setFinishedInvoiceSum(Double.parseDouble(getStrDouble(r.getCell(17))));    //已开发票金额
				ebNetAmount.setAgencyAmount(Double.parseDouble(getStrDouble(r.getCell(18))));          //代理金额
				ebNetAmount.setProfitAmount(Double.parseDouble(getStrDouble(r.getCell(19))));          //留利金额
				ebNetAmount.setCheckedAccountRemarks(getStr(r.getCell(20)));                     //对账核实备注
				ebNetAmount.setFinalDeliveryDate(CommonUtils.strToDate(getStr(r.getCell(21))));  //最终发货日期
				ebNetAmount.setCurrency(getStr(r.getCell(22)));                                  //货币
				ebNetAmount.setExchangeRate(Double.parseDouble(getStrDouble(r.getCell(23))));          //汇率
				ebNetAmount.setNetAmountOperator(Integer.parseInt(null == userThread.getTl().get().getUsername() ? "1":userThread.getTl().get().getUsername()));                                          //应收净额数据操作人
				ebNetAmount.setNetAmountOperateDate(new Date());                                 //应收净额操作时间
				ebNetAmount.setFrIssueId(issueId);                                               //账单期数id
				ebNetAmount.setNetAmountIsLive(1);                                               //应收净额是否作废：1有效，0作废
				
				
			}else if(null != c && null == r){//保存xls
				ebNetAmount.setId(uuid);                                                         //应收净额表主键uuid
				ebNetAmount.setReconciliationObj(c[0].getContents());                          //对账对象
				ebNetAmount.setWorker(c[1].getContents());                                     //职员
				ebNetAmount.setSalesOrderNo(c[2].getContents());                               //销售订单编号
				ebNetAmount.setConsumer(c[3].getContents());                                   //客户
				ebNetAmount.setBuildingUser(c[4].getContents());                               //使用单位
				ebNetAmount.setConsignorName(c[5].getContents());                              //货主名称
				ebNetAmount.setSystemItem(c[6].getContents());                                 //系统项目
				ebNetAmount.setOrderDate(CommonUtils.strToDate(c[7].getContents()));           //订单日期
				ebNetAmount.setOrderSum(Double.parseDouble(preHandleData(c[8])));               //订单金额
				ebNetAmount.setReceivedPaymentsSum(Double.parseDouble(preHandleData(c[9])));    //回款金额
				ebNetAmount.setPerformanceGuarantee(c[10].getContents());                      //履约质保
				ebNetAmount.setBackLibSum(Double.parseDouble(preHandleData(c[11])));            //回库销账金额
				ebNetAmount.setSpecialHandingSum(Double.parseDouble(preHandleData(c[12])));     //特殊处理金额
				ebNetAmount.setUndeliverySum(Double.parseDouble(preHandleData(c[13])));         //未发货金额
				ebNetAmount.setUnsettleAccountsSum(Double.parseDouble(preHandleData(c[14])));   //未结账款余额
				ebNetAmount.setInvoiceClassfication(c[15].getContents());    //发票分类
				ebNetAmount.setInvoiceNo(c[16].getContents());                                 //发票单号
				ebNetAmount.setFinishedInvoiceSum(Double.parseDouble(preHandleData(c[17])));    //已开发票金额
				ebNetAmount.setAgencyAmount(Double.parseDouble(preHandleData(c[18])));          //代理金额
				ebNetAmount.setProfitAmount(Double.parseDouble(preHandleData(c[19])));          //留利金额
				ebNetAmount.setCheckedAccountRemarks(c[20].getContents());                     //对账核实备注
				ebNetAmount.setFinalDeliveryDate(CommonUtils.strToDate(c[21].getContents()));  //最终发货日期
				ebNetAmount.setCurrency(c[22].getContents());                                  //货币
				ebNetAmount.setExchangeRate(Double.parseDouble(preHandleData(c[23])));          //汇率
				ebNetAmount.setNetAmountOperator(Integer.parseInt(null == userThread.getTl().get().getUsername() ? "1":userThread.getTl().get().getUsername()));                                          //应收净额数据操作人
				ebNetAmount.setNetAmountOperateDate(new Date());                                 //应收净额操作时间
				ebNetAmount.setFrIssueId(issueId);                                               //账单期数id
				ebNetAmount.setNetAmountIsLive(1);                                               //应收净额是否作废：1有效，0作废
				
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new MyException("解析应收净额文件，失败！");
		}
		return ebNetAmount;
	}
	
	/**
	 * @author 刘宗峰
	 * @TODO 解析留返利往来清单文件
	 * @param c
	 * @param r
	 * @param issueId
	 * @return
	 */
	public EbRebateT saveEbRebateT(Cell[] c,XSSFRow r,String issueId){
		
		EbRebateT ebRebateT = null;
		try {
			ebRebateT = new EbRebateT();
			String uuid = CommonUtils.obtainUUID();
			if(null == c && null != r){ //保存xlsx
				ebRebateT.setId(uuid);                                                  //留返利表主键，uuid
				ebRebateT.setReconciliationObj(getStr(r.getCell(0)));                   //对账对象
				ebRebateT.setRebateNoticeBillNo(getStr(r.getCell(1)));                  //留返利通知单号
				ebRebateT.setRebateDate(CommonUtils.strToDate(getStr(r.getCell(2))));   //日期
				ebRebateT.setSalesOrderNo(getStr(r.getCell(3)));                        //销售订单编号
				ebRebateT.setRebateAbstract(getStr(r.getCell(4)));                      //摘要
				ebRebateT.setConsignorName(getStr(r.getCell(5)));                       //货主名称
				ebRebateT.setRebateSum(Double.parseDouble(getStrDouble(r.getCell(6))));       //留返利金额
				ebRebateT.setRebateCheckSum(Double.parseDouble(getStrDouble(r.getCell(7))));  //留返利核销金额
				ebRebateT.setRebateProperty(getStr(r.getCell(8)));                      //留返利性质
				ebRebateT.setRebateRemarks(getStr(r.getCell(9)));                       //备注
				ebRebateT.setRebateOperator(Integer.parseInt(null == userThread.getTl().get().getUsername() ? "1":userThread.getTl().get().getUsername()));                                      //留返利表数据操作人
				ebRebateT.setRebateOperatDate(new Date());                              //留返利数据操作时间
				ebRebateT.setRebateIsLive(1);                                           //留返利数据是否作废：0作废，1有效
				ebRebateT.setFrIssueId(issueId);                                        //账单期数id,外键
				
			}else if(null != c && null == r){//保存xls
				ebRebateT.setId(uuid);                                                  //留返利表主键，uuid
				ebRebateT.setReconciliationObj(c[0].getContents());                   //对账对象
				ebRebateT.setRebateNoticeBillNo(c[1].getContents());                  //留返利通知单号
				ebRebateT.setRebateDate(CommonUtils.strToDate(c[2].getContents()));   //日期
				ebRebateT.setSalesOrderNo(c[3].getContents());                        //销售订单编号
				ebRebateT.setRebateAbstract(c[4].getContents());                      //摘要
				ebRebateT.setConsignorName(c[5].getContents());                       //货主名称
				ebRebateT.setRebateSum(Double.parseDouble(preHandleData(c[6])));       //留返利金额
				ebRebateT.setRebateCheckSum(Double.parseDouble(preHandleData(c[7])));  //留返利核销金额
				ebRebateT.setRebateProperty(c[8].getContents());                      //留返利性质
				ebRebateT.setRebateRemarks(c[9].getContents());                       //备注
				ebRebateT.setRebateOperator(Integer.parseInt(null == userThread.getTl().get().getUsername() ? "1":userThread.getTl().get().getUsername()));                                      //留返利表数据操作人
				ebRebateT.setRebateOperatDate(new Date());                              //留返利数据操作时间
				ebRebateT.setRebateIsLive(1);                                           //留返利数据是否作废：0作废，1有效
				ebRebateT.setFrIssueId(issueId);                                        //账单期数id,外键
			}
		} catch (NumberFormatException e) {

			e.printStackTrace();
			throw new MyException("解析留返利往来清单文件，失败！");
		}
		return ebRebateT;
	}
	
	/**
	 * @author 刘宗峰
	 * @TODO 解析未核销的留返利文件
	 * @param c
	 * @param r
	 * @param issueId
	 * @return
	 */
	public EbUnrebateT saveEbUnrebateT(Cell[] c,XSSFRow r,String issueId){
		
		EbUnrebateT unrebate = null;
		try {
			unrebate = new EbUnrebateT();
			String uuid = CommonUtils.obtainUUID();
			if(null == c && null != r){ //保存xlsx
				unrebate.setId(uuid);                                                   //主键uuid
				unrebate.setReconciliationObjNo(getStr(r.getCell(0)));                  //对账对象编号
				unrebate.setReconciliationObjName(getStr(r.getCell(1)));                //对账对象名称
				unrebate.setRebateNetAmount(Double.parseDouble(getStrDouble(r.getCell(2))));  //留返利净额
				unrebate.setFrIssueId(issueId);                                         //账单期数id
				unrebate.setUnrebateOperator(Integer.parseInt(null == userThread.getTl().get().getUsername() ? "1":userThread.getTl().get().getUsername()));                                     //未核销留返利表操作人
				unrebate.setUnrebateOperateDate(new Date());                            //操作人操作时间
				unrebate.setUnrebateIsLive(1);                                          //未核销留返利是否作废 :1 未作废，0作废，默认为1
				
			}else if(null != c && null == r){//保存xls
				unrebate.setId(uuid);                                                   //主键uuid
				unrebate.setReconciliationObjNo(c[0].getContents());                    //对账对象编号
				unrebate.setReconciliationObjName(c[1].getContents());                  //对账对象名称
				unrebate.setRebateNetAmount(Double.parseDouble(preHandleData(c[2])));    //对账对象名称
				unrebate.setFrIssueId(issueId);                                         //账单期数id
				unrebate.setUnrebateOperator(Integer.parseInt(null == userThread.getTl().get().getUsername() ? "1":userThread.getTl().get().getUsername()));                                     //未核销留返利表操作人
				unrebate.setUnrebateOperateDate(new Date());                            //操作人操作时间
				unrebate.setUnrebateIsLive(1);                                          //未核销留返利是否作废 :1 未作废，0作废，默认为1
				
			}
		} catch (NumberFormatException e) {

			e.printStackTrace();
			throw new MyException("解析未核销的留返利文件，失败！");
		}
		return unrebate;
	}
	
	/**
	 * @author 刘宗峰
	 * TODO 解析预收表文件
	 * @param r
	 * @param issueId
	 */
	public AdvancesReceived saveAdvancesReceived(Cell[] c,XSSFRow r,String issueId){
		
		AdvancesReceived ar =null;
		try {
			ar = new AdvancesReceived();
			String uuid = CommonUtils.obtainUUID();
			if(null == c && null != r){ //保存xlsx
				ar.setId(uuid);
				ar.setReconciliationObj(getStr(r.getCell(0)));  //对账对象
				ar.setCollectionType(getStr(r.getCell(1)));//收款类型
				ar.setCollectionNumber(getStr(r.getCell(2)));//收款单号
				ar.setCollectionDays(CommonUtils.strToDate(getStr(r.getCell(3))));//收款日期
				ar.setCurrency(getStr(r.getCell(4)));//币别
				ar.setExchangeRate(Double.parseDouble(getStrDouble(r.getCell(5))));//汇率
				ar.setConsumer(getStr(r.getCell(6)));//客户
				ar.setSalesman(getStr(r.getCell(7)));//业务员
				ar.setDepartment(getStr(r.getCell(8)));//部门
				ar.setAdvancesReceivedAbstract(getStr(r.getCell(9)));//摘要
				ar.setCollectionSum(Double.parseDouble(getStrDouble(r.getCell(10))));//收款金额
				ar.setUncheckedSum(Double.parseDouble(getStrDouble(r.getCell(11))));//未核销金额
				ar.setAdvancesReceivedOperator(userThread.getTl().get().getUsername());//操作人
				ar.setAdvancesReceivedOperateDate(new Date());//操作时间
				ar.setFrIssueId(issueId);//期数ID
				ar.setAdvancesReceivedIsLive(1);//是否删除
			}else if(null != c && null == r){//保存xls
				ar.setId(uuid);
				ar.setReconciliationObj(c[0].getContents());  //对账对象
				ar.setCollectionType(c[1].getContents());//收款类型
				ar.setCollectionNumber(c[2].getContents());//收款单号
				ar.setCollectionDays(CommonUtils.strToDate(c[3].getContents()));//收款日期
				ar.setCurrency(c[4].getContents());//币别
				ar.setExchangeRate(Double.parseDouble(preHandleData(c[5])));//汇率
				ar.setConsumer(c[6].getContents());//客户
				ar.setSalesman(c[7].getContents());//业务员
				ar.setDepartment(c[8].getContents());//部门
				ar.setAdvancesReceivedAbstract(c[9].getContents());//摘要
				ar.setCollectionSum(Double.parseDouble(preHandleData(c[10])));//收款金额
				ar.setUncheckedSum(Double.parseDouble(preHandleData(c[11])));//未核销金额
				ar.setAdvancesReceivedOperator(userThread.getTl().get().getUsername());//操作人
				ar.setAdvancesReceivedOperateDate(new Date());//操作时间
				ar.setFrIssueId(issueId);//期数ID
				ar.setAdvancesReceivedIsLive(1);//是否删除
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new MyException("解析预收表文件，失败！");
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
        	return xssfRow.getRawValue();
        }
        return null;
    }
    @SuppressWarnings("static-access")
    private String getStrDouble(XSSFCell xssfRow) {
    	if(null == xssfRow || "".equals(xssfRow)){
    		return "0.0";
    	}
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_STRING) {
            return null == xssfRow.getStringCellValue() ? "0.0" : xssfRow.getStringCellValue();
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            return String.valueOf(xssfRow.getNumericCellValue());
        } else if(xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN){
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if(xssfRow.getCellType() == xssfRow.CELL_TYPE_BLANK){
        	return "0.0";
        } else if(xssfRow.getCellType() == xssfRow.CELL_TYPE_FORMULA){
        	return xssfRow.getRawValue();
        }else{
        	 return "0.0";
        }
       
    }
	/**
	 * @author 刘宗峰
	 * @TODO 预处理xls double数据
	 * @param c
	 * @return
	 */
    private String preHandleData(Cell c){
    	
    	if(null != c && (c.getType().toString().equals("Empty") || null == c.getContents() || "".equals(c.getContents()))){
    		return "0.0";
    	}else{
    		return c.getContents();
    	}
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
	/**
	 * @author 刘宗峰
	 * @return
	 */
	@RequestMapping(value = "/searchEbIssue", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> searchEbIssueUnpublished(){
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("issueState", 0); //未发布状态的期数
		//1.查询未发布状态的期数，取最新
		List<EbIssueT> ei = ebIssueTMapper.queryIssueByMap(paramMap);
		EbIssueT issue = null;
		if(null != ei && ei.size() >0){
			issue = (EbIssueT)ei.get(0);
			paramMap.put("issue", issue);
		}
		//2.存在未发布状态的期数，再查看是否有本期上传的数据
		if(null != issue){
			//1.根据期数id查询应收净额表
			int netAmountCount = ebNetAmountMapper.searchCountByIssueId(issue.getId());
			paramMap.put("file_netsum", netAmountCount);
			//2.根据期数id查询留返利往来清单
			int rebateCount = ebRebateTMapper.searchCountByIssueId(issue.getId());
			paramMap.put("file_rebate", rebateCount);
			//3.根据期数id查询未核销的留返利
			int unRebateCount = ebUnrebateTMapper.searchCountByIssueId(issue.getId());
			paramMap.put("file_unrebate", unRebateCount);
			//4.根据期数id查询销售业务统计总表
			int statisticsCount = ebSalesStatisticsTMapper.searchCountByIssueId(issue.getId());
			paramMap.put("file_statistic", statisticsCount);
			//5.根据期数id查询预收表
			int arCount = advancesReceivedMapper.searchCountByIssueId(issue.getId());
			paramMap.put("file_yushou", arCount);
			//6.根据期数id查询资金占用费计算表
			int useAmountCount = ebFoundsUseAmountMapper.searchCountByIssueId(issue.getId());
			paramMap.put("file_useamount", useAmountCount);
		}
		
		return paramMap;
	}
}
