package org.sohel.bookingapp.service;

import org.sohel.bookingapp.dto.request.BookingRequestDto;
import org.sohel.bookingapp.dto.response.BookingResponseDto;
import org.sohel.bookingapp.dto.response.CursorPageResponse;

import java.util.List;

public interface BookingService {

    BookingResponseDto getBookingById(Long id);
    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto);
    BookingResponseDto updateBooking(long id, BookingRequestDto bookingRequestDto);
    void deleteBooking(long id);
    CursorPageResponse<BookingResponseDto> cursorPagination(long lastId, int limit);

}
