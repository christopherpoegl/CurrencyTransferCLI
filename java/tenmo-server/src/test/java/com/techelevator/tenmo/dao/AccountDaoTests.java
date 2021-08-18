package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AccountDaoTests extends TenmoDaoTests {
    private static final Account ACCOUNT_A = new Account(70000, 7000);

    private JdbcAccountDao accountDao;
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
    public void create_user_and_call_get_balance_for_user_id_returns_1000() {
        userDao.create("user", "password");
        long userId = userDao.findIdByUsername("user");
        BigDecimal balance = accountDao.getBalance(userId);
        Assert.assertTrue(balance.equals(new BigDecimal("1000.00").setScale(2, RoundingMode.HALF_UP)));
    }
}
