package com.stackroute.email.service;

import javax.mail.MessagingException;

import com.stackroute.email.model.EmailRequest;

public interface EmailService {

    String sendSimpleMail(EmailRequest emailRequest);

    String sendMailWithAttachment(EmailRequest emailRequest) throws MessagingException;
}
