package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.CustomSecurityException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoXl;

import java.util.List;

public interface IItemService {
    ItemDtoXl getItemById(Long itemId);

    List<ItemDto> getAllItems(Long userId);

    ItemDto createItem(Long userId, ItemDto itemDto) throws UserNotFoundException;

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws CustomSecurityException;

    List<ItemDto> searchItem(String searchText);
}
