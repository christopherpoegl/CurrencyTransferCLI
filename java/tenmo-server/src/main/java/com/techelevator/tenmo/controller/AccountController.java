package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/account")
public class AccountController {

    @Autowired
    AccountDao accountDao;
    @Autowired
    TransferDao transferDao;
    @Autowired
    UserDao userDao;

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {return accountDao.getBalanceByUserName(principal.getName());}

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listUsers(){
        return userDao.findAll();
    }

    @RequestMapping(path = "/transfer/send", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        System.out.println("FROM: " + transfer.getAccount_from());
        System.out.println("TO: " + transfer.getAccount_to());


        long accountNo = accountDao.getAccountIdByUserId(transfer.getAccount_from());
        transfer.setAccount_from(accountNo);
        accountNo = accountDao.getAccountIdByUserId(transfer.getAccount_to());
        transfer.setAccount_to(accountNo);
        transfer = transferDao.createTransfer(transfer);
        return transfer;
        /*Transfer testTransfer = new Transfer();
        testTransfer.setAccount_from(accountDao.getAccountIdByUserId(1002));
        System.out.println("Send Account is: "+ testTransfer.getAccount_from());
        testTransfer.setAccount_to(accountDao.getAccountIdByUserId(1001));
        System.out.println("To Account is: "+ testTransfer.getAccount_to());
        testTransfer.setAmount(new BigDecimal("100.00"));
        testTransfer.setTransfer_status_desc("Approved");
        testTransfer.setTransfer_status_id(2);
        testTransfer.setTransfer_type_desc("Send");
        testTransfer.setTransfer_type_id(2);
        testTransfer = transferDao.createTransfer(testTransfer);*/
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public List<Transfer> listTransfers(Principal principal) {
        Account account = accountDao.getAccountByUserName(principal.getName());
        long accountId = account.getId();
        return transferDao.listTransfersByAccountId(accountId);
    }

    @RequestMapping(path = "/string", method = RequestMethod.POST)
    public void printString(@RequestBody String strung) {
        System.out.println(strung);
    }

}
