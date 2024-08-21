package ru.yandex.cotroller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.dto.RequestDto;
import ru.yandex.dto.ResponseDtoAfterChangeBalance;
import ru.yandex.dto.ResponseDtoGetBalance;
import ru.yandex.service.WalletService;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDtoAfterChangeBalance changeBalance(@Validated @RequestBody RequestDto requestDto) {
        log.info("Запрос с типом операции {}, ну сумму {} для кошелька под UUID: {}",
                requestDto.getOperationType(),
                requestDto.getAmount(),
                requestDto.getWalletId());
        return walletService.changeBalance(requestDto);
    }

    @GetMapping("/{walletId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDtoGetBalance getBalance(@PathVariable UUID walletId) {
        log.info("Запрос на получение баланса кошелька под UUID: {}", walletId);
        return walletService.getBalance(walletId);
    }
}
