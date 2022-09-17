package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * HelloTraceV1 클래스는..
 * TraceStatus를 다루기 위한 메서드 집합체이다. (Status객체를 맴버로 가지지 않음.
 * TraceStatus를 생성 후 반환
 * TraceStatus를 종료
 *
 * +V2
 * TraceStatus를 생성할 때, traceId는 유지시키고, level은 +1 해서 생성하는..
 * beginSync 메서드를 추가하였다.
 */

@Slf4j
@Component
public class HelloTraceV2 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    public TraceStatus begin(String message) {

        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();

        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

        return new TraceStatus(traceId, startTimeMs, message);
    }

    //V2에서 추가
    public TraceStatus beginSync(TraceId beforeTraceId, String message) {

//        TraceId traceId = new TraceId();
        TraceId nextId = beforeTraceId.createNextId();//id는 그대로유지, level만 증가
        Long startTimeMs = System.currentTimeMillis();

        log.info("[{}] {}{}", nextId.getId(), addSpace(START_PREFIX, nextId.getLevel()), message);

        return new TraceStatus(nextId, startTimeMs, message);
    }

    public void end(TraceStatus status) {

        complete(status, null);
    }

    public void exception(TraceStatus status, Exception e) {

        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {

        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(),
                    resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs,
                    e.toString());
        }
    }
    private static String addSpace(String prefix, int level) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < level; i++) {

            sb.append( (i == level - 1) ? "|" + prefix : "| ");
        }
        return sb.toString();
    }
}
