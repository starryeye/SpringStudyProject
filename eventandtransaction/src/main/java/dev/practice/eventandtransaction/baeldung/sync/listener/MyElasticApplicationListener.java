package dev.practice.eventandtransaction.baeldung.sync.listener;

import dev.practice.eventandtransaction.baeldung.sync.event.MySimpleEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyElasticApplicationListener implements ApplicationListener<ApplicationEvent> {

    /**
     * Event 객체 : MySimpleEvent
     * Publisher 객체 : MyApplicationEventPublisher
     * Listener 객체 : MyElasticApplicationListener
     *
     * MySimpleEvent 객체는 ApplicationEvent 를 상속하지 않는다.
     * 해당 이벤트를 처리하는 Listener 를 만들고 싶을 때 이와 같이 객체를 만들면 된다.
     */

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof PayloadApplicationEvent) {

            PayloadApplicationEvent<?> payloadEvent = (PayloadApplicationEvent<?>) event;

            if (payloadEvent.getPayload() instanceof MySimpleEvent) {

                MySimpleEvent mySimpleEvent = (MySimpleEvent) payloadEvent.getPayload();
                log.info("Received MySimpleEvent.. message: {}, tx : {}", mySimpleEvent.getMessage(), Thread.currentThread().getName());
            }
        }
    }
}
