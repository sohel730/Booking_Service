package org.sohel.bookingapp.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sohel.bookingapp.configuration.L1CacheService;
import org.sohel.bookingapp.configuration.L2CacheService;
import org.sohel.bookingapp.dto.request.BookingRequestDto;
import org.sohel.bookingapp.dto.response.BookingResponseDto;
import org.sohel.bookingapp.dto.response.CursorPageResponse;
import org.sohel.bookingapp.entity.Booking;
import org.sohel.bookingapp.exception.custoomexception.ResourceNotFoundException;
import org.sohel.bookingapp.mapper.BookingMapper;
import org.sohel.bookingapp.repository.BookingRepository;
import org.sohel.bookingapp.service.BookingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final L1CacheService l1Cache;
    private final L2CacheService l2Cache;
    private  static final String PREFIX ="Booking";


//    Get data by id

    @Override
    public BookingResponseDto getBookingById(Long id) {

        log.info(" process started to return data with id: {} ",id);
        String key= PREFIX +id;

     BookingResponseDto l1Value=l1Cache.get(key,BookingResponseDto.class);

//     Returning response form l1cache

      if(l1Value!=null)
      {
          log.info("Data returned successfully form l1Cache with id: {}",id);
          return  l1Value;

      }

//        Returning response form l2Cache

        BookingResponseDto l2Value=l2Cache.get(key,BookingResponseDto.class);

      if(l2Value!=null){

          log.info("Data returned successfully form l2Cache with id: {}",id);
          return l2Value;
      }

//         Returning response form the Db

        Booking dbExist=bookingRepository.findById(id).orElseThrow(()->{

            log.warn("Data not found with id: {}",id);
            return new ResourceNotFoundException("Data not found with id: "+id);
        });

     BookingResponseDto dtoExist=bookingMapper.toBookingResponseDto(dbExist);

     log.info("Setting Db response in l1Cache and l2Cache");
     l1Cache.put(key,dbExist);
     l2Cache.put(key,dbExist, Duration.ofMinutes(10));


     log.info("Data returned successfully from db with id: {}",id);

        return dtoExist;
    }

//    create Resource in Db

    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {

        log.info("Create booking API initiated");


        Booking incomingObj=bookingMapper.toEntity(bookingRequestDto);

        incomingObj.setStatus("CONFIRMED");
        Booking saved=bookingRepository.save(incomingObj);

        log.info("Booking saved in DB with id: {}",saved.getId());

        String key=PREFIX+saved.getId();

        BookingResponseDto responseDto=bookingMapper.toBookingResponseDto(saved);

        log.info("Booking cached for create in L1 and L2 with key: {}", key);

        l2Cache.put(key,responseDto,Duration.ofMinutes(10));
        l1Cache.put(key,responseDto);


        return responseDto;

    }

//    Update Booking

    @Override
    public BookingResponseDto updateBooking(long id, BookingRequestDto bookingRequestDto) {

        log.info("Update booking api initiated");

        Booking incomingObj=bookingMapper.toEntity(bookingRequestDto);

        Booking existObj=bookingRepository.findById(id).orElseThrow(()->{

            log.warn("Booking not found with id: {}",id);
            return new ResourceNotFoundException("Booking not found with id: "+id);
        });

        existObj.setHotelId(incomingObj.getHotelId());
        existObj.setRoomId(incomingObj.getRoomId());
        existObj.setCheckIn(incomingObj.getCheckIn());
        existObj.setCheckOut(incomingObj.getCheckOut());

        Booking saved=bookingRepository.save(existObj);
        log.info("Booking updated successfully with id: {}",saved.getId());

        BookingResponseDto responseDto=bookingMapper.toBookingResponseDto(saved);

        String key=PREFIX+saved.getId();

        log.info("Booking cached  for update in L1 and L2 with key: {}", key);
        l2Cache.put(key, responseDto,Duration.ofMinutes(10));
        l1Cache.put(key,responseDto );


        return responseDto;
    }

//    Delete Booking

    @Override
    public void deleteBooking(long id) {

        log.info("Delete booking api initiated");

        Booking existed=bookingRepository.findById(id).orElseThrow(()->{

            log.warn("Booking  is not found with id: {}", id);
            return new ResourceNotFoundException("Booking not found with id: "+id);

        });

        String key=PREFIX+existed.getId();
        bookingRepository.delete(existed);
        log.info("Deleted booking Records form Db successfully with id: {}",id);

        log.info("Cache evicted from L1 and L2 for key: {}", key);
        l1Cache.evict(key);
        l2Cache.evict(key);

    }

//    Get all booking via cursor pagination

    @Override
    public CursorPageResponse<BookingResponseDto> cursorPagination(long lastId, int limit) {

        String key=PREFIX+"_"+lastId+"_"+limit;

        log.info("Process started to return data via cursor pagination");

      CursorPageResponse<BookingResponseDto> l1value =l1Cache.getCursorPage(key);

//      Returning response from the l1Cache

      if(l1value!=null){

          return l1value;

      }

//      Returning response for the l2Cache

        CursorPageResponse<BookingResponseDto> l2value=l2Cache.getCursorPageFromL2(key);

      if(l2value!=null){

          return l2value;
      }


        Pageable pageable= PageRequest.of(0,limit+1);

         List<BookingResponseDto> o=bookingRepository.cursorPaginationForBooking(lastId,pageable);

         boolean hasNext=o.size()>limit;

        if (hasNext) {
            o = o.subList(0, limit);
        }
         Long nextCursor=o.isEmpty()? null:o.get(o.size()-1).getId();

        CursorPageResponse<BookingResponseDto> response=new CursorPageResponse<>(o,nextCursor,limit,hasNext);

         log.info("Putting Db response in l1Cache and l2Cache for key: {}",key);

         l1Cache.put(key,response);
         l2Cache.put(key,response,Duration.ofMinutes(10));

         log.info("Response returned form Db successfully");

         return response;



    }


}
