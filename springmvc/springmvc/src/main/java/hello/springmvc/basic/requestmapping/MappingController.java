package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    // HTTP 메서드 모두 허용 get, head, post, put, patch, delete
    @RequestMapping("/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }

    //method 요소를 통해 get 메서드만 허용
    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mapppingGetV1");
        return "ok";
    }

    //축약 에노테이션
    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2(HttpServletRequest request) {
        log.info("mapping-get-v2");
        log.info("{}", request.getRequestURI());
        return "ok";
    }

    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("mappingPath userId={}", data);
        return "ok";
    }

    //다중
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

    //추가 매핑 - > 잘 사용 안함
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    //특정 헤더 추가 매핑
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }

    //content type  헤더 매핑
    @PostMapping(value = "/mapping-consume)", consumes = "application/json")
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    //accept 헤더 매핑
    @PostMapping(value = "/mapping-produce", produces = "/text/html")
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
}
