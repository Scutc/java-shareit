package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoForResponse;

import java.util.List;

public interface IRequestService {
    RequestDtoForResponse addRequest(RequestDto requestDto, Long requestorId);

    List<RequestDtoForResponse> findRequestByRequestorId(Long requestorId);

    List<RequestDtoForResponse> findAllRequests(Long userId, Integer from, Integer size);

    RequestDtoForResponse getRequestById(Long requestId, Long userId);
}
