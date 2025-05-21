package com.example.Lucky1.controller;

import com.example.Lucky1.dto.CommentDTO;
import com.example.Lucky1.dto.CommentRequest;
import com.example.Lucky1.model.ForecastComment;
import com.example.Lucky1.model.User;
import com.example.Lucky1.repository.ForecastCommentRepository;
import com.example.Lucky1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ForecastCommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/comments/{chatId}")
    public List<CommentDTO> getComments(@PathVariable Long chatId) {
        return commentRepository.findByChatIdOrderByCreatedAtAsc(chatId).stream().map(comment -> {
            User user = userRepository.findById(comment.getUserId()).orElse(null);
            return new CommentDTO(
                    user != null ? user.getUsername() : "Unknown",
                    comment.getMessage(),
                    comment.getCreatedAt()
            );
        }).toList();
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody CommentRequest request) {
        ForecastComment comment = new ForecastComment();
        comment.setChatId(request.getChatId());
        comment.setUserId(request.getUserId());
        comment.setMessage(request.getMessage());
        commentRepository.save(comment);
    }
}
