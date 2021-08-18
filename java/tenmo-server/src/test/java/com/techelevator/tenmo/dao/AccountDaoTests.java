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
        long userId = userDao.findIdByUsername("user");
        long accountId = accountDao.getAccountByUserId(userId).getId();
        BigDecimal balance = accountDao.getBalance(accountId);
        Assert.assertTrue(balance.equals(new BigDecimal("1000.00").setScale(2, RoundingMode.HALF_UP)));
    }

    @Test
    public void add_and_subtract_account_tests() {
        Account account = new Account(100, 100, new BigDecimal("1000.00"));
        account.addAmount(new BigDecimal("100.00"));
        Assert.assertTrue(new BigDecimal("1100.00").equals(account.getBalance()));
        account.subtractAmount(new BigDecimal("100.00"));
        Assert.assertTrue(new BigDecimal("1000.00").equals(account.getBalance()));
    }

    @Test
    public void send_100_dollars_from_user1_to_user2() {
        userDao.create("user1", "password");
        userDao.create("user2", "password");

        long fromUserId = userDao.findIdByUsername("user1");
        long fromAccountId = accountDao.getAccountByUserId(fromUserId).getId();
        long toUserId = userDao.findIdByUsername("user2");
        long toAccountId = accountDao.getAccountByUserId(toUserId).getId();
        accountDao.updateAccountBalances(fromAccountId, toAccountId, new BigDecimal("100.00"));
        BigDecimal user1Balance = accountDao.getBalance(fromAccountId);
        BigDecimal user2Balance = accountDao.getBalance(toAccountId);

        Assert.assertTrue(""+ user1Balance,user1Balance.equals(new BigDecimal("900.00").setScale(2, RoundingMode.HALF_UP)));
        Assert.assertTrue(""+user2Balance, user2Balance.equals(new BigDecimal("1100.00").setScale(2, RoundingMode.HALF_UP)));
    }
}
