package com.caimao.weixin.note.service.impl;

import com.caimao.weixin.note.dao.BaseDao;
import com.caimao.weixin.note.service.BaseService;
import com.caimao.weixin.note.util.Pager;
import com.caimao.weixin.note.util.StockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

public class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseServiceImpl.class);
	
	@Autowired
	protected BaseDao<T, PK> baseDao;

	@Autowired
	protected StockUtil stockUtil;

	public T findById(PK id) {
		return (T) baseDao.findById(id);
	}

	public int findAllCount(T t, String whereString) {
		return baseDao.findAllCount(t, whereString);
	}

	public List<T> findByPager(Pager pager, String whereString, String orderbyString) {
		return baseDao.findByPager(pager, whereString, orderbyString);
	}

	public Long save(T t) {
		return baseDao.save(t);
	}

	public void update(T t) {
		baseDao.update(t);
	}

	public void delete(PK id) {
		baseDao.delete(id);
	}

	public void delete(T t) {
		baseDao.delete(t);
	}

	public List<T> findListByLeftJoin(Pager page, String whereString) {
		return baseDao.findListByLeftJoin(page, whereString);
	}

}
