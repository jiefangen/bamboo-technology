package org.panda.tech.data.mybatis.support;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.panda.tech.data.common.DataCommons;
import org.panda.tech.data.datasource.DynamicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源配置支持
 *
 * @author fangen
 **/
public abstract class DynamicDataSourceSupport {

    @Bean
    public DynamicDataSource dynamicDataSource(@Qualifier(DataCommons.DATASOURCE_PRIMARY) DataSource primaryDataSource) {
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DataCommons.DATASOURCE_PRIMARY, primaryDataSource);
        if (getTargetDataSource() != null && !getTargetDataSource().isEmpty()) { // 自定义数据源加载
            targetDataSource.putAll(getTargetDataSource());
        }
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSource);
        dataSource.setDefaultTargetDataSource(primaryDataSource);
        return dataSource;
    }

    @Bean
    public SqlSessionFactory SqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(getMapperLocationPattern()));
        return bean.getObject();
    }

    protected abstract String getMapperLocationPattern();

    protected Map<Object, Object> getTargetDataSource() {
        return new HashMap<>();
    }
}
