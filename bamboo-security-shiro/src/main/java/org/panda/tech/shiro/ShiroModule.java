package org.panda.tech.shiro;

import org.springframework.context.annotation.ComponentScan;

/**
 * 安全鉴权Shiro组件自动扫描机制
 *
 * @author fangen
 **/
@ComponentScan(basePackageClasses = ShiroModule.class)
public class ShiroModule {
}
