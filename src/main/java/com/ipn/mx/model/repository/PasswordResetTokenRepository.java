package com.ipn.mx.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.PasswordResetToken;

import jakarta.transaction.Transactional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

	@Query("SELECT prt FROM PasswordResetToken prt "
			+ "WHERE prt.token = :token")
	Optional<PasswordResetToken> findByToken(@Param("token") String token);

	@Transactional
	@Modifying
	@Query("DELETE FROM PasswordResetToken prt WHERE prt.token = :token")
    void deleteByToken(@Param("token") String token);
	
}
