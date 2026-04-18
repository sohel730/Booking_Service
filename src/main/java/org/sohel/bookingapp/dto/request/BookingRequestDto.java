package org.sohel.bookingapp.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDto {

    @NotNull(message = "Hotel Id is required")
    private Long hotelId;

    @NotNull(message = "Room Id is required")
    private Long roomId;

    @NotNull(message = "Check In  date is required")
    @FutureOrPresent(message = "Check-in must be today or future")
    private LocalDate checkIn;

    @NotNull(message = "check Out date is required")
    @Future(message = "Check-out must be future date")
    private LocalDate checkOut;




}
