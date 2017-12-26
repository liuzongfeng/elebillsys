package rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Value("${server.servlet-path}")
    String servletPath;
	@Autowired
	private  MyUserDetailsService myUserDetailsService;
	@Autowired
	private SecrityRolePrefix secrityRolePrefix;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
					.antMatchers("/**/assets/**","/**/css/**","/**/js/**","/**/img/**","/**/bootstrap/**","/**/bootstrap/css/**","/**/bootstrap/js/**","/**/axios-master/**").permitAll()
					.anyRequest().authenticated()
					.and()
					.formLogin()
						.loginPage(servletPath+"/pages/login.html")
						.usernameParameter("username")
						.passwordParameter("password")
						.loginProcessingUrl("/miselebill/login")
						.defaultSuccessUrl(servletPath+"/pages/index.html")
						.permitAll()
						.and()
					.logout().permitAll()
					.and()
					.csrf().disable();
						//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		
		
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService).passwordEncoder(new MyPasswordEncoder());
		
	}
	
}
