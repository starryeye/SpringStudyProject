package dev.practice.valid.validator;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value // lombok
@EqualsAndHashCode(callSuper=false) // lombok
public class Person extends SelfValidating<Person> {

    /**
     * @Value
     * - @Value 은 불변(immutable) 객체를 생성하기 위해 사용된다.
     * - 모든 필드가 final 이 되어 객체 생성 후 필드 값을 변경할 수 없다. 생성자를 통해서만 값이 할당된다.
     * - 클래스 자체를 final 로 만들어서 상속이 불가능해진다.
     * - @Data 처럼 메서드(toString(), equals(), hashCode(), getter)를 생성하지만, setter 메서드는 생성하지 않는다.
     */

    @NotNull // jakarta.validation
    String firstName;
    @NotNull // jakarta.validation
    String lastName;
    @Min(value = 10) // jakarta.validation
    Integer age;

    @Builder // lombok
    private Person(String firstName, String lastName, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;

        this.validateSelf(); // jakarta.validation.validator 에 의해 검증함
    }
}
