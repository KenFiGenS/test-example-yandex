package ru.yandex.service;

import ru.yandex.dto.RequestDto;
import ru.yandex.dto.ResponseDtoAfterChangeBalance;
import ru.yandex.dto.ResponseDtoGetBalance;

import java.util.UUID;

public interface WalletService {
    ResponseDtoAfterChangeBalance changeBalance(RequestDto requestDto);

    ResponseDtoGetBalance getBalance(UUID walletId);
}
