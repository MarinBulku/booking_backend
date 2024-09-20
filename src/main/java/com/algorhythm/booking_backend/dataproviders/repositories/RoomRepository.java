package com.algorhythm.booking_backend.dataproviders.repositories;

import com.algorhythm.booking_backend.core.entities.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    /*
    * Room repository, to perform simple CRUD operations on
    * */

    /*
    * All three methods below have the same purpose
    *
    * Based on the requirements specified, finds the rooms' info that is needed for booking
    * It returns for each room the id, name, description, image path, all booking dates, all daily costs,
    * total cost and also any discount if it is applied.
    *
    * The difference is that findAvailableRooms2 gets these results in unsorted total cost order,
    * findAvailableRoomsASC gets the results in ascending total cost order,
    * findAvailableRoomsDESC gets the results in descending total cost order
    * */

    @Query(value = "WITH DateSeries AS ( " +
            "    SELECT generate_series(CAST(:startDate AS DATE), CAST(:endDate AS DATE), '1 day'::INTERVAL) AS booking_date " +
            "), " +
            "     AvailableRooms AS ( " +
            "         SELECT " +
            "             r.room_id, " +
            "             r.room_name, " +
            "             r.description, " +
            "             r.price, " +
            "             r.capacity_kids, " +
            "             r.capacity_adults, " +
            "             r.image_path, " +
            "             ds.booking_date " +
            "         FROM " +
            "             room_tbl r " +
            "                 CROSS JOIN " +
            "             DateSeries ds " +
            "         WHERE " +
            "                 r.fk_hotel_id = :hotelId " +
            "           AND r.capacity_kids = :kidsCapacity " +
            "           AND r.capacity_adults = :adultsCapacity " +
            "           AND NOT EXISTS ( " +
            "             SELECT 1 " +
            "             FROM booking_tbl b " +
            "             WHERE b.fk_room_id = r.room_id " +
            "               AND ds.booking_date BETWEEN b.start_date AND b.end_date - INTERVAL '1 day' " +
            "         ) " +
            "     ), " +
            "     CompleteRooms AS ( " +
            "         SELECT " +
            "             ar.room_id, " +
            "             COUNT(DISTINCT ar.booking_date) AS available_days " +
            "         FROM " +
            "             AvailableRooms ar " +
            "         GROUP BY " +
            "             ar.room_id " +
            "         HAVING " +
            "                 COUNT(DISTINCT ar.booking_date) = (SELECT COUNT(*) FROM DateSeries) " +
            "     ), " +
            "     FilteredAvailableRooms AS ( " +
            "         SELECT " +
            "             ar.* " +
            "         FROM " +
            "             AvailableRooms ar " +
            "                 JOIN " +
            "             CompleteRooms cr ON ar.room_id = cr.room_id " +
            "     ), " +
            "     RoomPriceAdjustments AS ( " +
            "         SELECT " +
            "             ar.room_id, " +
            "             ar.room_name, " +
            "             ar.description, " +
            "             ar.image_path, " +
            "             ar.booking_date, " +
            "             ar.price, " +
            "             COALESCE(dd.discount, 0) AS discount, " +
            "             CASE " +
            "                 WHEN dd.discount IS NOT NULL THEN ar.price * dd.discount " +
            "                 ELSE CASE " +
            "                          WHEN EXTRACT(DOW FROM ar.booking_date) IN (0, 6) THEN ar.price * 1.1 " +
            "                          WHEN EXTRACT(DOW FROM ar.booking_date) IN (1, 2) THEN ar.price * 0.9 " +
            "                          ELSE ar.price " +
            "                     END " +
            "                 END AS daily_cost " +
            "         FROM " +
            "             FilteredAvailableRooms ar " +
            "                 LEFT JOIN discount_date_tbl dd ON ar.room_id = dd.fk_room_id AND ar.booking_date = dd.discount_date " +
            "         WHERE NOT EXISTS ( " +
            "             SELECT 1 " +
            "             FROM booking_tbl b " +
            "             WHERE b.fk_room_id = ar.room_id " +
            "               AND ar.booking_date BETWEEN b.start_date AND b.end_date - INTERVAL '1 day' " +
            "         ) " +
            "     ), " +
            "     RoomTotalCost AS ( " +
            "         SELECT " +
            "             room_id, " +
            "             room_name, " +
            "             description, " +
            "             ROUND(SUM(daily_cost)::numeric, 2) AS total_cost" +
            "         FROM " +
            "             RoomPriceAdjustments " +
            "         GROUP BY " +
            "             room_id, room_name, description " +
            "     ), " +
            "     UserMaxDiscount AS ( " +
            "         SELECT " +
            "             GREATEST(MAX(pt.discount), 1.0) AS max_discount " +
            "         FROM " +
            "             point_tbl pt " +
            "         WHERE " +
            "                 pt.points_required <= :userPoints " +
            "     ) " +
            "SELECT " +
            "    rpa.room_id," +
            "    rpa.room_name," +
            "    rpa.description," +
            "    rpa.image_path," +
            "    rpa.booking_date," +
            "    rpa.daily_cost, " +
            "    rtc.total_cost * COALESCE((SELECT max_discount FROM UserMaxDiscount), 1.0) AS total_cost, " +
            "    COALESCE((SELECT max_discount FROM UserMaxDiscount), 1.0) AS max_discount_applied " +
            "FROM " +
            "    RoomPriceAdjustments rpa " +
            "JOIN " +
            "    RoomTotalCost rtc ON rpa.room_id = rtc.room_id " +
            "ORDER BY rpa.room_id, booking_date",
            nativeQuery = true)
    List<Object[]> findAvailableRooms2(@Param("hotelId") Integer hotelId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("adultsCapacity") Integer adultsCapacity,
                                       @Param("kidsCapacity") Integer kidsCapacity,
                                       @Param("userPoints") Integer userPoints,
                                       Pageable pageable);

    @Query(value = "WITH DateSeries AS ( " +
            "    SELECT generate_series(CAST(:startDate AS DATE), CAST(:endDate AS DATE), '1 day'::INTERVAL) AS booking_date " +
            "), " +
            "     AvailableRooms AS ( " +
            "         SELECT " +
            "             r.room_id, " +
            "             r.room_name, " +
            "             r.description, " +
            "             r.price, " +
            "             r.capacity_kids, " +
            "             r.capacity_adults, " +
            "             r.image_path, " +
            "             ds.booking_date " +
            "         FROM " +
            "             room_tbl r " +
            "                 CROSS JOIN " +
            "             DateSeries ds " +
            "         WHERE " +
            "                 r.fk_hotel_id = :hotelId " +
            "           AND r.capacity_kids = :kidsCapacity " +
            "           AND r.capacity_adults = :adultsCapacity " +
            "           AND NOT EXISTS ( " +
            "             SELECT 1 " +
            "             FROM booking_tbl b " +
            "             WHERE b.fk_room_id = r.room_id " +
            "               AND ds.booking_date BETWEEN b.start_date AND b.end_date - INTERVAL '1 day' " +
            "         ) " +
            "     ), " +
            "     CompleteRooms AS ( " +
            "         SELECT " +
            "             ar.room_id, " +
            "             COUNT(DISTINCT ar.booking_date) AS available_days " +
            "         FROM " +
            "             AvailableRooms ar " +
            "         GROUP BY " +
            "             ar.room_id " +
            "         HAVING " +
            "                 COUNT(DISTINCT ar.booking_date) = (SELECT COUNT(*) FROM DateSeries) " +
            "     ), " +
            "     FilteredAvailableRooms AS ( " +
            "         SELECT " +
            "             ar.* " +
            "         FROM " +
            "             AvailableRooms ar " +
            "                 JOIN " +
            "             CompleteRooms cr ON ar.room_id = cr.room_id " +
            "     ), " +
            "     RoomPriceAdjustments AS ( " +
            "         SELECT " +
            "             ar.room_id, " +
            "             ar.room_name, " +
            "             ar.description, " +
            "             ar.image_path, " +
            "             ar.booking_date, " +
            "             ar.price, " +
            "             COALESCE(dd.discount, 0) AS discount, " +
            "             CASE " +
            "                 WHEN dd.discount IS NOT NULL THEN ar.price * dd.discount " +
            "                 ELSE CASE " +
            "                          WHEN EXTRACT(DOW FROM ar.booking_date) IN (0, 6) THEN ar.price * 1.1 " +
            "                          WHEN EXTRACT(DOW FROM ar.booking_date) IN (1, 2) THEN ar.price * 0.9 " +
            "                          ELSE ar.price " +
            "                     END " +
            "                 END AS daily_cost " +
            "         FROM " +
            "             FilteredAvailableRooms ar " +
            "                 LEFT JOIN discount_date_tbl dd ON ar.room_id = dd.fk_room_id AND ar.booking_date = dd.discount_date " +
            "         WHERE NOT EXISTS ( " +
            "             SELECT 1 " +
            "             FROM booking_tbl b " +
            "             WHERE b.fk_room_id = ar.room_id " +
            "               AND ar.booking_date BETWEEN b.start_date AND b.end_date - INTERVAL '1 day' " +
            "         ) " +
            "     ), " +
            "     RoomTotalCost AS ( " +
            "         SELECT " +
            "             room_id, " +
            "             room_name, " +
            "             description, " +
            "             ROUND(SUM(daily_cost)::numeric, 2) AS total_cost " +
            "         FROM " +
            "             RoomPriceAdjustments " +
            "         GROUP BY " +
            "             room_id, room_name, description " +
            "     ), " +
            "     UserMaxDiscount AS ( " +
            "         SELECT " +
            "             GREATEST(MAX(pt.discount), 1.0) AS max_discount " +
            "         FROM " +
            "             point_tbl pt " +
            "         WHERE " +
            "                 pt.points_required <= :userPoints " +
            "     ) " +
            "SELECT " +
            "    rpa.room_id," +
            "    rpa.room_name," +
            "    rpa.description," +
            "    rpa.image_path," +
            "    rpa.booking_date," +
            "    rpa.daily_cost, " +
            "    rtc.total_cost * COALESCE((SELECT max_discount FROM UserMaxDiscount), 1.0) AS total_cost, " +
            "    COALESCE((SELECT max_discount FROM UserMaxDiscount), 1.0) AS max_discount_applied " +
            "FROM " +
            "    RoomPriceAdjustments rpa " +
            "JOIN " +
            "    RoomTotalCost rtc ON rpa.room_id = rtc.room_id " +
            "ORDER BY total_cost, rpa.booking_date",
            nativeQuery = true)
    List<Object[]> findAvailableRoomsASC(@Param("hotelId") Integer hotelId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("adultsCapacity") Integer adultsCapacity,
                                       @Param("kidsCapacity") Integer kidsCapacity,
                                       @Param("userPoints") Integer userPoints,
                                       Pageable pageable);

    @Query(value = "WITH DateSeries AS ( " +
            "    SELECT generate_series(CAST(:startDate AS DATE), CAST(:endDate AS DATE), '1 day'::INTERVAL) AS booking_date " +
            "), " +
            "     AvailableRooms AS ( " +
            "         SELECT " +
            "             r.room_id, " +
            "             r.room_name, " +
            "             r.description, " +
            "             r.price, " +
            "             r.capacity_kids, " +
            "             r.capacity_adults, " +
            "             r.image_path, " +
            "             ds.booking_date " +
            "         FROM " +
            "             room_tbl r " +
            "                 CROSS JOIN " +
            "             DateSeries ds " +
            "         WHERE " +
            "                 r.fk_hotel_id = :hotelId " +
            "           AND r.capacity_kids = :kidsCapacity " +
            "           AND r.capacity_adults = :adultsCapacity " +
            "           AND NOT EXISTS ( " +
            "             SELECT 1 " +
            "             FROM booking_tbl b " +
            "             WHERE b.fk_room_id = r.room_id " +
            "               AND ds.booking_date BETWEEN b.start_date AND b.end_date - INTERVAL '1 day' " +
            "         ) " +
            "     ), " +
            "     CompleteRooms AS ( " +
            "         SELECT " +
            "             ar.room_id, " +
            "             COUNT(DISTINCT ar.booking_date) AS available_days " +
            "         FROM " +
            "             AvailableRooms ar " +
            "         GROUP BY " +
            "             ar.room_id " +
            "         HAVING " +
            "                 COUNT(DISTINCT ar.booking_date) = (SELECT COUNT(*) FROM DateSeries) " +
            "     ), " +
            "     FilteredAvailableRooms AS ( " +
            "         SELECT " +
            "             ar.* " +
            "         FROM " +
            "             AvailableRooms ar " +
            "                 JOIN " +
            "             CompleteRooms cr ON ar.room_id = cr.room_id " +
            "     ), " +
            "     RoomPriceAdjustments AS ( " +
            "         SELECT " +
            "             ar.room_id, " +
            "             ar.room_name, " +
            "             ar.description, " +
            "             ar.image_path, " +
            "             ar.booking_date, " +
            "             ar.price, " +
            "             COALESCE(dd.discount, 0) AS discount, " +
            "             CASE " +
            "                 WHEN dd.discount IS NOT NULL THEN ar.price * dd.discount " +
            "                 ELSE CASE " +
            "                          WHEN EXTRACT(DOW FROM ar.booking_date) IN (0, 6) THEN ar.price * 1.1 " +
            "                          WHEN EXTRACT(DOW FROM ar.booking_date) IN (1, 2) THEN ar.price * 0.9 " +
            "                          ELSE ar.price " +
            "                     END " +
            "                 END AS daily_cost " +
            "         FROM " +
            "             FilteredAvailableRooms ar " +
            "                 LEFT JOIN discount_date_tbl dd ON ar.room_id = dd.fk_room_id AND ar.booking_date = dd.discount_date " +
            "         WHERE NOT EXISTS ( " +
            "             SELECT 1 " +
            "             FROM booking_tbl b " +
            "             WHERE b.fk_room_id = ar.room_id " +
            "               AND ar.booking_date BETWEEN b.start_date AND b.end_date - INTERVAL '1 day' " +
            "         ) " +
            "     ), " +
            "     RoomTotalCost AS ( " +
            "         SELECT " +
            "             room_id, " +
            "             room_name, " +
            "             description, " +
            "             ROUND(SUM(daily_cost)::numeric, 2) AS total_cost " +
            "         FROM " +
            "             RoomPriceAdjustments " +
            "         GROUP BY " +
            "             room_id, room_name, description " +
            "     ), " +
            "     UserMaxDiscount AS ( " +
            "         SELECT " +
            "             GREATEST(MAX(pt.discount), 1.0) AS max_discount " +
            "         FROM " +
            "             point_tbl pt " +
            "         WHERE " +
            "                 pt.points_required <= :userPoints " +
            "     ) " +
            "SELECT " +
            "    rpa.room_id," +
            "    rpa.room_name," +
            "    rpa.description," +
            "    rpa.image_path," +
            "    rpa.booking_date," +
            "    rpa.daily_cost, " +
            "    rtc.total_cost * COALESCE((SELECT max_discount FROM UserMaxDiscount), 1.0) AS total_cost, " +
            "    COALESCE((SELECT max_discount FROM UserMaxDiscount), 1.0) AS max_discount_applied " +
            "FROM " +
            "    RoomPriceAdjustments rpa " +
            "JOIN " +
            "    RoomTotalCost rtc ON rpa.room_id = rtc.room_id " +
            "ORDER BY total_cost DESC, rpa.booking_date",
            nativeQuery = true)
    List<Object[]> findAvailableRoomsDESC(@Param("hotelId") Integer hotelId,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate,
                                         @Param("adultsCapacity") Integer adultsCapacity,
                                         @Param("kidsCapacity") Integer kidsCapacity,
                                         @Param("userPoints") Integer userPoints,
                                         Pageable pageable);

}
