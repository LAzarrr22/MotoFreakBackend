package com.MJ.MotoFreaksBackend.MotoFreaksBackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String messageContent;
    private boolean isRead;
    private Date createdDate;
    private Date readDate;
    private boolean isReceived;

    public Message(String messageContent, Date createdDate) {
        this.messageContent = messageContent;
        this.createdDate = createdDate;
    }
}
