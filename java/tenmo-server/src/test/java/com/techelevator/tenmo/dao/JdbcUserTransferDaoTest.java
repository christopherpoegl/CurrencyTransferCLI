package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserTransfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcUserTransferDaoTest extends TenmoDaoTests {

    private TransferDao transferDao;
    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    private UserTransferDao userTransferDao;

    @Before
    public void setup() {
        this.jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        this.userDao = new JdbcUserDao(jdbcTemplate);
        this.transferDao = new JdbcTransferDao(jdbcTemplate);
        this.accountDao = new JdbcAccountDao(jdbcTemplate);
        this.userTransferDao = new JdbcUserTransferDao(jdbcTemplate);
    }

    @Test
    public void userTransfer_test_returns_userTransfer() {
        userDao.create("a", "1");
        System.out.println(userDao.findIdByUsername("a"));
        userDao.create("b", "1");

        Transfer transfer = new Transfer();
        transfer.setTransfer_status_id(2);
        transfer.setTransfer_type_id(2);
        transfer.setTransfer_status_desc(transferDao.getStatusDesc(2));
        System.out.println("status: "+transfer.getTransfer_status_desc());
        transfer.setTransfer_type_desc(transferDao.getTypeDesc(2));
        System.out.println("status: "+ transfer.getTransfer_type_desc());
        transfer.getAccount_from();
        transfer.setAccount_to(userDao.findIdByUsername("b"));
        transfer.setAmount(new BigDecimal("100.00"));

        transfer = transferDao.createTransfer(transfer);
        UserTransfer userTransfer = userTransferDao.getUserTransferInfo(transfer.getTransfer_id());
        Assert.assertEquals("a", userTransfer.getAccountFromUserName());
        Assert.assertEquals("b", userTransfer.getAccountToUsername());
        Assert.assertEquals(new BigDecimal("100.00"), userTransfer.getAmount());

    }

}