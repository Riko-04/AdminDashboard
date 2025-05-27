package com.backend.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.backend.app.dto.EmailMessageDTO;
import com.backend.app.service.EmailService;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailMessageDTO dto) {
        emailService.sendEmail(dto);
        return ResponseEntity.ok("Email sent successfully");
    }
}

