package com.backend.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.app.entity.EmailMessage;

public interface EmailRepository extends JpaRepository<EmailMessage, Long> {

}
