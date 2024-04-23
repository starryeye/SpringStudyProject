package dev.practice.eventandtransaction.baeldung.sync.listener;

import dev.practice.eventandtransaction.baeldung.sync.event.MyApplicationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyApplicationListener implements ApplicationListener<MyApplicationEvent> {

    /**
     * Event 객체 : MyApplicationEvent
     * Publisher 객체 : MyApplicationEventPublisher
     * Listener 객체 : MyApplicationListener
     *
     * MyApplicationEvent 객체는 ApplicationEvent 를 상속한다.
     * 해당 이벤트를 처리하는 Listener 를 만들고 싶을 때 이와 같이 객체를 만들면 된다.
     */

    @Override
    public void onApplicationEvent(MyApplicationEvent event) {
        log.info("Received MyApplicationEvent.. message: {}, tx : {}", event.getMessage(), Thread.currentThread().getName());
    }
}
