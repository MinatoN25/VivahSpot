package com.stackroute.email.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.stackroute.email.config.MQConfig;
//import com.stackroute.email.consumer.InvoiceConsumer;
import com.stackroute.email.model.EmailRequest;
import com.stackroute.email.model.Invoice;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	private Invoice invoice;

	@RabbitListener(queues = MQConfig.QUEUE)
	public void listner(Invoice invoice) {
		try {
			this.invoice = invoice;
			EmailRequest email = new EmailRequest();
			email.setInvoice(invoice.toString());
			email.setMessage(invoice.toString());
			email.setRecipient(invoice.getUserEmail());
			email.setSubject("VivahSpot Booking: " + invoice.getBookingId());

			sendMailWithAttachment(email);

			email.setRecipient(invoice.getVenueOwnerEmail());
			email.setMessage("Your venue " + invoice.getVenueName() + " has been booked by " + invoice.getUserEmail()
					+ " for date " + invoice.getBookingDate());
			sendSimpleMail(email);
		} catch (Exception m) {
			log.error("Exception occured in listner");
		}

	}

	public String sendSimpleMail(EmailRequest emailRequest) {

		try {

			SimpleMailMessage mailMessage = new SimpleMailMessage();

			mailMessage.setFrom(sender);
			mailMessage.setTo(emailRequest.getRecipient());
			mailMessage.setSubject(emailRequest.getSubject());
			mailMessage.setText(emailRequest.getMessage());

			javaMailSender.send(mailMessage);
			return "Mail Sent Successfully...";
		}

		catch (MailSendException e) {
			return "Error while Sending Mail";
		}
	}

	public String sendMailWithAttachment(EmailRequest emailRequest) throws MessagingException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			getClass().getClassLoader();

			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(Arrays.asList(invoice),
					false);
			JasperReport compileReport;
			compileReport = JasperCompileManager.compileReport(new FileInputStream("invoice.jrxml"));
			Map<String, Object> parameters = new HashMap<>();
			JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
			DataSource aAttachment = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");

			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(sender);
			mimeMessageHelper.setTo(emailRequest.getRecipient());
			mimeMessageHelper.setText(emailRequest.getMessage());
			mimeMessageHelper.setSubject(emailRequest.getSubject());
			mimeMessageHelper.addAttachment("customer_invoice.pdf", aAttachment);
			mimeMessageHelper.setText(generateReportMessage(), true);
			javaMailSender.send(mimeMessage);
			return "Mail sent Successfully";
		}

		catch (MessagingException e) {

			return "Error while sending mail!!!";
		} catch (JRException e) {
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	 public String generateReportMessage(Invoice invoice) {
	        StringBuilder stringBuilder = generateCommonHtmlHead();
	            stringBuilder.append("<tr>");
	            stringBuilder.append("<td>").append(invoice.getInvoiceId()).append("</td>");
	            stringBuilder.append("<td>").append(invoice.getUserEmail()).append("</td>");
	            stringBuilder.append("<td>").append(invoice.getVenueOwnerEmail()).append("</td>");
	            stringBuilder.append("<td>").append(invoice.getTotalAmount()).append("</td>");
	            stringBuilder.append("<td>").append(invoice.getPaymentMethod()).append("</td>");
	            stringBuilder.append("<td>").append(invoice.getCurrency()).append("</td>");
	            stringBuilder.append("<td>").append(invoice.getPaymentMode()).append("</td>");
	            stringBuilder.append("<td>").append(invoice.getPaymentStatus()).append("</td>");
	            stringBuilder.append("<td>").append(invoice.getPaymentTime()).append("</td>");
	          
	            
	            stringBuilder.append("<td>").append(invoice.getVenueName()).append("</td>");
	      
	            stringBuilder.append("</tr>");
	        generateCommonFooter(stringBuilder);
	        return stringBuilder.toString();
	    }


	    private StringBuilder generateCommonHtmlHead() {
	        StringBuilder stringBuilder = new StringBuilder();

	        return stringBuilder.append("<head>")
	                .append("<h1 style=\"color:Blue;font-size:40px;text-align:center;\">Invoice<h1>")
	                .append("</head>")
	                .append("<body>")
	                .append("<table border=1>")
	                .append("<tr>")
	                .append("<th>Invoice Id</th><th>User Email</th><th>Venue Owner Email</th><th>Total amount</th><th>Payment Method</th><th>Currency</th><th>Payment Mode</th><th>Payment Status</th><th>Payment Time</th><th>Venue Name</th>")
					.append("<tr>");
	               
	    }

	    private void generateCommonFooter(StringBuilder stringBuilder) {
	        stringBuilder.append("</table><i><h1 style=\"color:red;font-size:40px;text-align:left;\">Thank You<h1></i></body>");
	    }


	    public String generateReportMessage() { 	
	        return generateReportMessage(invoice);
	    }

}
