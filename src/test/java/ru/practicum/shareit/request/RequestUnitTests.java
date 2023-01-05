package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoResponseForRequest;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoForResponse;
import ru.practicum.shareit.request.service.IRequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RequestUnitTests {
    @Mock
    IRequestService requestService;

    @Test
    void testAddRequest() {
        RequestDto requestDto = new RequestDto(1L, "Description");
        Mockito
                .when(requestService.addRequest(Mockito.any(RequestDto.class), Mockito.anyLong()))
                .thenReturn(new RequestDtoForResponse(2L, requestDto.getDescription(),
                        LocalDateTime.now(), new ArrayList<ItemDtoResponseForRequest>()));

        assertThat(requestService.addRequest(requestDto, 1L).getId().equals(2L));
    }

    @Test
    void testGetRequestByIdWithNoRequest() {
        Mockito
                .when(requestService.getRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new RequestNotFoundException(1L));

        assertThrows(RequestNotFoundException.class, () -> requestService.getRequestById(1L, 1L));
    }

}
