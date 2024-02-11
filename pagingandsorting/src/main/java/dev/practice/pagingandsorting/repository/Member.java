package dev.practice.pagingandsorting.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate registrationDate;

    @Embedded
    private Address address;

    @Builder
    private Member(Long id, String name, LocalDate registrationDate, Address address) {
        this.id = id;
        this.name = name;
        this.registrationDate = Objects.isNull(registrationDate) ? LocalDate.now() : registrationDate;
        this.address = address;
    }

    public static Member create(String name, LocalDate registrationDate, Address address) {
        return Member.builder()
                .id(null)
                .name(name)
                .registrationDate(registrationDate)
                .address(address)
                .build();
    }
}
