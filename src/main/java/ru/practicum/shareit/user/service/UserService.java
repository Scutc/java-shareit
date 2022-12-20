package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public UserDto getUserById(Long userId) {
        log.info("Получение пользователя по ID = {}", userId);
        User user = userRepository.getUserById(userId);
        if (user != null) {
            return toUserDto(user);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        List<User> users = userRepository.findAllUsers();
        return users.stream()
                    .map(this::toUserDto)
                    .collect(Collectors.toList());
    }

    public UserDto createUser(UserDto userDto) {
        log.info("Создание нового пользователя {}", userDto);
        try {
            User user = userRepository.save(toUser(userDto));
            return toUserDto(user);
        } catch (DataIntegrityViolationException e) {
            log.warn("Пользователь с такими данными уже существует в базе!");
            throw new DuplicateDataException("Пользователь с такими данными уже существует в базе!");
        }
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("Обновление пользователя с ID = {}", userId);
        User userForUpdate = userRepository.save(toUser(userDto));
        return toUserDto(userForUpdate);
    }

    public boolean deleteUser(Long userId) {
        if (true) {
            log.info("Удаление пользователя ID = {} успешно", userId);
            return true;
        } else {
            log.info("Удаление пользователя ID = {} не удалось, пользователь не найден", userId);
            return false;
        }
    }

    private UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    private User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail(),
            //    itemRepository.getItemsByUserWithin(userDto.getId())
                null);
    }
}
