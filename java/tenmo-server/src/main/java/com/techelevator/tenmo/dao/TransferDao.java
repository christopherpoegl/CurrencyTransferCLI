package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {


    public BigDecimal send(Long sendingUserId, Long receivingUserId, BigDecimal amount);

   // public List<Transfer> getTransfersByUserId(long userId);

    //Transfer get(long transferId);


}
