package rest.config;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import rest.mybatis.dao.misDao.UsertMapper;
import rest.mybatis.model.misModel.Usert;
import rest.service.HandleTransactionalService;
import rest.utils.MD5Utils;

@Component
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	HttpSession session; //这里可以获取到request
	@Autowired
	private HandleTransactionalService handleTransactionalService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//1.查询用户数据		
		Usert u = handleTransactionalService.selectUByComNum(username);
		
		//2.查询权限
		
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		//3.构造用户信息
		User user = new User(u.getComNum(),u.getUserPassword(),true,true,true,true,authorities);
		//4.保存到session，用redis保存
		
		return user;
	}
	
}








