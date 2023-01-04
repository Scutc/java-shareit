package ru.practicum.shareit.mvc.item;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.shareit.item.service.IItemService;

import static org.mockito.Mockito.mock;

@Configuration
public class ItemControllerTestConfig {
    @Bean
    public IItemService itemService() {
        return mock(IItemService.class);
    }
}
