package com.google.code.routing4db.mybatis;

import com.google.code.routing4db.proxy.RountingProxyFactory;
import com.google.code.routing4db.strategy.RoutingStrategy;
import org.mybatis.spring.mapper.MapperFactoryBean;

public class RoutingMapperFactoryBean<T> extends MapperFactoryBean<T> {

    /**
     * 路由策略
     */
    private RoutingStrategy routingStrategy;

    @Override
    public T getObject() throws Exception {
        T target = super.getObject();
        Class<T> interfaceClass = this.getObjectType();
        return RountingProxyFactory.proxy(target, interfaceClass, routingStrategy);
    }

    public void setRoutingStrategy(RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
    }

    @Override
    protected void checkDaoConfig() {
        super.checkDaoConfig();
        if (null == routingStrategy) {
            throw new IllegalArgumentException("路由策略不能为空！");
        }
    }

}
