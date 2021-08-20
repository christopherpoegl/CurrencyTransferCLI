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

        Transfer transfer = new Transfer();
        transfer.setTransfer_status_id(2);
        transfer.setTransfer_type_id(2);
        transfer.setTransfer_status_desc(transferDao.getStatusDesc(2));
        transfer.setTransfer_type_desc(transferDao.getTypeDesc(2));
        transfer.setAccount_from(accountDao.getAccountByUserName("user1").getId());
        transfer.setAccount_to(accountDao.getAccountByUserName("user2").getId());
        transfer.setAmount(new BigDecimal("100.00"));

        Transfer completedTransfer = transferDao.createTransfer(transfer);
        BigDecimal actualTransferValue = completedTransfer.getAmount();
        BigDecimal actualAccount1Balance = accountDao.getAccountByUserName("user1").getBalance();
        BigDecimal actualAccount2Balance = accountDao.getAccountByUserName("user2").getBalance();

        Assert.assertTrue(""+actualTransferValue, actualTransferValue.equals(new BigDecimal("100.00").setScale(2, RoundingMode.HALF_UP)));
        Assert.assertTrue(""+actualAccount1Balance, actualAccount1Balance.equals(new BigDecimal("900.00").setScale(2, RoundingMode.HALF_UP)));
        Assert.assertTrue(""+actualAccount2Balance, actualAccount2Balance.equals(new BigDecimal("1100.00").setScale(2, RoundingMode.HALF_UP)));
    }

    @Test
    public void getStatusDesc_2_returns_approved() {
        Assert.assertEquals("Approved", transferDao.getStatusDesc(2));
    }

    @Test
    public void getTypeDesc_2_returns_send() {
        Assert.assertEquals("Send", transferDao.getTypeDesc(2));
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


        Transfer transfer = new Transfer();
        transfer.setTransfer_status_id(2);
        transfer.setTransfer_status_desc(transferDao.getStatusDesc(2));
        transfer.setTransfer_type_id(2);
        transfer.setTransfer_type_desc(transferDao.getTypeDesc(2));
        transfer.setAccount_from(accountDao.getAccountByUserName("user1").getId());
        transfer.setAccount_to(accountDao.getAccountByUserName("user2").getId());
        transfer.setAmount(new BigDecimal("100.00"));

        transfer = transferDao.createTransfer(transfer);
        transfer.setAmount(new BigDecimal("300.00"));
        transfer = transferDao.createTransfer(transfer);

        Assert.assertEquals(2, transferDao.listTransfersByAccountId(accountDao.getAccountByUserName("user1").getId()).size());
        Assert.assertEquals(2, transferDao.listTransfersByAccountId(accountDao.getAccountByUserName("user2").getId()).size());
    }

    @Test
    public void get_transfer_by_id_returns_transfer() {
        userDao.create("user1", "password");
        userDao.create("user2", "password");

        Transfer transfer = new Transfer();
        transfer.setTransfer_status_id(2);
        transfer.setTransfer_status_desc(transferDao.getStatusDesc(2));
        transfer.setTransfer_type_id(2);
        transfer.setTransfer_type_desc(transferDao.getTypeDesc(2));
        transfer.setAccount_from(accountDao.getAccountByUserName("user1").getId());
        transfer.setAccount_to(accountDao.getAccountByUserName("user2").getId());
        transfer.setAmount(new BigDecimal("100.00"));

        transfer = transferDao.createTransfer(transfer);
        long account1Id = accountDao.getAccountByUserName("user1").getId();
        long account2Id = accountDao.getAccountByUserName("user2").getId();

        List<Transfer> transfers =transferDao.listTransfersByAccountId(account1Id);
        long transferId = transfers.get(0).getTransfer_id();
        BigDecimal actualValue = transferDao.getTransferByTransferId(transferId).getAmount();
        Assert.assertEquals(new BigDecimal("100.00"), actualValue);
    }

}