package com.ipn.mx.model.entity;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.ipn.mx.model.dto.MensajeSeguro;
import com.ipn.mx.model.dto.MessageDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Mensaje")
public class Mensaje implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMensaje", nullable = false)
    private Integer idMensaje;

    @ManyToOne
    @JoinColumn(name = "usuarioId", referencedColumnName = "idUsuario", nullable = false,
                foreignKey = @ForeignKey(name = "fk_idUsuarioMS"))
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "chatId", referencedColumnName = "idChat", nullable = false,
                foreignKey = @ForeignKey(name = "fk_idChatMS"))
    private Chat chat;

    @Column(name = "contenido", length = 255, nullable = true)
    private String contenido;

    @Column(name = "fecha", nullable = true, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;
    
    
    public MensajeSeguro toMensajeSeguro() {
    	return new MensajeSeguro(
    			this.idMensaje,
    			this.usuario.toUsuarioSeguroMensaje(),
    			this.chat.getIdChat(),
    			this.contenido,
    			this.fecha
    			);
    }
}
