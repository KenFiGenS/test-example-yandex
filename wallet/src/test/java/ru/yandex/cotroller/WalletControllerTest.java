package ru.yandex.cotroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.dto.RequestDto;
import ru.yandex.dto.ResponseDtoAfterChangeBalance;
import ru.yandex.dto.ResponseDtoGetBalance;
import ru.yandex.service.WalletService;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = WalletController.class)
class WalletControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private WalletService walletService;
    @Autowired
    private MockMvc mvc;

    private final EasyRandom generator = new EasyRandom();

    @Test
    @SneakyThrows
    void changeBalanceTest() {
        RequestDto requestDto = generator.nextObject(RequestDto.class);

        when(walletService.changeBalance(Mockito.any(RequestDto.class)))
                .thenAnswer(invocationOnMock -> new ResponseDtoAfterChangeBalance(requestDto.getWalletId(), 9950));

        mvc.perform(post("/api/v1/wallet")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(requestDto.getWalletId().toString()))
                .andExpect(jsonPath("$.balance", is(9950.0), Double.class));
    }

    @Test
    @SneakyThrows
    void changeBalanceTestWrongAmount() {
        RequestDto requestDto = generator.nextObject(RequestDto.class);
        requestDto.setAmount(-150);

        mvc.perform(post("/api/v1/wallet")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void changeBalanceTestWrongOperationType() {
        RequestDto requestDto = generator.nextObject(RequestDto.class);
        requestDto.setOperationType(null);

        mvc.perform(post("/api/v1/wallet")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void changeBalanceTestWrongUUID() {
        RequestDto requestDto = generator.nextObject(RequestDto.class);
        requestDto.setWalletId(null);

        mvc.perform(post("/api/v1/wallet")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void getBalance() {
        RequestDto requestDto = generator.nextObject(RequestDto.class);
        UUID uuid = requestDto.getWalletId();

        when(walletService.getBalance(Mockito.any(UUID.class)))
                .thenAnswer(invocationOnMock -> new ResponseDtoGetBalance(9950.0));

        mvc.perform(get("/api/v1/wallet/" + uuid.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(9950.0), Double.class));
    }

    @Test
    @SneakyThrows
    void getBalanceWrongUUID() {

        mvc.perform(get("/api/v1/wallet/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}