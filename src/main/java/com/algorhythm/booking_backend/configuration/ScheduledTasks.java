package com.algorhythm.booking_backend.configuration;

import com.algorhythm.booking_backend.repositories.BookingRepository;
import com.algorhythm.booking_backend.repositories.RevokedTokensRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final RevokedTokensRepository revokedTokensRepository;
    private final BookingRepository bookingRepository;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void deleteRevokedTokens(){
        LocalDateTime time = LocalDateTime.now().minusHours(8);
        revokedTokensRepository.deleteOlderThan(time);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateBookingStatus(){
        bookingRepository.updateCompletedBookings(LocalDate.now());
    }

}
