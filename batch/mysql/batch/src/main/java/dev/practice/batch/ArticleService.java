package dev.practice.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public Long save(String title, String content) {

        Article article = Article.create(title, content);

        return articleRepository.save(article).getId();
    }

    @Transactional
    public Long saveFail(String title, String content) {
        Article article = Article.create(title, content);

        try {
            articleRepository.save(article);
            throw new IllegalArgumentException(); // 여기 중단점 걸고 DB 에서 저장되어있는지 확인 (IDENTITY 전략 확인)
        }catch (RuntimeException e) {
            throw e;
        }
    }
}
