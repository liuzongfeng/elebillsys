package rest.config;

import java.util.HashSet;
import java.util.List;
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

import rest.mybatis.dao.misDao.TUserResourcesMapper;
import rest.mybatis.model.misModel.TUserResources;
import rest.mybatis.model.misModel.Usert;
import rest.service.HandleTransactionalService;

@Component
public class MyUserDetailsService implements UserDetailsService {

	
	@Autowired
	private HandleTransactionalService handleTransactionalService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//1.查询用户数据		
		Usert u = handleTransactionalService.selectUByComNum(username);
		
		//2.查询权限
		List<TUserResources> UserResources = handleTransactionalService.selectListByComNum(username);
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		if(null != UserResources && UserResources.size() >0){
			for(TUserResources ur : UserResources){
				System.out.println(ur.getResource());
				authorities.add(new SimpleGrantedAuthority(ur.getResource()));
			}
		}
		
		
		//3.构造用户信息
		User user = new User(u.getComNum(),u.getUserPassword(),true,true,true,true,authorities);
		
		
		return user;
	}
	
}








