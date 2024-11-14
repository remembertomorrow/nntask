package com.example.nntask.wiremock;

import com.example.nntask.model.Rate;
import com.example.nntask.model.response.NbpExchangeRateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class NbpRateStub {
    public static void stubNbpCall(String code) throws JsonProcessingException {
        stubFor(get(urlEqualTo("/C/" + code + "/today/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildMockResponse(code))));
    }

    private static String buildMockResponse(String code) throws JsonProcessingException {

        NbpExchangeRateResponse response = new NbpExchangeRateResponse();
        response.setCode(code);
        Rate rate = new Rate();
        rate.setBid(4.0568);
        rate.setAsk(4.1388);
        response.setRates(Collections.singletonList(rate));

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(response);
    }

}
