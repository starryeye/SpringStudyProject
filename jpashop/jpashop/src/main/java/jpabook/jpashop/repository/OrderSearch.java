package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {

    private String memberName; //회원이름은 뭘로 검색할것인가..
    private OrderStatus orderStatus;
}
