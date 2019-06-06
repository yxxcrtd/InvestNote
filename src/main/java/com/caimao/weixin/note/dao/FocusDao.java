package com.caimao.weixin.note.dao;

import com.caimao.weixin.note.domain.Focus;

public interface FocusDao extends BaseDao<Focus, Integer> {

	Integer findFocusCountByUserId(String where, Integer userId);

	Focus findFocusByUserIdAndFocusId(Integer userId, Integer otherId);

}
