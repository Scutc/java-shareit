package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.IItemService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ItemUnitTests {
    @Mock
    IItemService itemService;

    @Test
    void testCreateBooking() {
        ItemDto itemDto = new ItemDto(0L, "Item1", "Description",
                true, 1L, null);

        Mockito
                .when(itemService.createItem(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(new ItemDto(1L, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                        itemDto.getUserId(), itemDto.getRequestId()));

        assertThat(itemService.createItem(1L, itemDto).getId().equals(1L));
    }

    @Test
    void testCreateBookingWithWrongUser() {
        ItemDto itemDto = new ItemDto(0L, "Item1", "Description",
                true, 1L, null);
        Mockito
                .when(itemService.createItem(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenThrow(new UserNotFoundException(99L));

        assertThrows(UserNotFoundException.class, () -> itemService.createItem(99L, itemDto));
    }

    @Test
    void testGetItemByIdWithNoItemExisting() {
        Mockito
                .when(itemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new ItemNotFoundException(1L));

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(1L, 1L));
    }
}
