package org.panda.tech.core.config.security.config;

import org.panda.tech.core.config.security.AuthenticationFilter;
import org.panda.tech.core.config.security.AuthorizationInterceptor;
import org.panda.tech.core.config.security.authority.AppSecurityMetadataSource;
import org.panda.tech.core.config.security.authority.AuthoritiesAppExecutor;
import org.panda.tech.core.config.security.executor.AuthoritiesAppExecutorImpl;
import org.panda.tech.core.config.security.executor.strategy.IndependentAuthStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 应用安全元数据源配置
 *
 * @author fangen
 **/
@Import({IndependentAuthStrategy.class, AuthoritiesAppExecutorImpl.class})
public class AppSecurityAutoConfiguration {

    @Autowired
    private AuthoritiesAppExecutor authoritiesAppExecutor;

    @Bean
    public AppSecurityMetadataSource appSecurityMetadataSource() {
        return new AppSecurityMetadataSource(authoritiesAppExecutor);
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(IndependentAuthStrategy.class);
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor() {
        return new AuthorizationInterceptor(IndependentAuthStrategy.class);
    }

}
