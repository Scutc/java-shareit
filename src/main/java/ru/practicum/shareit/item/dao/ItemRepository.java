package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item getItemById(Long itemId);

    Set<Item> getAllByOwnerId(Long ownerId);

   // List<Item> searchItem(String searchText);
}
