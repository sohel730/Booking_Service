package org.sohel.bookingapp.mapper;

import org.mapstruct.Mapper;
import org.sohel.bookingapp.dto.request.BookingRequestDto;
import org.sohel.bookingapp.dto.response.BookingResponseDto;
import org.sohel.bookingapp.entity.Booking;

@Mapper(componentModel = "Spring")
public interface BookingMapper {

    Booking toEntity(BookingRequestDto bookingRequestDto);
    BookingResponseDto toBookingResponseDto(Booking booking);

}
