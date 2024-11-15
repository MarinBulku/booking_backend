package com.algorhythm.booking_backend.core.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "pdf_tbl")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PdfReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pdf_id", nullable = false)
    private UUID id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private Double fileSize;

    @ManyToOne
    @JoinColumn(name = "fk_booking_id")
    private Booking booking;
}