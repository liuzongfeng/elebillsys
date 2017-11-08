package rest.mybatis;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.github.pagehelper.PageHelper;

@Configuration
@EnableTransactionManagement
public class MyBatisConfig implements TransactionManagementConfigurer {

    @Autowired
    DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("rest.mybatis.model");
        
        
      //��ҳ���
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        //���÷���
        properties.setProperty("dialect","mysql");                                         
        //�ò���Ĭ��Ϊfalse;����Ϊtrueʱ���ὫRowBounds��һ������offset����pageNumҳ��ʹ��;��startPage�е�pageNumЧ��һ��
        properties.setProperty("offsetAsPageNum", "true"); 
        //�ò���Ĭ��Ϊfalse;��Ϊtrueʱ��ʹ��RowBounds��ҳ�����count��ѯ
        properties.setProperty("rowBoundsWithCount", "true");
        //����Ϊtrueʱ�����pageSize=0����RowBounds.limit = 0�ͻ��ѯ��ȫ���Ľ��;�൱��û��ִ�з�ҳ��ѯ�����Ƿ��ؽ����Ȼ��Page����
        properties.setProperty("pageSizeZero", "true");
   
        //���ú���ʱ�����pageNum<1���ѯ��һҳ�����pageNum>pages���ѯ���һҳ ;���ú���ʱ�����pageNum<1��pageNum>pages�᷵�ؿ�����
        properties.setProperty("reasonable", "false");
//        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        //��������pageNum,pageSize,count,pageSizeZero,reasonable,������ӳ�����Ĭ��ֵ
        properties.setProperty("params", "pageNum=start;pageSize=limit;");
        //always���Ƿ���PageInfo����,check��鷵�������Ƿ�ΪPageInfo,none����Page
        pageHelper.setProperties(properties);

        //��Ӳ��
        bean.setPlugins(new Interceptor[]{pageHelper});


      //���XMLĿ¼
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath:rest/mybatis/mapping/*/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
    
    
}