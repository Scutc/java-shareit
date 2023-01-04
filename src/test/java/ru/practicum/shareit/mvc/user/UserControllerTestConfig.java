package ru.practicum.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.shareit.user.service.IUserService;

import static org.mockito.Mockito.mock;

@Configuration
public class UserControllerTestConfig {
    @Bean
    public IUserService userService() {
        return mock(IUserService.class);
    }
}