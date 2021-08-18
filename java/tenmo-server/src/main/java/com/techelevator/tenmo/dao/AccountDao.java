package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao {

    public BigDecimal getBalance(long userId);

    //public Transfer getTransfer();
    //public void transfer???;
}
