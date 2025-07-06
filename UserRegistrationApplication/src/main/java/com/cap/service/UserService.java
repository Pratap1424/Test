package com.cap.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import com.cap.entities.UserInfo;
import com.cap.payload.RegisterRequest;

public interface UserService {
	
	Optional<UserInfo> validateUser(String email, String password);

    void registerUser(RegisterRequest request);

	boolean emailExists(String email);

    List<UserInfo> getAllUsers();

    UserInfo getUserByEmail(String email);

    //ByteArrayInputStream generateUserCSV();

}
