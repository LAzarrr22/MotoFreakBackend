package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.response;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {
    private String id;
    private List<Message> messages;
}
