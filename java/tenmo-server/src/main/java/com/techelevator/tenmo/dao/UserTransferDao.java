package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.UserTransfer;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public interface UserTransferDao {

    UserTransfer getUserTransferInfo(long transferId);

    List<UserTransfer> getUserTransferList(long accountId);

}
