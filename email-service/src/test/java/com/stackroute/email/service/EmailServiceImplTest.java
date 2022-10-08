package com.stackroute.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stackroute.email.model.EmailRequest;
import com.stackroute.email.model.Invoice;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmailServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EmailServiceImplTest {
    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @MockBean
    private JavaMailSender javaMailSender;


    @Test
    void testListner() {


        EmailServiceImpl emailServiceImpl = new EmailServiceImpl();
        emailServiceImpl.listner(new Invoice("42", 123, "jane.doe@example.org", "jane.doe@example.org", "Payment Method",
                "GBP", "10", "Payment Mode", "Payment Status", "Payment Time", 123, "2020-03-01", "Venue Name"));
    }


    @Test
    void testListner2() {




        (new EmailServiceImpl()).listner(null);
    }

    @Test
    void testListner3() {

        EmailServiceImpl emailServiceImpl = new EmailServiceImpl();
        Invoice invoice = mock(Invoice.class);
        when(invoice.getBookingId()).thenReturn(123);
        when(invoice.getUserEmail()).thenReturn("jane.doe@example.org");
        emailServiceImpl.listner(invoice);
        verify(invoice).getBookingId();
        verify(invoice).getUserEmail();
    }


    @Test
    void testListner4() {


        EmailServiceImpl emailServiceImpl = new EmailServiceImpl();
        Invoice invoice = mock(Invoice.class);
        when(invoice.getBookingId()).thenThrow(new RuntimeException("An error occurred"));
        when(invoice.getUserEmail()).thenThrow(new RuntimeException("An error occurred"));
        emailServiceImpl.listner(invoice);
        verify(invoice).getUserEmail();
    }


    @Test
    void testSendSimpleMail() throws MailException {
        doNothing().when(javaMailSender).send((SimpleMailMessage) any());
        assertEquals("Mail Sent Successfully...", emailServiceImpl.sendSimpleMail(
                new EmailRequest("Recipient", "Venue Booking", "Payment Done Successfully", "Invoice")));
        verify(javaMailSender).send((SimpleMailMessage) any());
    }


    @Test
    void testSendSimpleMail2() throws MailException {
        doThrow(new MailSendException("Mail Sent Successfully...")).when(javaMailSender).send((SimpleMailMessage) any());
        assertEquals("Error while Sending Mail", emailServiceImpl.sendSimpleMail(
                new EmailRequest("Recipient", "Venue Booking", "Payment Done Successfully", "Invoice")));
        verify(javaMailSender).send((SimpleMailMessage) any());
    }

    @Test
    void testSendSimpleMail3() throws MailException {
        doThrow(new RuntimeException("An error occurred")).when(javaMailSender).send((SimpleMailMessage) any());
        assertThrows(RuntimeException.class, () -> emailServiceImpl.sendSimpleMail(
                new EmailRequest("Recipient", "Venue Booking", "Payment Done Successfully", "Invoice")));
        verify(javaMailSender).send((SimpleMailMessage) any());
    }

    @Test
    void testSendMailWithAttachment() throws MessagingException, MailException {
        doNothing().when(javaMailSender).send((MimeMessage) any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertEquals("Mail sent Successfully", emailServiceImpl.sendMailWithAttachment(
                new EmailRequest("Recipient", "Venue Booking", "Payment Done Successfully", "Invoice")));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send((MimeMessage) any());
    }

    /**
     * Method under test: {@link EmailServiceImpl#sendMailWithAttachment(EmailRequest)}
     */
    @Test
    void testSendMailWithAttachment2() throws MessagingException, MailException {
        doThrow(new MailSendException("$F{UU}")).when(javaMailSender).send((MimeMessage) any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertThrows(MailSendException.class, () -> emailServiceImpl.sendMailWithAttachment(
                new EmailRequest("Recipient", "Venue Booking", "Payment Done Successfully", "Invoice")));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send((MimeMessage) any());
    }


    @Test
    void testSendMailWithAttachment3() throws MessagingException, MailException {
        doNothing().when(javaMailSender).send((MimeMessage) any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertEquals("Error while sending mail!!!", emailServiceImpl.sendMailWithAttachment(
                new EmailRequest("", "Venue Booking", "Payment Done Successfully", "Invoice")));
        verify(javaMailSender).createMimeMessage();
    }


    @Test
    void testSendMailWithAttachment4() throws MessagingException, MailException {
        doNothing().when(javaMailSender).send((MimeMessage) any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        EmailRequest emailRequest = mock(EmailRequest.class);
        when(emailRequest.getSubject()).thenThrow(new MailSendException("$F{UU}"));
        when(emailRequest.getMessage()).thenReturn("Payment Done Successfully");
        when(emailRequest.getRecipient()).thenReturn("Recipient");
        assertThrows(MailSendException.class, () -> emailServiceImpl.sendMailWithAttachment(emailRequest));
        verify(javaMailSender).createMimeMessage();
        verify(emailRequest).getMessage();
        verify(emailRequest).getRecipient();
        verify(emailRequest).getSubject();
    }


    @Test
    void testGenerateReportMessage() {
        assertEquals("<head><h1 style=\"color:Blue;font-size:40px;text-align:center;\">Invoice<h1></head><body><table"
                + " border=1><tr><th>Invoice Id</th><th>User Email</th><th>Venue Owner Email</th><th>Total amount</th>"
                + "<th>Payment Method</th><th>Currency</th><th>Payment Mode</th><th>Payment Status</th><th>Payment"
                + " Time</th><tr><tr><td>dsh</td><td>etyr</td><td>gdte</td><td>teuy</td><td>gfar</td><td>dfsrw</td><td"
                + ">shytt</td><td>srety</td><td>sryy</td><td>null</td></tr></table><i><h1 style=\"color:red;font-size:40px"
                + ";text-align:left;\">Thank You<h1></i></body>", emailServiceImpl.generateReportMessage());
        assertEquals("<head><h1 style=\"color:Blue;font-size:40px;text-align:center;\">Invoice<h1></head><body><table"
                        + " border=1><tr><th>Invoice Id</th><th>User Email</th><th>Venue Owner Email</th><th>Total amount</th>"
                        + "<th>Payment Method</th><th>Currency</th><th>Payment Mode</th><th>Payment Status</th><th>Payment"
                        + " Time</th><tr><tr><td>42</td><td>jane.doe@example.org</td><td>jane.doe@example.org</td><td>10</td><td>Payment"
                        + " Method</td><td>GBP</td><td>Payment Mode</td><td>Payment Status</td><td>Payment Time</td><td>Venue"
                        + " Name</td></tr></table><i><h1 style=\"color:red;font-size:40px;text-align:left;\">Thank You<h1></i><"
                        + "/body>",
                emailServiceImpl.generateReportMessage(
                        new Invoice("42", 123, "jane.doe@example.org", "jane.doe@example.org", "Payment Method", "GBP", "10",
                                "Payment Mode", "Payment Status", "Payment Time", 123, "2020-03-01", "Venue Name")));
    }




    @Test
    void testGenerateReportMessage2() {
        Invoice invoice = mock(Invoice.class);
        when(invoice.getCurrency()).thenReturn("GBP");
        when(invoice.getInvoiceId()).thenReturn("42");
        when(invoice.getPaymentMethod()).thenReturn("Payment Method");
        when(invoice.getPaymentMode()).thenReturn("Payment Mode");
        when(invoice.getPaymentStatus()).thenReturn("Payment Status");
        when(invoice.getPaymentTime()).thenReturn("Payment Time");
        when(invoice.getTotalAmount()).thenReturn("10");
        when(invoice.getUserEmail()).thenReturn("jane.doe@example.org");
        when(invoice.getVenueName()).thenReturn("Venue Name");
        when(invoice.getVenueOwnerEmail()).thenReturn("jane.doe@example.org");
        assertEquals("<head><h1 style=\"color:Blue;font-size:40px;text-align:center;\">Invoice<h1></head><body><table"
                + " border=1><tr><th>Invoice Id</th><th>User Email</th><th>Venue Owner Email</th><th>Total amount</th>"
                + "<th>Payment Method</th><th>Currency</th><th>Payment Mode</th><th>Payment Status</th><th>Payment"
                + " Time</th><tr><tr><td>42</td><td>jane.doe@example.org</td><td>jane.doe@example.org</td><td>10</td><td>Payment"
                + " Method</td><td>GBP</td><td>Payment Mode</td><td>Payment Status</td><td>Payment Time</td><td>Venue"
                + " Name</td></tr></table><i><h1 style=\"color:red;font-size:40px;text-align:left;\">Thank You<h1></i><"
                + "/body>", emailServiceImpl.generateReportMessage(invoice));
        verify(invoice).getCurrency();
        verify(invoice).getInvoiceId();
        verify(invoice).getPaymentMethod();
        verify(invoice).getPaymentMode();
        verify(invoice).getPaymentStatus();
        verify(invoice).getPaymentTime();
        verify(invoice).getTotalAmount();
        verify(invoice).getUserEmail();
        verify(invoice).getVenueName();
        verify(invoice).getVenueOwnerEmail();
    }
}

