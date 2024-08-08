package com.mkh.tableOrder.config.db;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * <b>클래스 설명</b>  : Datasource 설정 
 * @author : DKI
 */
@Configuration
@MapperScan(value={"com.mkh.tableOrder.api.mapper"}, sqlSessionFactoryRef="masterSqlSessionFactory")
@EnableTransactionManagement
public class DatasourceConfiguration {

    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix="spring.datasource.primary")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name="masterSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource, ApplicationContext applicationContext) throws Exception{
        //세션 생성 시, 빌드된 DataSource를 세팅하고 SQL문을 관리할 mapper.xml의 경로를 알려준다.
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(masterDataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mariadb/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name="masterSqlSessionTemplate")
    public SqlSessionTemplate masterSqlSessionTemplate(SqlSessionFactory masterSqlSessionFactory) throws Exception{
        return new SqlSessionTemplate(masterSqlSessionFactory);
    }
}