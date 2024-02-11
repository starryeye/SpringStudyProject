package dev.practice.pagingandsorting.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @Builder
    private Member(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public static Member create(String name, Address address) {
        return Member.builder()
                .id(null)
                .name(name)
                .address(address)
                .build();
    }
}
