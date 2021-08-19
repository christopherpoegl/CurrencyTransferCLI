package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AccountDaoTests extends TenmoDaoTests {

    private AccountDao accountDao;
    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        this.jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        accountDao = new JdbcAccountDao(jdbcTemplate);
        userDao = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void create_user_and_call_get_balance_for_id_returns_1000() {
        userDao.create("user", "password");
        BigDecimal balance = accountDao.getBalanceByUserName("user");
        Assert.assertEquals(new BigDecimal("1000.00"), balance);
    }

    @Test
    public void send_100_dollars_from_user1_to_user2() {
        userDao.create("user1", "password");
        userDao.create("user2", "password");

        long fromAccountId = accountDao.getAccountByUserName("user1").getId();
        long toAccountId = accountDao.getAccountByUserName("user2").getId();
        accountDao.updateAccountBalances(fromAccountId, toAccountId, new BigDecimal("100.00"));
        BigDecimal user1Balance = accountDao.getBalanceByAccountId(fromAccountId);
        BigDecimal user2Balance = accountDao.getBalanceByAccountId(toAccountId);

        Assert.assertTrue(""+ user1Balance,user1Balance.equals(new BigDecimal("900.00").setScale(2, RoundingMode.HALF_UP)));
        Assert.assertTrue(""+user2Balance, user2Balance.equals(new BigDecimal("1100.00").setScale(2, RoundingMode.HALF_UP)));
    }
}
