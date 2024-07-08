package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.Hotel;
import com.algorhythm.booking_backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    /*
    * findHotelsByOwner(User owner)
    * owner - The owner of the business(hotel)
    *
    * Returns a list of hotels registered by the owner
    * */
    List<Hotel> findHotelsByOwner(User owner);

    /*
    * findAvailableHotels( LocalDate checkInDate, LocalDate checkOutDate, Integer adultsNumber, Integer kidsNumber, Pageable pageable)
    * checkInDate, checkOutDate - Date range to search for available hotels
    * adultsNumber, kidsNumber - Other specifications of request, that the hotels must have
    * pageable - For pagination of results
    *
    * The query searches for hotels that have rooms that meet the specs for kids and adults number
    * Also the rooms must have not been booked between(including) dates specified
    *
    * If a hotel is found which meets the room requirements,
    * it is returned along with the number of free rooms that it does have
    *
    * The results are paginated
    * */
    @Query("select h, count(r) as freeRooms " +
            "from Hotel h " +
            "inner join Room r on r.hotel = h " +
            "where r.adultsCapacity = :adultsNumber and r.kidsCapacity = :kidsNumber " +
            "and not exists (" +
            "   select b from Booking b " +
            "   where b.room = r " +
            "   and (" +
            "       (b.startDate >= :checkInDate and :checkInDate < b.endDate) or " +
            "       (b.startDate < :checkOutDate and :checkOutDate <= b.endDate) or " +
            "       (:checkInDate <= b.startDate and b.startDate < :checkOutDate)" +
            "   )" +
            ")" +
            "group by h")
    Page<Object[]> findAvailableHotels(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("adultsNumber") Integer adultsNumber,
            @Param("kidsNumber") Integer kidsNumber,
            Pageable pageable);
}
