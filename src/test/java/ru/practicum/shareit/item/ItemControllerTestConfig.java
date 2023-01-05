package ru.practicum.shareit.item;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.practicum.shareit.item.service.IItemService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ItemControllerTestConfig {
    @Bean
    public IItemService itemService() {
        return mock(IItemService.class);
    }
}
