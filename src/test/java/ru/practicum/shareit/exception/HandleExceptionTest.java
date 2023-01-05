package ru.practicum.shareit.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ErrorHandler.class)
public class HandleExceptionTest {

    @Autowired
    MockMvc mvc;
}

