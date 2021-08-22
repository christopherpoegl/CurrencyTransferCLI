package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dao.UserTransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserTransfer;
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
    @Autowired
    UserTransferDao userTransferDao;

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
    }

    @RequestMapping(path = "/transfer/setStatus", method = RequestMethod.POST)
    public String setStatusId(@RequestBody String statusId){
        String[] info = statusId.split(",");
        long sId = Long.parseLong(info[0]);
        long tId = Long.parseLong(info[1]);
        System.out.println(statusId);
        System.out.println(info[0]);
        System.out.println(info[1]);
        return transferDao.setTransferStatusId(sId, tId);
    }

    @RequestMapping(path = "/transfers/list", method = RequestMethod.GET)
    public List<UserTransfer> listTransfers(Principal principal) {
        Account account = accountDao.getAccountByUserName(principal.getName());
        long accountId = account.getId();
        return userTransferDao.getUserTransferList(accountId);
    }

    @RequestMapping(path = "/transfers/pending", method = RequestMethod.GET)
    public List<UserTransfer> listPendingTransfers(Principal principal) {
        Account account = accountDao.getAccountByUserName(principal.getName());
        long accountId = account.getId();
        return userTransferDao.getPendingUserTransferList(accountId);
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer transfer(@PathVariable("id") long id) {
        return transferDao.getTransferByTransferId(id);
    }

}
