package rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rest.config.LZFDS;
import rest.mybatis.dao.eBillDao.EbIssueTMapper;
import rest.mybatis.dao.misDao.UsertMapper;
import rest.mybatis.model.eBillModel.EbIssueT;
import rest.mybatis.model.misModel.Usert;

@Controller
public class UserController {

	@Autowired
	private UsertMapper usertMapper;
	@Autowired
	private EbIssueTMapper ebIssueTMapper;
	
	@RequestMapping("/hello")
	@ResponseBody
	@LZFDS("misDS")
	public String sayHello(){
		
		Usert usert = usertMapper.selectByPrimaryKey(3255);
		
		return usert.getUserName() + " hello world";
	}
	
	@RequestMapping("/hello2")
	@ResponseBody
	public String sayHello2(){
		
		EbIssueT issue = ebIssueTMapper.selectByPrimaryKey("00606f94db3611e7a1da001708575bfc");
		
		return  issue.getIssueName() + " hello world";
	}
	
}
