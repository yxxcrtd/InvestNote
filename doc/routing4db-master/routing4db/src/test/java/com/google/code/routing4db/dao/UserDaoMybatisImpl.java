package com.google.code.routing4db.dao;

import com.google.code.routing4db.holder.RoutingHolder;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class UserDaoMybatisImpl extends SqlSessionDaoSupport implements UserDao {

    public int insert(User user) {
        System.out.println("================= Mybatis 的 insert =================");
        int returnId = this.getSqlSession().insert("insert", user);

        RoutingHolder.setCurrentDataSourceKey("slaveDataSourceOne");
        this.getSqlSession().insert("insert", user);

        return returnId;
    }

    public User getUserById(long id) {
        System.out.println("================= Mybatis 的 getUserById =================");
        return this.getSqlSession().selectOne("getUserById", id);
    }

    @Transactional
    public void insertWithTransaction(User user) {
        System.out.println("================= Mybatis 的 插入（有事务）=================");
        this.getSqlSession().insert("insert", user);
        throw new RuntimeException("xxx");
    }

    @Override
    public void excludeMethod() {

    }

}
