package com.backend.app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailMessageDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String message;
}

