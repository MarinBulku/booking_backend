package com.algorhythm.booking_backend.configuration;

import com.algorhythm.booking_backend.repositories.RevokedTokensRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final RevokedTokensRepository revokedTokensRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void deleteRevokedTokens(){
        LocalDateTime time = LocalDateTime.now().minusHours(8);
        revokedTokensRepository.deleteOlderThan(time);
    }

}