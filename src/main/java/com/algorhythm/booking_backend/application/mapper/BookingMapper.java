package com.algorhythm.booking_backend.application.mapper;

import com.algorhythm.booking_backend.adapter.in.models.booking.BookingDto;
import com.algorhythm.booking_backend.adapter.in.models.booking.BookingHistoryDto;
import com.algorhythm.booking_backend.core.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BookingMapper {

    BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "hotelName", source = "room.hotel.hotelName")
    @Mapping(target = "bookingPrice", source = "pricePaid")
    BookingHistoryDto toBookingHistoryDto(Booking booking);

    List<BookingHistoryDto> toBookingHistoryDtoList(List<Booking> bookings);

    @Mapping(target = "roomId", source = "room.roomId")
    @Mapping(target = "userId", source = "user.userId")
    BookingDto toBookingDto(Booking booking);

    List<BookingDto> toBookingDtoList(List<Booking> bookings);
}
