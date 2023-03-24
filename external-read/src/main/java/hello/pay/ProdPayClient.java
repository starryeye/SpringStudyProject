package hello.pay;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProdPayClient implements PayClient{
    @Override
    public void pay(int money) {
        log.info("production pay process money={}", money);
    }
}
