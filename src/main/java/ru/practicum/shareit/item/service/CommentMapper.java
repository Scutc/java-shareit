package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toComment(CommentDto commentDto) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                null,
                null,
                LocalDateTime.now());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }
}
