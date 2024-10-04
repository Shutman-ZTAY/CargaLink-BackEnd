package com.ipn.mx.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipn.mx.model.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

}
