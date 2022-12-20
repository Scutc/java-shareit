package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserDto getUserById(Long userId) {
        log.info("Получение пользователя по ID = {}", userId);
        User user = userRepository.getUserById(userId);
        if (user != null) {
            return UserMapper.toUserDto(user);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        List<User> users = userRepository.findAllUsers();
        return users.stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
    }

    public UserDto createUser(UserDto userDto) {
        log.info("Создание нового пользователя {}", userDto);
        try {
            User user = userRepository.save(UserMapper.toUser(userDto));
            return UserMapper.toUserDto(user);
        } catch (DataIntegrityViolationException e) {
            log.warn("Пользователь с такими данными уже существует в базе!");
            throw new DuplicateDataException("Пользователь с такими данными уже существует в базе!");
        }
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("Обновление пользователя с ID = {}", userId);
        User userForUpdate = userRepository.getUserById(userId);
        if (userDto.getName() != null) {
            userForUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userForUpdate.setEmail(userDto.getEmail());
        }
        try {
            userRepository.save(userForUpdate);
        } catch (DataIntegrityViolationException e) {
            log.warn("Пользователь с такими данными уже существует в базе!");
            throw new DuplicateDataException("Пользователь с такими данными уже существует в базе!");
        }
        return UserMapper.toUserDto(userForUpdate);
    }

    public boolean deleteUser(Long userId) {
        User userForDelete = userRepository.getUserById(userId);
        if (userForDelete != null) {
            userRepository.delete(userForDelete);
            log.info("Удаление пользователя ID = {} успешно", userId);
            return true;
        } else {
            log.info("Удаление пользователя ID = {} не удалось, пользователь не найден", userId);
            return false;
        }
    }
}
