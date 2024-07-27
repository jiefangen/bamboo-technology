package org.panda.tech.auth;

import org.springframework.context.annotation.ComponentScan;

/**
 * 安全鉴权组件自动扫描机制
 *
 * @author fangen
 **/
@ComponentScan(basePackageClasses = AuthModule.class)
public class AuthModule {
}
