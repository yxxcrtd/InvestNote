package com.google.code.routing4db.dao;

public interface UserDao {

    int insert(User user);

    User getUserById(long id);

    void insertWithTransaction(User user);

    void excludeMethod();

}
