package ru.practicum.shareit.mvc.request;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.shareit.request.service.IRequestService;

import static org.mockito.Mockito.mock;

@Configuration
public class RequestControllerTestConfig {
    @Bean
    public IRequestService requestService() {
        return mock(IRequestService.class);
    }
}
