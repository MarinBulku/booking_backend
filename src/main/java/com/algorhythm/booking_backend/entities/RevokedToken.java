package com.algorhythm.booking_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "revoked_tokens_tbl")
@Builder
@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RevokedToken {

    @Id
    @Column(name = "token")
    private String revokedToken;

    @Column(name = "date_revoked")
    private LocalDateTime timeRevoked;

}
