package com.google.code.routing4db.mybatis.example;

import com.google.code.routing4db.dao.User;
import com.google.code.routing4db.dao.UserDao;
import com.google.code.routing4db.holder.RoutingHolder;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:write-master-read-slaves-mybatis.xml")
public class MybatisWriteMasterReadSlavesTest {

//    @Resource
//    UserDao userDao;

    @Resource
    UserDao userDaoTwo;

    @Resource
    JdbcTemplate jdbcTemplate;

    /**
     * 写 Master （localhost test）
     */
    @Test
    public void testWriteMasterReadSlaves() {
        for (int i = 1; i <= 10; i++) {
            User user = new User();
            long id = i;
            user.setId(id);
            user.setName("用户" + i);
            userDaoTwo.insert(user);

//            int count = jdbcTemplate.queryForInt("select count(*) from user where id = " + id);
//            Assert.assertEquals(count, 1);
//            System.out.println(user.getName());
        }
    }

    /**
     * 读 Slave
     */
    @Test
    public void testReadFromSlaves() {
        for (int i = 1; i <= 10; i++) {
            RoutingHolder.setCurrentDataSourceKey(null); // Master
            User user = userDaoTwo.getUserById(i);
            System.out.println("获取到的用户信息：" + user.getId() + " - " + user.getName() + "\n");
            Assert.assertNotNull(user);
        }
    }

    /**
     * 测试事务，采用mybatis的传统实现方式支持事务. 仅采用mapper接口，不支持事务
     */
    @Test
    public void testTransactionWithMybatisImpl() {
        for (int i = 10000; i <= 10008; i++) {
            long id = i;
            User user = new User();
            user.setId(id);
            user.setName("用户：" + i);
            try {
                userDaoTwo.insertWithTransaction(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 插入 master， 事务生效，插入失败 count 为 0
//            RoutingHolder.setCurrentDataSourceKey(null); // Master
            int count = jdbcTemplate.queryForInt("select count(*) from user where id = " + id);
            Assert.assertEquals(0, count);
        }
    }

    /**
     * 测试事务，采用 mybatis 的 mapper 不支持事务
     */
    @Test
    public void testWithMapperNotSupportTransaction() {
        for (int i = 10000; i <= 10008; i++) {
            long id = i;
            User user = new User();
            user.setId(id);
            user.setName("User" + i);
            try {
                // 插入 master， 事务生效，插入失败
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                userDaoTwo.insertWithTransaction(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 插入master，事务生效，插入失败 count 为 0
            RoutingHolder.setCurrentDataSourceKey(null); // Master
            int count = jdbcTemplate.queryForInt("select count(*) from user where id = " + id);
            Assert.assertEquals(0, count);
        }
    }

    /**
     * 删除数据
     */
    @Test
    public void cleanData() {
        for (int i = 0; i <= 10; i++) {
            long id = i;
            RoutingHolder.setCurrentDataSourceKey(null); // Master
            jdbcTemplate.execute("delete from user where id = " + id);
        }
        for (int i = 10000; i <= 10008; i++) {
            long id = i;
            RoutingHolder.setCurrentDataSourceKey(null); // Master
            jdbcTemplate.execute("delete from user where id = " + id);
        }
    }

}
