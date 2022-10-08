package com.stackroute.email.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    private String recipient;
    private String subject;
    private String message;
    private String  invoice;

    public String getAttachment() {
        return null;
    }
}
