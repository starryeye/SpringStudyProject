package dev.practice.pagingandsorting.repository;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 필수
@EqualsAndHashCode
public class Address {

    /**
     * 임베디드 타입 (복합 값 타입)
     *
     *
     * 1. 값 타입은 여러 엔티티에 걸친 공유 참조를 피해야 한다.
     * 공유 참조를 최대한 억제하려면 불변객체로 만들면 어느정도 해소가 된다.(자바의 기본객체(String 등)를 제외하면 객체는 기본적으로 얕은 복사)
     * (완전히 같은 값을 사용하더라도 서로 다른 엔티티면 새로 인스턴스를 만들어서 할당하도록 하자..)
     *
     * setter 를 제공하지 않음으로 해서 불변객체이다.
     * 수정할 수 없으므로 수정이 필요하면 생성자를 이용하여 새로운 객체를 만들게 됨을 의도한다.
     *
     * 2. 값 타입은 값이므로 equals 를 재정의해야한다.
     * equals 를 재정의하면 hashCode 도 재정의 해야 안전 (HashSet, HashMap 동작 때문)
     *
     * 3. 값 타입도 기본 생성자가 필수이다.
     */

    private String city;
    private String street;
    private String zipcode;

    @Builder
    private Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public static Address create(String city, String street, String zipcode) {
        return Address.builder()
                .city(city)
                .street(street)
                .zipcode(zipcode)
                .build();
    }
}
