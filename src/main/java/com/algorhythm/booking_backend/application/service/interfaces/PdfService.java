package com.algorhythm.booking_backend.application.service.interfaces;

import java.io.IOException;

public interface PdfService {

    byte[] getRecipeOfBooking(Integer bookingId) throws IOException;
}
