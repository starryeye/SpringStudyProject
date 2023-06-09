package dev.practice.OpenFeign.adapter.out.web;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ExchangeRateFeign", url = "https://open.er-api.com/v6")
interface MyFeignClient {

    @GetMapping("/latest")
    ResponseExchangeRates getLatest(); //파라미터에 @RequestHeader, @RequestParam, @PathVariable 등을 사용할 수 있다.
}
