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
@PreAuthorize("isAuthenticated()")
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
        long accountNo = accountDao.getAccountIdByUserId(transfer.getAccount_from());
        transfer.setAccount_from(accountNo);
        accountNo = accountDao.getAccountIdByUserId(transfer.getAccount_to());
        transfer.setAccount_to(accountNo);
        transfer = transferDao.createTransfer(transfer);
        return transfer;
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public List<Transfer> listTransfers(Principal principal) {
        Account account = accountDao.getAccountByUserName(principal.getName());
        long accountId = account.getId();
        return transferDao.listTransfersByAccountId(accountId);
    }




}
