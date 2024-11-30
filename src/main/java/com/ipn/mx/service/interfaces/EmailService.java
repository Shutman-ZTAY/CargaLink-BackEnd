package com.ipn.mx.service.interfaces;

import java.io.IOException;

import com.ipn.mx.model.entity.Usuario;

import jakarta.mail.MessagingException;

public interface EmailService {

	public void sendBasicEmail(String to, String subject, String body) throws MessagingException;
	public void sendResetPassworMail(Usuario usuario) throws MessagingException, IOException;
	
}
