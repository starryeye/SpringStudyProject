package study.jwttutorial.dto;

import lombok.*;

/**
 * 토큰 정보를 Response 할 때, 사용 할 TokenDto 클래스
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String token;
}
