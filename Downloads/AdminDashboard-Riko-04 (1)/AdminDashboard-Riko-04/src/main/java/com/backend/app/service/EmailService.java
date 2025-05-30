package com.backend.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

// import com.backend.app.dto.EmailMessageDTO;
import com.backend.app.entity.EmailMessage;
import com.backend.app.repository.EmailRepository;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;

    @Value("${app.support.email}")
    private String supportEmail;

    public void sendEmail(EmailMessage dto) {
        System.out.println("Sending email to support: " + dto);
        // EmailMessage savedMessage = EmailMessage.builder()
        //         .firstName(dto.getFirstName())
        //         .lastName(dto.getLastName())
        //         .email(dto.getEmail())
        //         .phoneNumber(dto.getPhoneNumber())
        //         .message(dto.getMessage())
        //         .build();

        emailRepository.save(dto);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(supportEmail);
            helper.setSubject("New Contact Message from " + dto.getFirstName() + " " + dto.getLastName());
            helper.setFrom(dto.getEmail());

            String content = """
                <p><b>Sender Name:</b> %s %s</p>
                <p><b>Email:</b> %s</p>
                <p><b>Phone:</b> %s</p>
                <p><b>Message:</b></p>
                <p>%s</p>
                """.formatted(
                dto.getFirstName(), dto.getLastName(),
                dto.getEmail(), dto.getPhoneNumber(), dto.getMessage()
            );

            helper.setText(content, true);

            mailSender.send(message);
        } catch (MailException | jakarta.mail.MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
