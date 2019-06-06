package com.caimao.weixin.note.service;

import com.caimao.weixin.note.util.Pager;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T, PK extends Serializable> {

	T findById(PK id);

	int findAllCount(T t, String whereString);

	List<T> findByPager(Pager pager, String whereString, String orderbyString);

	Long save(T t);

	void update(T t);

	void delete(PK id);

	void delete(T t);

	List<T> findListByLeftJoin(Pager page, String whereString);

}
