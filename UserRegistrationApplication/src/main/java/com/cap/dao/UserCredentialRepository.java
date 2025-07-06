package com.cap.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cap.entities.UserCredential;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long>{

	Optional<UserCredential> findByEmail(String email);
    Optional<UserCredential> findByEmailAndPassword(String email, String password);
}
