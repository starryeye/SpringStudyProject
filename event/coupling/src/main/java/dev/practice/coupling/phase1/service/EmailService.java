package dev.practice.coupling.phase1.service;

import dev.practice.coupling.repository.EmailSendHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailSendHistoryRepository emailSendHistoryRepository;
}
