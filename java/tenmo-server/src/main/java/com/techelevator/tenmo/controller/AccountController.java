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
    public Account getAccount(Principal principal) {
        return accountDao.getAccountByUserName(principal.getName());
    }

    @RequestMapping(path = "/balance/2", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {return accountDao.getBalanceByUserName(principal.getName());}

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listUsers(){
        return userDao.findAll();
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        transfer = transferDao.createTransfer(transfer);
        return transfer;
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public List<Transfer> listTransfers(Principal principal) {
        long accountId = accountDao.getAccountByUserName(principal.getName()).getId();
        return transferDao.listTransfersByAccountId(accountId);
    }




}
