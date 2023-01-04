package ru.practicum.shareit.mvc.item;


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

@SpringJUnitWebConfig({ItemController.class, ItemControllerTestConfig.class,
        ru.practicum.config.WebConfig.class})
public class ItemControllerTest {
    @Mock
    private IItemService itemService;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemDto itemDto;
    private ItemDtoResponse itemDtoResponse;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemDto = new ItemDto(1L, "Item1", "Nice Item", true, 1L, null);
        itemDtoResponse = new ItemDtoResponse(1L, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                null, null, null, 1L);
    }

    @Test
    void getItemByIdTest() throws Exception {
        when(itemService.getItemById(any(), any()))
                .thenReturn(itemDtoResponse);

        mvc.perform(get("/items/1").header("X-Sharer-User-Id", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(itemDtoResponse.getId()), Long.class));
    }

    @Test
    void findAllItemsTest() throws Exception {
        when(itemService.findAllItems(any()))
                .thenReturn(List.of(itemDtoResponse));

        mvc.perform(get("/items").header("X-Sharer-User-Id", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].id", is(itemDtoResponse.getId()), Long.class));
    }

    @Test
    void createItemTest() throws Exception {
        when(itemService.createItem(any(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                   .header("X-Sharer-User-Id", 1L)
                   .content(mapper.writeValueAsString(itemDto))
                   .characterEncoding(StandardCharsets.UTF_8)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class));
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.updateItem(any(), any(), any()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                   .header("X-Sharer-User-Id", 1L)
                   .content(mapper.writeValueAsString(itemDto))
                   .characterEncoding(StandardCharsets.UTF_8)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class));
    }

    @Test
    void searchItemTest() throws Exception {
        when(itemService.searchItem(any()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search").param("text", "ddd"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].id", is(itemDtoResponse.getId()), Long.class));
    }

    @Test
    void addCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "text", "name", LocalDateTime.now());
        when(itemService.addComment(any(), any(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                   .header("X-Sharer-User-Id", 1L)
                   .content(mapper.writeValueAsString(commentDto))
                   .characterEncoding(StandardCharsets.UTF_8)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class));
    }
}
