package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewMessage;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/message")
@Slf4j
@CrossOrigin("*")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(path = "/send/{receiverId}", method = RequestMethod.PUT, produces = "application/json")
    public Object sendMessage(HttpServletRequest req, @RequestBody NewMessage messageContent, @PathVariable String receiverId) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return messageService.sendMessage(token, receiverId, messageContent);
    }

    @RequestMapping(path = "/read/{receiverId}", method = RequestMethod.POST, produces = "application/json")
    public Object getReadAllMessagesWithUser(HttpServletRequest req, @PathVariable String receiverId) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return messageService.readMessage(token, receiverId);
    }

    @RequestMapping(path = "/unread", method = RequestMethod.GET, produces = "application/json")
    public Object getUnreadCount(HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return messageService.getUnreadMessage(token);
    }

    @RequestMapping(path = "/chats", method = RequestMethod.GET, produces = "application/json")
    public Object getChats(HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return messageService.getChatsInfo(token);
    }

    @RequestMapping(path = "/{receiverId}", method = RequestMethod.GET, produces = "application/json")
    public Object getMessages(HttpServletRequest req, @PathVariable String receiverId) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return messageService.getMessages(token, receiverId);
    }
}
