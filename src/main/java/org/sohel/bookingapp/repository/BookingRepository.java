package org.sohel.bookingapp.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.sohel.bookingapp.dto.response.BookingResponseDto;
import org.sohel.bookingapp.dto.response.CursorPageResponse;
import org.sohel.bookingapp.entity.Booking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Query("""
         SELECT new org.sohel.bookingapp.dto.response.BookingResponseDto(
              b.id,
              b.hotelId,
              b.roomId,
              b.checkIn,
              b.checkOut,
              b.status     
                                                     
               )
         FROM Booking b
         WHERE b.id > :id  
         ORDER BY  b.id ASC      
     """)
    public List<BookingResponseDto> cursorPaginationForBooking(@Param("id") long id, Pageable pageable);
}
