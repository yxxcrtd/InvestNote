package com.caimao.weixin.note.service;

import com.caimao.weixin.note.domain.Focus;

public interface FocusService extends BaseService<Focus, Integer> {

	Integer findFocusCountByUserId(String where, Integer userId);

	Focus findFocusByUserIdAndFocusId(Integer userId, Integer otherId);

}
