package com.caimao.weixin.note.service.impl;

import org.springframework.stereotype.Service;

import com.caimao.weixin.note.dao.FocusDao;
import com.caimao.weixin.note.domain.Focus;
import com.caimao.weixin.note.service.FocusService;

@Service
public class FocusServiceImpl extends BaseServiceImpl<Focus, Integer> implements FocusService {

	@Override
	public Integer findFocusCountByUserId(String where, Integer userId) {
		return ((FocusDao) baseDao).findFocusCountByUserId(where, userId);
	}

	@Override
	public Focus findFocusByUserIdAndFocusId(Integer userId, Integer otherId) {
		return ((FocusDao) baseDao).findFocusByUserIdAndFocusId(userId, otherId);
	}

}
