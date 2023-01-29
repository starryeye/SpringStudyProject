package study.jwttutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 회원 가입 시, 사용 할 UserDto 클래스
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;


    /**
     * @JsonProperty (access = JsonProperty.Access.WRITE_ONLY) 는
     * 해당 필드에 데이터를 오직 쓰려는 경우(deserialize)에만 접근이 허용된다.
     * -> Json 이 Java Object 로 변환 될 때만 허용
     * -> 응답을 보낼 때 처럼, Java Object 가 Json 으로 변환 될 때는 허용되지 않는 어노테이션
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;
}
