package com.google.code.routing4db.spring;

import com.google.code.routing4db.proxy.RountingProxyFactory;
import com.google.code.routing4db.strategy.RoutingStrategy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class RoutingSpringFactoryBean<T> implements FactoryBean<T>, InitializingBean {

    /**
     * 代理的接口
     */
    private Class<T> targetInterface;

    /**
     * 代理对象
     */
    private Object targetObject;

    /**
     * 路由策略
     */
    private RoutingStrategy routingStrategy;

    /**
     * 返回代理对象
     */
    public T getObject() throws Exception {
        return RountingProxyFactory.proxy(targetObject, targetInterface, routingStrategy);
    }

    public Class<?> getObjectType() {
        return targetInterface;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setTargetInterface(Class<T> targetInterface) {
        this.targetInterface = targetInterface;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public void setRoutingStrategy(RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
    }

    public void afterPropertiesSet() throws Exception {
        if (null == targetObject || null == targetInterface || null == routingStrategy) {
            throw new IllegalArgumentException("Spring中DAO接口代理中的参数【target, interfaceClass, routingStrategy】都不能为空！");
        }
    }

}
