package org.sohel.bookingapp.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursorPageResponse<T> {

    private List<T> data;
    private Long  nextCursor;
    private int limit;
    private Boolean hasNext;




}
