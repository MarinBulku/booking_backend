package com.algorhythm.booking_backend.dataproviders.entities;

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
    /*
    * RevokedToken Entity
    * Has all the tokens that were used, and then logged out.
    * */
    @Id
    @Column(name = "token")
    private String revokedToken;

    @Column(name = "date_revoked")
    private LocalDateTime timeRevoked;

}
