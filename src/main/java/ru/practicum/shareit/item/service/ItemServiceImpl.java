package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.CustomSecurityException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.service.IUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements IItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
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
        return null;
/*        Set<Item> items = itemRepository.getItemsByUserWithin(userId);
        return items.stream()
                    .map(this::toItemDto)
                    .collect(Collectors.toList());*/
    }

    public ItemDto createItem(Long userId, ItemDto itemDto) throws UserNotFoundException {
        log.info("Создание нового товара {}", itemDto);
        userService.getUserById(userId);
        Item item = itemRepository.save(toItem(itemDto));
        return toItemDto(item);
    }

    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws CustomSecurityException {
        log.info("Обновление товара");
        userService.getUserById(userId);
        if (!itemRepository.getItemById(itemId).getOwnerId().equals(userId)) {
            throw new CustomSecurityException("У пользователя отсутствуют права на изменение товара!");
        }
        Item itemForUpdate = itemRepository.save(toItem(itemDto));
        return null;
    }

    public List<ItemDto> searchItem(String searchText) {
        log.info("Поиск товара по строке {}", searchText);
  /*      List<Item> items = itemRepository.searchItem(searchText);
        return items.stream()
                    .map(this::toItemDto)
                    .collect(Collectors.toList());*/

        return null;
    }

    private ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId()
        );
    }

    private Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getUserId());
    }
}
