package rest.config;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import rest.utils.MyException;

@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect {

    @Before("@annotation(LZFDS)")
    public void beforeSwitchDS(JoinPoint point){

        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
        String dataSource = DataSourceContextHolder.DEFAULT_DS;
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);

            // 判断是否存在@LZFDS注解
            if (method.isAnnotationPresent(LZFDS.class)) {
            	LZFDS annotation = method.getAnnotation(LZFDS.class);
                // 取出注解中的数据源名
                dataSource = annotation.value();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("aop动态数据源失败");
        }
        System.out.println("===========数据源："+dataSource);
        // 切换数据源
        DataSourceContextHolder.setDB(dataSource);

    }


    @After("@annotation(LZFDS)")
    public void afterSwitchDS(JoinPoint point){

        DataSourceContextHolder.clearDB();

    }
}
