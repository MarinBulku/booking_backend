package com.algorhythm.booking_backend.adapter.out.persistence;

import com.algorhythm.booking_backend.core.entities.PdfReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PdfReceiptRepository extends JpaRepository<PdfReceipt, UUID> {


    Optional<PdfReceipt> findByFileName(String fileName);
}