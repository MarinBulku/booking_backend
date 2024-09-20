package com.algorhythm.booking_backend.core.entities;

public enum Status {
    /*
    * Booking status
    * ACTIVE - when the booking is due, or while consuming
    * CANCELLED - when a booking is cancelled, before consuming
    * COMPLETE - After a booking is consumed, it is considered complete
    * */
    ACTIVE,
    CANCELLED,
    COMPLETE
}
