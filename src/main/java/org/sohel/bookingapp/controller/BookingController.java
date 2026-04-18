package org.sohel.bookingapp.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sohel.bookingapp.dto.request.BookingRequestDto;
import org.sohel.bookingapp.dto.response.BookingResponseDto;
import org.sohel.bookingapp.dto.response.CursorPageResponse;
import org.sohel.bookingapp.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;


    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable @Positive long id){

        return ResponseEntity.ok(bookingService.getBookingById(id));
    }


    @PostMapping
    public  ResponseEntity<BookingResponseDto> createBooking(@RequestBody @Valid BookingRequestDto requestDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(requestDto));

    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDto> updateBooking(@PathVariable @Positive Long id,
                                                            @RequestBody @Valid BookingRequestDto requestDto){

        return ResponseEntity.status(HttpStatus.OK).body(bookingService.updateBooking(id,requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable  @Positive  long id){

        bookingService.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }

//    Cursor pagination method is here



    @GetMapping("/cursorPagination")
    public ResponseEntity<CursorPageResponse<BookingResponseDto>> getCursorPage(@RequestParam(value = "lastId",defaultValue = "0") Long lastId,
                                                                                @RequestParam(value = "limit",defaultValue = "3") int limit)
    {
         return ResponseEntity.ok(bookingService.cursorPagination(lastId,limit));
    }



}

