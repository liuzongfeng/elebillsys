package rest.controller;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import rest.config.LZFDS;
import rest.mybatis.dao.eBillDao.EbIssueTMapper;
import rest.mybatis.dao.misDao.UsertMapper;
import rest.mybatis.model.eBillModel.EbIssueT;
import rest.mybatis.model.misModel.Usert;

@RestController
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
	/**
	 * 测试session中的取值
	 * @param session
	 * @return
	 */
	@RequestMapping("/checkPermision")
	public Collection<GrantedAuthority> checkPermision(HttpSession session){
		
		SecurityContext context = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
		User user = (User)context.getAuthentication().getPrincipal();
		Collection<GrantedAuthority> authorities = user.getAuthorities();
		
		return authorities;
	}
	
	
	
}
