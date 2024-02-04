package dev.practice.compositekey.idclass;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class SchoolId implements Serializable {

    private String regionNumber;
    private String name;

    /**
     * 복합키 식별자 클래스
     *
     * 식별자 클래스의 속성명과 엔티티에서 사용하는 식별자의 속성명이 같아야한다.
     * Serializable 을 구현한다.
     * equals, hashcode 를 구현해야한다.
     * 기본 생성자가 존재해야한다.
     * 식별자는 public class 여야한다.
     */

    @Builder // 편의
    private SchoolId(String regionNumber, String name) {
        this.regionNumber = regionNumber;
        this.name = name;
    }
}
