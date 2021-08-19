package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcTransferDaoTest extends TenmoDaoTests {

    private TransferDao transferDao;
    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    @Before
    public void setup() {
        this.jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        this.userDao = new JdbcUserDao(jdbcTemplate);
        this.transferDao = new JdbcTransferDao(jdbcTemplate);
        this.accountDao = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void create_a_transfer_and_send_money() {
        userDao.create("user1", "password");
        userDao.create("user2", "password");

        long user1Id = userDao.findIdByUsername("user1");
        long user2Id = userDao.findIdByUsername("user2");
        long account1Id = accountDao.getAccountByUserId(user1Id).getId();
        long account2Id = accountDao.getAccountByUserId(user2Id).getId();
        BigDecimal actualTransferValue = transferDao.send(account1Id, account2Id, new BigDecimal("100.00"));

        BigDecimal actualAccount1Balance = accountDao.getBalanceByAccountId(account1Id);
        BigDecimal actualAccount2Balance = accountDao.getBalanceByAccountId(account2Id);

        Assert.assertTrue(""+actualTransferValue, actualTransferValue.equals(new BigDecimal("100.00").setScale(2, RoundingMode.HALF_UP)));
        Assert.assertTrue(""+actualAccount1Balance, actualAccount1Balance.equals(new BigDecimal("900.00").setScale(2, RoundingMode.HALF_UP)));
        Assert.assertTrue(""+actualAccount2Balance, actualAccount2Balance.equals(new BigDecimal("1100.00").setScale(2, RoundingMode.HALF_UP)));
    }

    @Test
    public void transfer_type_id_two_returns_send() {
        String desc = transferDao.getTypeDesc(2);
        Assert.assertEquals("Send", desc);
    }

    @Test
    public void get_transfers_by_account_returns_correct_number_of_transfers() {
        userDao.create("user1", "password");
        userDao.create("user2", "password");

        long user1Id = userDao.findIdByUsername("user1");
        long user2Id = userDao.findIdByUsername("user2");
        long account1Id = accountDao.getAccountByUserId(user1Id).getId();
        long account2Id = accountDao.getAccountByUserId(user2Id).getId();

        BigDecimal thisDoesntMatter = transferDao.send(account1Id, account2Id, new BigDecimal("100.00"));
        thisDoesntMatter = transferDao.send(account1Id, account2Id, new BigDecimal("300.00"));

        Assert.assertEquals(2, transferDao.listTransfersByAccountId(account1Id).size());
        Assert.assertEquals(2, transferDao.listTransfersByAccountId(account2Id).size());
    }

    @Test
    public void get_transfer_by_id_returns_transfer() {
        userDao.create("user1", "password");
        userDao.create("user2", "password");

        long user1Id = userDao.findIdByUsername("user1");
        long user2Id = userDao.findIdByUsername("user2");
        long account1Id = accountDao.getAccountByUserId(user1Id).getId();
        long account2Id = accountDao.getAccountByUserId(user2Id).getId();

        BigDecimal thisDoesntMatter = transferDao.send(account1Id, account2Id, new BigDecimal("100.00"));
        List<Transfer> transfers =transferDao.listTransfersByAccountId(account1Id);
        long transferId = transfers.get(0).getTransfer_id();
        BigDecimal actualValue = transferDao.getTransferByTransferId(transferId).getAmount();
        Assert.assertEquals(new BigDecimal("100.00"), actualValue);
    }

}