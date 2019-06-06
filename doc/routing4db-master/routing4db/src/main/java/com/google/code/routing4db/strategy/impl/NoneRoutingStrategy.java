package com.google.code.routing4db.strategy.impl;

import com.google.code.routing4db.strategy.RoutingStrategy;

import java.lang.reflect.Method;

public class NoneRoutingStrategy implements RoutingStrategy {

	public void route(Object target, Method method, Object[] args) {
		// Do nothing
	}

}
