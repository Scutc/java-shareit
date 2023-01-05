package ru.practicum.shareit.request;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.practicum.shareit.request.service.IRequestService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class RequestControllerTestConfig {
    @Bean
    public IRequestService requestService() {
        return mock(IRequestService.class);
    }
}
