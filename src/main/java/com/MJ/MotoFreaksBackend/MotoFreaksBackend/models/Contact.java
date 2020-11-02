package com.MJ.MotoFreaksBackend.MotoFreaksBackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private String email;
    private String phone;
    private String facebook;
    private String instagram;
    private String twitter;
    private String skype;
    private String youtube;

    public Contact(String email) {
        this.email = email;
    }
}
