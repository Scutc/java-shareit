package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.CustomSecurityException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.IUserService;
import static ru.practicum.shareit.item.service.ItemMapper.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements IItemService {
    private final ItemRepository itemRepository;
    private final IUserService userService;

    public ItemDto getItemById(Long itemId) {
        log.info("Получение товара с ID = {}", itemId);
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException(itemId);
        }
        return toItemDto(item);
    }

    public List<ItemDto> getAllItems(Long userId) {
        log.info("Получение всех товаров");
        Set<Item> items = itemRepository.getAllByOwnerId(userId);
        return items.stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
    }

    @Transactional
    public ItemDto createItem(Long userId, ItemDto itemDto) throws UserNotFoundException {
        log.info("Создание нового товара {}", itemDto);
        userService.getUserById(userId);
        itemDto.setUserId(userId);
        Item item = itemRepository.save(toItem(itemDto));
        return toItemDto(item);
    }

    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws CustomSecurityException {
        log.info("Обновление товара");
        userService.getUserById(userId);
        Item itemForUpdate = itemRepository.getItemById(itemId);
        if (!itemForUpdate.getOwnerId().equals(userId)) {
            throw new CustomSecurityException("У пользователя отсутствуют права на изменение товара!");
        }
        if (itemDto.getAvailable() != null) {
            itemForUpdate.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getName() != null) {
            itemForUpdate.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemForUpdate.setDescription(itemDto.getDescription());
        }
        itemRepository.save(itemForUpdate);
        return toItemDto(itemForUpdate);
    }

    public List<ItemDto> searchItem(String searchText) {
        log.info("Поиск товара по строке {}", searchText);
        List<Item> items = itemRepository.searchItem(searchText);
        return items.stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
    }
}
