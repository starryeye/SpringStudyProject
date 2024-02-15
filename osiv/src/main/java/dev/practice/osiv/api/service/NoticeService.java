package dev.practice.osiv.api.service;

import dev.practice.osiv.domain.Notice;
import dev.practice.osiv.domain.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public void create(String message) {

        Notice notice = Notice.create(message);

        noticeRepository.save(notice);
    }
}
