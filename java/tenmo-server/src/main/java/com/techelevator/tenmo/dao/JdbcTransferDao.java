package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){this.jdbcTemplate = jdbcTemplate;}

    public Transfer createTransfer(Transfer transfer) {
        if (transfer.getTransfer_type_desc().equals("Send")) send(transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount(), transfer.getTransfer_status_id());
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?,?,?,?,?) RETURNING transfer_id";
        long transfer_id = jdbcTemplate.queryForObject(sql, Long.class, transfer.getTransfer_type_id(), transfer.getTransfer_status_id(), transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount());
        return getTransferByTransferId(transfer_id);
    }

    public void send(Long sendingAccountId, Long receivingAccountId, BigDecimal amount, Long transferStatusId){

        if(transferStatusId==2) {
            accountDao.updateAccountBalances(sendingAccountId, receivingAccountId, amount);
        }
    }

    public String setTransferStatusId(Long statusId, Long transferId){
        String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, statusId, transferId);
        return sql;
    }


    public List<Transfer> listTransfersByAccountId(long accountId){
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
                "JOIN accounts ON account_from = accounts.account_id OR account_to = accounts.account_id " +
                "WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    public List<Transfer> listPendingTransfersByAccountId(long accountId){
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
                "JOIN accounts ON account_from = accounts.account_id OR account_to = accounts.account_id " +
                "WHERE transfer_status_id = 1 AND account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    public Transfer getTransferByTransferId(long transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, "+
    "transfer_status_id, account_from, account_to, amount FROM transfers WHERE transfer_id = ? ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,transferId);
        if(results.next()) {
            transfer = mapRowToTransfer(results);
        }

        return transfer;
    }

    public String getTypeDesc(long transferTypeId) {
        String sql = "SELECT transfer_type_desc FROM transfer_types WHERE transfer_type_id = ?;";
        return jdbcTemplate.queryForObject(sql, String.class, transferTypeId);
    }

    public String getStatusDesc(long transferStatusId) {
        String sql = "SELECT transfer_status_desc FROM transfer_statuses WHERE transfer_status_id = ?;";
        return jdbcTemplate.queryForObject(sql, String.class, transferStatusId);
    }

    @Override
    public long getTransfer_status_id(Transfer transfer) {

        String sql = "SELECT transfer_status_id FROM transfer_statuses WHERE transfer_id = ?;";
        return jdbcTemplate.queryForObject(sql, Long.class, transfer.getTransfer_id());
    }

    public long getStatusId(String transferStatusDesc) {
        String sql = "SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = ?;";
        return jdbcTemplate.queryForObject(sql, Long.class, transferStatusDesc);
    }

    public long getTypeId(String transferTypeDesc) {
        String sql = "SELECT transfer_type_id FROM transfer_types WHERE transfer_type_desc = ?;";
        return jdbcTemplate.queryForObject(sql, Long.class, transferTypeDesc);
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer t = new Transfer();
        t.setTransfer_id(rs.getLong("transfer_id"));
        t.setTransfer_type_id(rs.getLong("transfer_type_id"));
        t.setTransfer_type_desc(getTypeDesc(t.getTransfer_type_id()));
        t.setTransfer_status_id(rs.getLong("transfer_status_id"));
        t.setTransfer_status_desc(getStatusDesc(t.getTransfer_status_id()));
        t.setAccount_from(rs.getLong("account_from"));
        t.setAccount_to(rs.getLong("account_to"));
        t.setAmount(new BigDecimal(rs.getString("amount")));

        return t;
    }
}
