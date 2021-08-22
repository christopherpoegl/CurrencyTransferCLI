package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserTransfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {


    public Transfer createTransfer(Transfer transfer);

    public void send(Long sendingAccountId, Long receivingAccountId, BigDecimal amount, Long transferStatusId);

    public List<Transfer> listTransfersByAccountId(long accountId);

    public Transfer getTransferByTransferId (long transferId);

    public String getTypeDesc(long transferTypeId);

    public String getStatusDesc(long transferStatusId);

    public long getTransfer_status_id(Transfer transfer);

    public String setTransferStatusId(Long statusId, Long transferId);

    public List<Transfer> listPendingTransfersByAccountId(long accountId);

}
