package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.User;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Message;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.UserRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewMessage;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.response.MessageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@Service
@Slf4j
public class MessageService {


    private final UserService userService;
    private final UserRepository userRepository;

    public MessageService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    public Object sendMessage(String token, String receiverId, NewMessage messageContent) {
        Map<Object, Object> model = new HashMap<>();
        Message message = new Message(messageContent.getContent(), new Date());

        User senderUser = userService.getUserByToken(token);
        User receiverUser = userService.getUserById(receiverId);
        this.userRepository.save(addMessage(senderUser, receiverUser.getId(), message, false));
        this.userRepository.save(addMessage(receiverUser, senderUser.getId(), message, true));

        model.put("message", "Message sent to " + receiverId + " from " + senderUser.getId());
        log.info("Message sent to " + receiverId + " from " + senderUser.getId());
        return ok(model);
    }

    private User addMessage(User user, String secondId, Message message, boolean isReceived) {
        message.setReceived(isReceived);
        message.setRead(!isReceived);
        message.setReadDate(!isReceived ? new Date() : null);
        if (user.getMessages().get(secondId) == null) {
            List<Message> messageList = new ArrayList<>();
            messageList.add(message);
            user.getMessages().put(secondId, messageList);
        } else {
            user.getMessages().get(secondId).add(message);
        }
        return user;
    }


    public Object readMessage(String token, String receiverId) {
        Map<Object, Object> model = new HashMap<>();
        User user = userService.getUserByToken(token);
        user.getMessages().get(receiverId).stream().filter(message -> !message.isRead()).forEach(unreadMessage -> {
            unreadMessage.setRead(true);
            unreadMessage.setReadDate(new Date());
        });
        this.userRepository.save(user);
        model.put("message", "Messages to " + user.getId() + " user from " + receiverId + " user is read now.");
        log.info("All messages to " + user.getId() + " user from " + receiverId + " user is read now.");
        return ok(model);
    }

    public Object getUnreadMessage(String token) {
        AtomicReference<Long> count = new AtomicReference<>((long) 0);

        User currentUser = userService.getUserByToken(token);
        currentUser.getMessages().values().forEach(messages -> {
            count.updateAndGet(v -> v + messages.stream().filter(message -> !message.isRead()).count());
        });
        return ok(count);
    }

    public Object getMessages(String token) {
        User currentUser = userService.getUserByToken(token);
        List<MessageData> allMessages = new ArrayList<>();
        currentUser.getMessages().keySet().forEach(key -> {
            allMessages.add(new MessageData(key, currentUser.getMessages().get(key).get(currentUser.getMessages().get(key).size() - 1).getCreatedDate(), currentUser.getMessages().get(key)));
        });

        return ok(allMessages.stream().sorted(Comparator.comparing(MessageData::getLastMessageDate).reversed()).collect(Collectors.toList()));
    }
}
