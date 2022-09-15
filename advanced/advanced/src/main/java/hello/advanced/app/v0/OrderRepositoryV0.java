package hello.advanced.app.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV0 {

    public void save(String itemId) {
        //저장 로직
        if(itemId.equals("ex")) { //id에 ex가 포함되면 예외 발생
            throw new IllegalStateException("예외 발생!");
        }
        sleep(1000); //상품을 주문하는데는 1초정도 걸린다고 가정
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
