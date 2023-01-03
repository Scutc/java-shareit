package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoResponseForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
public class RequestDtoForResponse {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDtoResponseForRequest> items;

    public void addItem(Item item) {
        if (item != null) {
            items.add(ItemMapper.toItemDtoResponseForRequest(item));
        }
    }
}