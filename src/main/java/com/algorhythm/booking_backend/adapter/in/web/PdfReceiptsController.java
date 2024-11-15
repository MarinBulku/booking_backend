package com.algorhythm.booking_backend.adapter.in.web;

import com.algorhythm.booking_backend.application.service.interfaces.PdfService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/docs")
@RequiredArgsConstructor
@Tag(
        name = "Documents",
        description = "All APIs of documents"
)
public class PdfReceiptsController {

    public final PdfService docService;

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<byte[]> getDocument(@RequestParam Integer bookingId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        byte[] docData = docService.getRecipeOfBooking(bookingId);

        headers.add("Content-Type", "application/pdf");
        headers.add("Content-Disposition", "inline; filename=Booking_" + bookingId + ".pdf");

        return new ResponseEntity<>(docData, headers, HttpStatus.OK);
    }

}
