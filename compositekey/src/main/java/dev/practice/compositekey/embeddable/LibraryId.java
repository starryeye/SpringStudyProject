package dev.practice.compositekey.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class LibraryId implements Serializable {

    private String regionNumber;
    private String name;

    /**
     * 복합키 식별자 클래스
     *
     * @Embeddable 어노테이션을 붙여준다.
     * Serializable 을 구현한다.
     * equals, hashcode 를 오버라이딩한다.
     * 기본 생성자가 존재해야한다.
     * public class 여야 한다.
     */

    @Builder // 편의
    private LibraryId(String regionNumber, String name) {
        this.regionNumber = regionNumber;
        this.name = name;
    }

}
