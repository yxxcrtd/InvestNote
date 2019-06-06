package com.google.code.routing4db.standby;

import com.google.code.routing4db.dao.User;
import com.google.code.routing4db.dao.UserDao;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:master-standby.xml")
public class MasterStandbyDataSourceTest extends TestCase {

    @Resource
    UserDao userDao;

    /**
     * 插入到 Standby 数据库中
     */
    @Test
    public void testMasterStandby() throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
            User user = new User();
            long id = i;
            user.setId(id);
            user.setName("用户" + i);
            // 插入
            try {
                userDao.insert(user);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(20 * 1000);
            }
            System.out.println(user.getName());
        }
    }


    @Resource
    JdbcTemplate jdbcTemplate;

    /**
     * 删除数据
     */
    @Test
    public void cleanData() {
        for (int i = 0; i <= 10; i++) {
            long id = i;
            jdbcTemplate.execute("delete from user where id = " + id);
        }
    }

}
