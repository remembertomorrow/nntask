package com.example.nntask;

import com.example.nntask.fixture.FixtureBuilder;
import com.example.nntask.model.entity.Wallet;
import com.example.nntask.model.request.ConvertCurrencyRequest;
import com.example.nntask.model.response.GetAccountResponse;
import com.example.nntask.service.CurrencyConversionService;
import com.example.nntask.wiremock.NbpRateStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.hamcrest.Matchers;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CurrencyConversionIntegrationTest {

    private static final String CONVERSION_API_PATH = "/conversion";

    private static final String SEED_PATH = "src/test/resources/seed.sql";

    private static final String PLN = "PLN";

    private static final String USD = "USD";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void seedDatabase() throws IOException {
        Path sqlFilePath = Path.of(SEED_PATH);
        String sql = Files.readString(sqlFilePath);
        jdbcTemplate.execute("DELETE FROM wallet");
        jdbcTemplate.execute("DELETE FROM account");
        jdbcTemplate.execute(sql);
    }


    @Test
    @DisplayName("Success - convert PLN to USD")
    void createObservedRepoSuccessPlnToUsd() throws Exception {

        ConvertCurrencyRequest request = FixtureBuilder.buildConvertCurrencyRequest(
                "PLN", "USD", UUID.fromString("a48f0ea0-2ce2-4def-87ba-54fe0904b807"));
        NbpRateStub.stubNbpCall("USD");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CONVERSION_API_PATH)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        GetAccountResponse response = objectMapper.readValue(content, GetAccountResponse.class);

        List<Wallet> wallets = response.getWallets();
        assertThat(wallets).isNotNull();

        Wallet plnWallet = chooseWalletByCurrency(wallets, PLN);
        Wallet usdWallet = chooseWalletByCurrency(wallets, USD);

        // Simply subtracting 100 from 1000 PLN available at PLN wallet = 900
        assertThat(plnWallet.getAmount(), Matchers.comparesEqualTo(BigDecimal.valueOf(900)));

        // Adding 100 PLN / 4.1388 (stubbed ask price) = 24.1616 to existing usd wallet balance of 200 = 224.1616
        assertThat(usdWallet.getAmount(), Matchers.comparesEqualTo(BigDecimal.valueOf(224.1616)));

    }


    @Test
    @DisplayName("Success - convert USD to PLN")
    void createObservedRepoSuccessUsdToPln() throws Exception {

        ConvertCurrencyRequest request = FixtureBuilder.buildConvertCurrencyRequest(
                "USD", "PLN", UUID.fromString("a48f0ea0-2ce2-4def-87ba-54fe0904b807"));
        NbpRateStub.stubNbpCall("USD");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CONVERSION_API_PATH)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        GetAccountResponse response = objectMapper.readValue(content, GetAccountResponse.class);

        List<Wallet> wallets = response.getWallets();
        assertThat(wallets).isNotNull();

        Wallet plnWallet = chooseWalletByCurrency(wallets, PLN);
        Wallet usdWallet = chooseWalletByCurrency(wallets, USD);

        // Simply subtracting 100 from 200 USD available at USD wallet = 100
        assertThat(usdWallet.getAmount(), Matchers.comparesEqualTo(BigDecimal.valueOf(100)));

        // Adding 100 USD * 4.0568 (stubbed bid price) = 405.6800 to existing pln wallet balance of 1000 = 1405.6800
        assertThat(plnWallet.getAmount(), Matchers.comparesEqualTo(BigDecimal.valueOf(1405.6800)));

    }


    private Wallet chooseWalletByCurrency(List<Wallet> wallets, String currencyCode) {
        return wallets.stream().filter(w -> w.getWalletCurrency().getCurrencyCode().equals(currencyCode))
                .toList().getFirst();
    }

    public String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}