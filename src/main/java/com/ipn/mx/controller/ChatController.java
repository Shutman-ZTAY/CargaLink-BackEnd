package com.ipn.mx.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.dto.MessageDTO;
import com.ipn.mx.model.dto.ChatSeguro;
import com.ipn.mx.model.dto.MensajeSeguro;
import com.ipn.mx.model.dto.UsuarioSeguro;
import com.ipn.mx.model.entity.Chat;
import com.ipn.mx.model.entity.Mensaje;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.repository.ChatRepository;
import com.ipn.mx.model.repository.MensajeRepository;
import com.ipn.mx.model.repository.UsuarioRepository;

@RequestMapping("/api/chats")
@Controller
public class ChatController {
	@Autowired
	private MensajeRepository mensajeRepository;
	@Autowired
	private ChatRepository chatRepository;
	@Autowired
	private ControllerUtils controllerUtils;
	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("myChat")
	public ResponseEntity<?> obtenerAllMyChats(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		try {
			List<Chat> chats = chatRepository.findByUsuario(u);
			List<ChatSeguro> chatsseguros = chats.stream()
												.map(Chat::toChatSeguro)
												.collect(Collectors.toList());
			
			return ResponseEntity.ok(chatsseguros);
		}catch(Exception e) {
			return ControllerUtils.exeptionsResponse(e);
		}
	}
	@GetMapping("chat/{usuario2}")
	public  ResponseEntity<?> obtenerChat(@PathVariable String usuario2){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		Optional<Usuario> usuario22 = usuarioRepository.findById(usuario2);
		if (!usuario22.isPresent()) {
			return ResponseEntity.notFound().build();
		}
			try {
				String usuario1 = u.getIdUsuario();
				Optional<Chat> chatOptional = chatRepository.findByUsuario1AndUsuario2OrUsuario2AndUsuario1(u, usuario22.get());
	            
	            Chat chat;
	            if (chatOptional.isPresent()) {
	                chat = chatOptional.get();
	            } else {
	                // Crea un nuevo chat si no existe
	                chat = Chat.builder()
	                        .usuario1(u)
	                        .usuario2(usuario22.get())
	                        .build();
	                chat = chatRepository.save(chat);
	            }
	            
	            return ResponseEntity.ok(chat.toChatSeguro());
				
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}
	
		@GetMapping("mensajes/{chatId}")
		public ResponseEntity<?> obtenerMensajes(@PathVariable Integer chatId){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Optional<Chat> chat = chatRepository.findById(chatId);
			if(!chat.isPresent()) {
				return ResponseEntity.notFound().build();
			}
			Usuario u = (Usuario) auth.getPrincipal();
			try {
				List<Mensaje>mensajes = mensajeRepository.findByChatOrderByFechaAsc(chat.get());
				List<MensajeSeguro> mensajesSeguros = mensajes.stream()
														.map(Mensaje::toMensajeSeguro)
														.collect(Collectors.toList());
				return ResponseEntity.ok(mensajesSeguros);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}
		@MessageMapping("/{chatId}")
		@SendTo("/broker/{chatId}")
		public MensajeSeguro crearMensaje(@DestinationVariable Integer chatId, MessageDTO body){
			System.out.println("Si llego al endpoint");
			System.out.println("Mensaje recibido en el backend");
		    System.out.println("Chat ID: " + chatId);
		    System.out.println("Contenido del mensaje: " + body.getContenido());
		    System.out.println("Usuario: " + body.getUsuario());
			try {
				System.out.println(body);
			Optional<Chat> chat = chatRepository.findById(chatId);
			Mensaje message = new Mensaje();
			message.setChat(chat.get());
			message.setContenido(body.getContenido());
			message.setUsuario(body.getUsuario());
			message.setFecha(LocalDateTime.now());
			Mensaje m = mensajeRepository.save(message);
			return m.toMensajeSeguro();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error al enviar mensaje");
		}
			
		}
		
	
	}
	
