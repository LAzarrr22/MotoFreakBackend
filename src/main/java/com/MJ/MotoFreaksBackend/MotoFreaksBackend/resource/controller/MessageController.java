package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewMessage;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.security.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/message")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(path = "/send/{receiverId}", method = RequestMethod.POST, produces = "application/json")
    public Object addCar(HttpServletRequest req, @RequestBody NewMessage messageContent, @PathVariable String receiverId) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return messageService.sendMessage(token, receiverId, messageContent);
    }

    @RequestMapping(path = "/read/all/user/{receiverId}", method = RequestMethod.POST, produces = "application/json")
    public Object getReadLastById(HttpServletRequest req, @PathVariable String receiverId) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return messageService.readMessage(token, receiverId);
    }

    @RequestMapping(path = "/unread/count", method = RequestMethod.GET, produces = "application/json")
    public Object getUnreadMessage(HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return messageService.getUnreadMessage(token);
    }

    @RequestMapping(path = "/get", method = RequestMethod.GET, produces = "application/json")
    public Object getMessages(HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return messageService.getMessages(token);
    }
}
