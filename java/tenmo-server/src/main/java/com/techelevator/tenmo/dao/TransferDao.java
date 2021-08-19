package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {


    public BigDecimal send(Long sendingUserId, Long receivingUserId, BigDecimal amount);

    public List<Transfer> listTransfersByAccountId(long accountId);

    public Transfer getTransferByTransferId (long transferId);

    public String getTypeDesc(long transferTypeId);
}
