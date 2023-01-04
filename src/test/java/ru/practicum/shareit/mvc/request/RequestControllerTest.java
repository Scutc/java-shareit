package ru.practicum.shareit.mvc.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.mvc.item.ItemControllerTestConfig;
import ru.practicum.shareit.request.controller.RequestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.IItemService;
import org.springframework.http.MediaType;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoForResponse;
import ru.practicum.shareit.request.service.IRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig({RequestController.class, RequestControllerTestConfig.class,
        ru.practicum.config.WebConfig.class})
public class RequestControllerTest {
    @Mock
    private IRequestService requestService;

    @InjectMocks
    private RequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private RequestDtoForResponse requestDtoForResponse;
    private RequestDto requestDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        requestDto = new RequestDto(1L, "description");
        requestDtoForResponse = new RequestDtoForResponse(1L, "description", LocalDateTime.now(), null);
    }

    @Test
    void createRequestTest() throws Exception {
        when(requestService.addRequest(any(), any()))
                .thenReturn(requestDtoForResponse);

        mvc.perform(post("/requests").header("X-Sharer-User-Id", 1L)
                                     .content(mapper.writeValueAsString(requestDto))
                                     .characterEncoding(StandardCharsets.UTF_8)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(requestDtoForResponse.getId()), Long.class));
    }

    @Test
    void findRequestsByRequestorTest() throws Exception {
        when(requestService.findRequestByRequestorId(any()))
                .thenReturn(List.of(requestDtoForResponse));

        mvc.perform(get("/requests").header("X-Sharer-User-Id", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].id", is(requestDtoForResponse.getId()), Long.class));
    }

    @Test
    void findRequestsWithPaginationTest() throws Exception {
        when(requestService.findAllRequests(any(), any(), any()))
                .thenReturn(List.of(requestDtoForResponse));

        mvc.perform(get("/requests/all").header("X-Sharer-User-Id", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].id", is(requestDtoForResponse.getId()), Long.class));
    }

    @Test
    void getRequestByIdTest() throws Exception {
        when(requestService.getRequestById(any(),any()))
                .thenReturn(requestDtoForResponse);
        mvc.perform(get("/requests/1").header("X-Sharer-User-Id", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(requestDtoForResponse.getId()), Long.class));
    }


}
