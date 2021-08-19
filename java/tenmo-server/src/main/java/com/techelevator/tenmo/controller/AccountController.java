package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public BigDecimal getBalance(Principal principal) {
        String username = principal.getName();
        return accountDao.getBalanceByUserName("user");
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listUsers(){
        return userDao.findAll();
    }

    @RequestMapping(path = "/transfer/send", method = RequestMethod.POST)
    public BigDecimal sendTransfer(Principal userSending, User userReceiving, BigDecimal amount){
        return transferDao.send()
    }




}
