package com.caimao.weixin.note.dao;

import com.caimao.weixin.note.domain.Capital;
import com.caimao.weixin.note.util.Pager;

import java.util.List;

public interface CapitalDao extends BaseDao<Capital, Integer> {

    public Integer findAdminListCount(String where);

    public List<Capital> findAdminListPage(Pager pager, String where, String order);

}
