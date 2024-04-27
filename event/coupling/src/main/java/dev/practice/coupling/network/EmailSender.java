package dev.practice.coupling.network;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailSender {

    public void send(String emailAddress, String message) {

        log.info("Sending email to = {}, message = {}", emailAddress, message);
    }
}
