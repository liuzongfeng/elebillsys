package rest.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * 取得spring的上下文，取得自定义线程类
 * @author 刘宗峰
 *
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext context;
	
	private ApplicationContextProvider(){};
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		context = applicationContext;
	}
	
	
	public static<T> T getBean(String name, Class<T> aClass){
		return context.getBean(name,aClass);
	}
	
}
