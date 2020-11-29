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
        log.info("Message sent to " + receiverId + " from " + senderUser.getId());
        model.put("message", "Message sent to " + receiverId + " from " + senderUser.getId());
        return getMessages(token, receiverId);
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
        if (user.getMessages().keySet().stream().anyMatch(key -> key.equals(receiverId))) {
            user.getMessages().get(receiverId).stream().filter(message -> !message.isRead()).forEach(unreadMessage -> {
                unreadMessage.setRead(true);
                unreadMessage.setReadDate(new Date());
            });
        }
        this.userRepository.save(user);
        model.put("message", "Messages to " + user.getId() + " user from " + receiverId + " user is read now.");
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

    public Object getChatsInfo(String token) {
        User currentUser = userService.getUserByToken(token);
        List<MessageData> allChats = new ArrayList<>();
        currentUser.getMessages().keySet().forEach(key -> {
            allChats.add(new MessageData(key, currentUser.getMessages().get(key).get(currentUser.getMessages().get(key).size() - 1).getCreatedDate()
                    , currentUser.getMessages().get(key).get(currentUser.getMessages().get(key).size() - 1)));
        });

        return ok(allChats.stream().sorted(Comparator.comparing(MessageData::getLastMessageDate).reversed()).collect(Collectors.toList()));
    }

    public Object getMessages(String token, String receiverId) {
        User currentUser = userService.getUserByToken(token);
        List<Message> allMessages = new ArrayList<>();
        if (currentUser.getMessages().keySet().stream().anyMatch(key -> key.equals(receiverId))) {
            allMessages = currentUser.getMessages().get(receiverId);
        }
        readMessage(token, receiverId);
        return ok(allMessages);
    }
}
