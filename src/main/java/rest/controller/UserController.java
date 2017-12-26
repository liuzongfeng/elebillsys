package rest.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	/**
	 * @author 刘宗峰
	 * @TODO 查询当前对账对象，（根据权限）如果是商务人员，则返回空
	 * @param session
	 * @return
	 */
	@LZFDS("misDS")
	@RequestMapping("/searchCurrentObj")
	@ResponseBody
	public Map<String,String> searchCurrentObj(HttpSession session){
		
		SecurityContext context = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
		User user = (User)context.getAuthentication().getPrincipal();
		
		Usert u = usertMapper.selectByComNum(user.getUsername());
		
		Collection<GrantedAuthority> authorities = user.getAuthorities();
		int i =0;
		for(GrantedAuthority ga : authorities){
			if(ga.getAuthority().equals("AUTH_URL_ROLE_DUIZHANG_EMP")){
				i++;
			}else{
				
			}
		}
		if(i >0){
			Map<String,String> m = new HashMap<String,String>();
			m.put("text", u.getUserName());
			m.put("value", u.getUserId().toString());
			return m;
		}else{
			return null;
		}
		
	}
	
	
}
