package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private long transfer_id;
    private String transfer_type_desc;
    private String transfer_status_desc;
    private long account_from;
    private long account_to;
    private BigDecimal amount;



    public Transfer(long transfer_id, String transfer_type_desc, String transfer_status_desc, long account_from, long account_to, BigDecimal amount) {
        this.transfer_id = transfer_id;
        this.transfer_type_desc = transfer_type_desc;
        this.transfer_status_desc = transfer_status_desc;
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;

    }

    public long getTransfer_id() {
        return transfer_id;
    }

    public String getTransfer_type_desc() {
        return transfer_type_desc;
    }

    public String getTransfer_status_desc() {
        return transfer_status_desc;
    }

    public long getAccount_from() {
        return account_from;
    }

    public long getAccount_to() {
        return account_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}