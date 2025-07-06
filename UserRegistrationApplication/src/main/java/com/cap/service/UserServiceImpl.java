package com.cap.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cap.dao.UserCredentialRepository;
import com.cap.dao.UserInfoRepository;
import com.cap.entities.UserCredential;
import com.cap.entities.UserInfo;
import com.cap.payload.RegisterRequest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserCredentialRepository credentialRepo;

	@Autowired
	private UserInfoRepository infoRepo;

	@Override
	public void registerUser(RegisterRequest request) {
		UserCredential credential = new UserCredential();
		credential.setEmail(request.getEmail());
		credential.setPassword(request.getPassword());
		credentialRepo.save(credential);

		UserInfo info = new UserInfo();
		info.setFirstName(request.getFirstName());
		info.setLastName(request.getLastName());
		info.setEmail(request.getEmail());
		info.setMobileNumber(request.getMobileNumber());
	//	info.setUserType("U"); // default as User
		infoRepo.save(info);
	}

	@Override
	public boolean emailExists(String email) {
		return credentialRepo.findByEmail(email).isPresent();
	}

	@Override
	public Optional<UserInfo> validateUser(String email, String password) {
		Optional<UserCredential> credential = credentialRepo.findByEmailAndPassword(email, password);
		return credential.flatMap(c -> infoRepo.findByEmail(c.getEmail()));
	}

	@Override
	public List<UserInfo> getAllUsers() {

		return infoRepo.findAll();
	}

	@Override
	public UserInfo getUserByEmail(String email) {
		return infoRepo.findByEmail(email).orElseThrow(() ->
        new RuntimeException("User not found with email: " + email));
	}

}
