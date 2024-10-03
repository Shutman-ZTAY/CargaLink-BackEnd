package com.ipn.mx.model.entity;

import java.io.Serializable;

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
@Table(name = "Chat")
public class Chat implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idChat", nullable = false)
    private Integer idChat;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "usuario1Id", referencedColumnName = "idUsuario", nullable = false,
                foreignKey = @ForeignKey(name = "fk_idUsuarioC1"))
    private Usuario usuario1;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "usuario2Id", referencedColumnName = "idUsuario", nullable = false,
                foreignKey = @ForeignKey(name = "fk_idUsuarioC2"))
    private Usuario usuario2;
}
