package ru.practicum.shareit.utility;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotValidParamsException;

@Component
@Data
public class PaginationConverter {

    public Pageable convert(Integer from, Integer size) {
        Pageable pageable;
        if (from == null && size == null) {
            pageable = Pageable.unpaged();
        } else if (checkPagination(from, size)) {
            pageable = PageRequest.of(from, size);
        } else {
            throw new NotValidParamsException("Некорректно заданы параметры пагинации!");
        }
        return pageable;
    }

    private boolean checkPagination(Integer from, Integer size) {
        return from >= 0 && size > 0;
    }
}
