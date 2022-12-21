package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.CustomSecurityException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface IItemService {
    ItemDto getItemById(Long itemId);

    List<ItemDto> getAllItems(Long userId);

    ItemDto createItem(Long userId, ItemDto itemDto) throws UserNotFoundException;

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws CustomSecurityException;

    List<ItemDto> searchItem(String searchText);
}
