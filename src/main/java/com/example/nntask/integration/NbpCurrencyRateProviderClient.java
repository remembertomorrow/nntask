package com.example.nntask.integration;

import com.example.nntask.model.response.NbpExchangeRateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "nbpFeignClient", url = "https://api.nbp.pl/api/exchangerates/rates")
public interface NbpCurrencyRateProviderClient {

    @GetMapping("/C/{code}/today/")
    NbpExchangeRateResponse getTodayExchangeRate(@PathVariable("code") String code);

}