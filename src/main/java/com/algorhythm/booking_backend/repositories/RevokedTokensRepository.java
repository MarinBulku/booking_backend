package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokensRepository extends JpaRepository<RevokedToken, String> {
}
