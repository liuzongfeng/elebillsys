package rest.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import rest.mybatis.dao.eBillDao.EbIssueTMapper;
import rest.mybatis.model.eBillModel.EbIssueT;

@Controller
public class eleBillController {

	@Autowired
	private EbIssueTMapper ebIssueTMapper;
	
	@RequestMapping(value = "/uploadBillFile", method = RequestMethod.POST)
	@ResponseBody
	public void uploadBillFile(HttpServletRequest req,MultipartHttpServletRequest multiReq){
		//获得上传的文件
		MultipartFile uploadfile = multiReq.getFile("upfile");
		//期数开始时间
		String issueStartDate = req.getParameter("issueStartDate");
		//期数结束时间
		String issueEndDate = req.getParameter("issueEndDate");
		//期数ID
		String issueId  = req.getParameter("issueObjId");
		
		String fileName = uploadfile.getOriginalFilename();
		
		System.out.println(uploadfile.getOriginalFilename());
		System.out.println(uploadfile.getName());
		System.out.println(uploadfile.getContentType());
		System.out.println(uploadfile.getSize());
	
	}
	
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
