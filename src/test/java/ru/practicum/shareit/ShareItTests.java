package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class ShareItTests {
    private final UserDao userDao;
    private final ItemDao itemDao;
    private User user;
    private Long userId;
    private Item item;
    private Long itemId;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    public void before() {
        user = new User(0L, "User1", "user1@gmail.com");
        userId = userDao.createUser(user).getId();
        item = new Item(0L, "Item Vacuum", "Description vacuum", Boolean.TRUE, 1L);
        itemId = itemDao.createItem(item).getId();
    }

    @AfterEach
    public void after() {
        userDao.deleteUser(userId);
        itemDao.getAllItems(itemId).remove(itemId);
    }

    @Test
    public void testCreateAndGetUser() {
        assertThat(userDao.getUserById(userId).getName().equals("User1"));
    }

    @Test
    public void testUpdateUser() {
        User user2 = new User(0L, "User1", "update@gmail.com");
        userDao.updateUser(userId, user2);
        assertThat(userDao.getUserById(userId).getEmail().equals("update@gmail.com"));
    }

    @Test
    public void testGetAllUsers() {
        assertThat(userDao.getAllUsers().size() == 1);
    }

    @Test
    public void testDeleteUser() {
        userDao.deleteUser(userId);
        assertThat(userDao.getAllUsers().size() == 0);
    }

    @Test
    public void testItemCreateAndGet() {
        assertThat(itemDao.getItemById(userId).getName().equals("Item Vacuum"));
    }

    @Test
    public void testGetAllItems() {
        assertThat(itemDao.getAllItems(userId).size() == 1);
    }

    @Test
    public void testUpdateItem() {
        Item item2 = new Item(0L, "Item Update", "Description vacuum", Boolean.TRUE, 1L);
        itemDao.updateItem(itemId, item2);
        assertThat(itemDao.getItemById(itemId).getName().equals("Item Update"));
    }

    @Test
    public void testSearchItem() {
        assertThat(itemDao.searchItem("VacUUm").get(0).getName().equals("Item Update"));
    }
}
