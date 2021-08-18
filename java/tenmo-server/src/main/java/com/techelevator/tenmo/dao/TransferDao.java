package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {


    Transfer send(BigDecimal amount);

    List<Transfer> getTransfersByUserId(long userId);

    Transfer get(long transferId);


}
