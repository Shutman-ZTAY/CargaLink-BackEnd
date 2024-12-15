package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
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
@Table(name = "PasswordResetToken")
public class PasswordResetToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@OneToOne
    @JoinColumn(name = "usuarioId", referencedColumnName = "idUsuario", nullable = false,
                foreignKey = @ForeignKey(name = "fk_idUsuarioPRT"))
	private Usuario usuario;
	
	@Column(name = "token", nullable = false, length = 100)
	private String token;
	
	@Column(name = "expiration", nullable = false)
	private LocalDateTime expiration;
	
	@PrePersist
    public void prePersist() {
    	this.expiration = LocalDateTime.now().plusHours(1);
    	this.token = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
    }
	
}
