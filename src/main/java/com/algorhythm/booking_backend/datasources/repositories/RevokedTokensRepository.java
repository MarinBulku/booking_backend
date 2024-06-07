package com.algorhythm.booking_backend.datasources.repositories;

import com.algorhythm.booking_backend.core.entities.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface RevokedTokensRepository extends JpaRepository<RevokedToken, String> {

    /*
    * deleteOlderThan(LocalDateTime time)
    * time - time specified
    *
    * Deletes all the tokens that are older than the specified time
    * */
    @Modifying
    @Query("DELETE FROM RevokedToken t WHERE t.timeRevoked <= :time")
    void deleteOlderThan(LocalDateTime time);
}
