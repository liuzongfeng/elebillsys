package rest.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

    @Bean(name = "elebillsysDS")
    @ConfigurationProperties(prefix = "spring.datasource.ele-ds") // application.properteis中对应属性的前缀
    public DataSource elebillsysDS() {
        return DataSourceBuilder.create().build();
    }




    @Bean(name = "misDS")
    @ConfigurationProperties(prefix = "spring.datasource.mis-ds") // application.properteis中对应属性的前缀
    public DataSource misDS() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 动态数据源: 通过AOP在不同数据源之间动态切换
     * @return
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(elebillsysDS());

        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap<Object,Object>();
        dsMap.put("elebillsysDS", elebillsysDS());
        dsMap.put("misDS", misDS());

        dynamicDataSource.setTargetDataSources(dsMap);

        return dynamicDataSource;
    }
}
