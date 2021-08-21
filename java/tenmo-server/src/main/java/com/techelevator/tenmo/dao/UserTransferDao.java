package com.techelevator.tenmo.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface UserTransferDao {

    void setUserTransferInfo(long transferId);

}
