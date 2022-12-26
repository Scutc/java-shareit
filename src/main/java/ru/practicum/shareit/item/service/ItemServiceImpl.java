package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.IBookingService;
import ru.practicum.shareit.exception.CustomSecurityException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.NotAllowedToChangeException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoXl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.user.service.UserMapper;

import static ru.practicum.shareit.item.service.ItemMapper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements IItemService {
    private final ItemRepository itemRepository;
    private final IUserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDtoXl getItemById(Long itemId, Long ownerId) {
        log.info("Получение товара с ID = {}", itemId);
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException(itemId);
        }
        List<Booking> bookings = bookingRepository.getBookingByItemAndOwner(itemId, ownerId);
        BookingDto lastBooking = BookingMapper.toBookingDto(bookings.stream().findFirst().orElse(null));
        BookingDto nextBooking = BookingMapper.toBookingDto(bookings.stream().reduce((first, last) -> last)
                                                                    .orElse(null));
        List<Comment> comments = commentRepository.findCommentsById(itemId);
        return toItemDtoXl(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemDtoXl> getAllItems(Long ownerId) {
        log.info("Получение всех товаров");
        List<Item> items = itemRepository.getAllByOwnerIdOrderByIdAsc(ownerId);
        List<ItemDtoXl> result = new ArrayList<>();
        for (Item item : items) {
            List<Booking> bookings = bookingRepository.getBookingByItemAndOwner(item.getId(), ownerId);
            BookingDto lastBooking = BookingMapper.toBookingDto(bookings.stream().findFirst().orElse(null));
            BookingDto nextBooking = BookingMapper.toBookingDto(bookings.stream().reduce((first, last) -> last)
                                                                        .orElse(null));
            List<Comment> comments = commentRepository.findCommentsById(item.getId());
            result.add(toItemDtoXl(item, lastBooking, nextBooking, comments));
        }
        return result;
    }

    @Transactional
    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) throws UserNotFoundException {
        log.info("Создание нового товара {}", itemDto);
        userService.getUserById(userId);
        itemDto.setUserId(userId);
        Item item = itemRepository.save(toItem(itemDto));
        return toItemDto(item);
    }

    @Transactional
    @Override
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

    @Override
    public List<ItemDto> searchItem(String searchText) {
        log.info("Поиск товара по строке {}", searchText);
        List<Item> items = itemRepository.searchItem(searchText);
        return items.stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto addComment(CommentDto commentDto, Long userId, Long itemId) {
        log.info("Создание нового комментария для вещи {} от пользователя {}", userId, itemId);
        if (checkIfBookedItem(itemId, userId)) {
            Comment comment = CommentMapper.toComment(commentDto);
            User user = UserMapper.toUser(userService.getUserById(userId));
            Item item = itemRepository.getItemById(itemId);
            comment.setAuthor(user);
            comment.setItem(item);
            comment = commentRepository.save(comment);
            return CommentMapper.toCommentDto(comment);
        } else {
            throw new NotAllowedToChangeException("Пользователь не брал вещь в аренду!");
        }
    }

    private boolean checkIfBookedItem(Long itemId, Long userId) {
        List<Booking> bookings = bookingRepository.getBookingsByItemAndBooker(userId, itemId);
        return bookings.size() != 0;
    }
}
