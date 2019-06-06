package com.google.code.routing4db.proxy;

import com.google.code.routing4db.holder.RoutingHolder;
import com.google.code.routing4db.strategy.RoutingStrategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态代理拦截接口的请求
 */
public class RoutingInvocationHanlder implements InvocationHandler {

    /**
     * 代理对象
     */
    private Object proxyTarget;

    /**
     * 代理接口上的方法
     */
    private Map<Method, Object> proxyInterfaceMethods;

    /**
     * 路由策略
     */
    private RoutingStrategy routingStrategy;

    /**
     * @param proxyTarget     代理对象
     * @param interfaceClass  接口class
     * @param routingStrategy 路由策略
     */
    public RoutingInvocationHanlder(Object proxyTarget, Class<?> interfaceClass, RoutingStrategy routingStrategy) {
        super();
        if (null == proxyTarget) {
            throw new IllegalArgumentException("DAO代理对象不能为空！");
        }
        if (null == interfaceClass) {
            throw new IllegalArgumentException("代理接口必须是一个接口类！");
        }
        if (null == routingStrategy) {
            throw new IllegalArgumentException("路由策略不能为空！");
        }

        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("interfaceClass 的 value 必须是一个接口类！");
        }

        if (!interfaceClass.isInstance(proxyTarget)) {
            throw new IllegalArgumentException("proxyTarget must be sub class of " + interfaceClass.getName() + "; but proxyTarget class is " + proxyTarget.getClass().getName() + ", which is not sub class of the interface.");
        }

        this.proxyTarget = proxyTarget;
        this.routingStrategy = routingStrategy;


        proxyInterfaceMethods = new HashMap<>();
        for (Method method : interfaceClass.getMethods()) {
            proxyInterfaceMethods.put(method, null);
        }
        for (Class<?> parentInterface : interfaceClass.getInterfaces()) {
            for (Method method : parentInterface.getMethods()) {
                proxyInterfaceMethods.put(method, null);
            }
        }
    }

    /**
     * 对接口上的方法执行路由逻辑，然后委托给实际对象，其它方法直接委托给代理对象
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!proxyInterfaceMethods.containsKey(method)) {
            return method.invoke(proxyTarget, args);
        }
        String preRoutingHolderKey = RoutingHolder.getCurrentDataSourceKey();
        try {
            routingStrategy.route(proxyTarget, method, args);
            return method.invoke(proxyTarget, args);
        } finally {
            RoutingHolder.setCurrentDataSourceKey(preRoutingHolderKey);
        }
    }

}
