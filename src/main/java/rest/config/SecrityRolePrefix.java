package rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.stereotype.Component;
/**
 * 权限前缀
 * @author 刘宗峰
 *
 */
@Component
public class SecrityRolePrefix extends DefaultWebSecurityExpressionHandler{

	@Bean
	public DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(){
		DefaultMethodSecurityExpressionHandler d = new DefaultMethodSecurityExpressionHandler();
		d.setDefaultRolePrefix("AUTH_URL_ROLE_");
		return d;
	}
	@Bean
	public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler(){
		DefaultWebSecurityExpressionHandler d = new DefaultWebSecurityExpressionHandler();
		d.setDefaultRolePrefix("AUTH_URL_ROLE_");
		return d;
	}
}
