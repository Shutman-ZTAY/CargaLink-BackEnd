package com.ipn.mx.service.implementation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ipn.mx.model.entity.PasswordResetToken;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.repository.PasswordResetTokenRepository;
import com.ipn.mx.service.interfaces.EmailService;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
    private JavaMailSender mailSender;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	@Value("${spring.mail.username}")
	private String CORREO;
	@Value("${temlates.reset.password}")
	private String TEMPLATE_PATH;
	@Value("${frontend.link}")
	private String FRONT_LINK;
	
	@Override
	public void sendBasicEmail(String to, String subject, String body) throws MessagingException {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(CORREO);
        mailSender.send(message);
	}

	@Override
	public void sendResetPassworMail(Usuario usuario) throws MessagingException, IOException {
		PasswordResetToken prt = PasswordResetToken.builder().usuario(usuario).build();
		prt = passwordResetTokenRepository.save(prt);
		
		String template = Files.readString(Paths.get(TEMPLATE_PATH), StandardCharsets.UTF_8);
		template = template.replace("\n", "").replace("\r", "");
		template = template.replace("https://tusitio.com/reset-password?token=TU_TOKEN_UNICO", FRONT_LINK + "/reset-password?token=" + prt.getToken());
		System.out.println(template);
		MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(usuario.getCorreo());
        helper.setSubject("Restablecer contraseña");
        helper.setText(template, true);
        helper.setFrom(CORREO);

        mailSender.send(message);
	}

}
