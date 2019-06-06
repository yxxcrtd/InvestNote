package com.google.code.routing4db.dao;

public class UserDaoImpl implements UserDao {

	public int insert(User user) {
		return 0;
	}

	public User getUserById(long id) {
		User user = new User();
		user.setId(id);
		user.setName("Mock");
		return user;
	}

	public void insertWithTransaction(User user) {
		System.out.println("============================== 没有 insert 操作 =================");
	}

	@Override
	public void excludeMethod() {}

}
