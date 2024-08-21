package ru.yandex.service;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.dto.OperationType;
import ru.yandex.dto.RequestDto;
import ru.yandex.dto.ResponseDtoAfterChangeBalance;
import ru.yandex.dto.ResponseDtoGetBalance;
import ru.yandex.exception.DataNotFound;
import ru.yandex.model.Wallet;
import ru.yandex.repository.WalletRepository;

import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseDtoAfterChangeBalance changeBalance(RequestDto requestDto) {
        Wallet currentWallet = walletRepository.getReferenceById(requestDto.getWalletId());
        Wallet currentWalletAfterChangeBalance = null;
        try {
            if (requestDto.getOperationType().equals(OperationType.DEPOSIT)) {
                currentWallet.setBalance(currentWallet.getBalance() + requestDto.getAmount());
                currentWalletAfterChangeBalance = walletRepository.save(currentWallet);
            } else if (requestDto.getOperationType().equals(OperationType.WITHDRAW)) {
                if (currentWallet.getBalance() < requestDto.getAmount()) {
                    throw new IllegalArgumentException("Недостаточно средств на счёте");
                }
                currentWallet.setBalance(currentWallet.getBalance() - requestDto.getAmount());
                currentWalletAfterChangeBalance = walletRepository.save(currentWallet);
            }
        } catch (EntityNotFoundException e) {
            throw new DataNotFound("Кошелёк с идентификатором " + requestDto.getWalletId() + " не найден.");
        }
        return convertToResponseDtoAfterChangeBalance(currentWalletAfterChangeBalance);
    }

    @Override
    public ResponseDtoGetBalance getBalance(UUID walletId) {
        Wallet currentWallet = walletRepository.getReferenceById(walletId);
        try {
            currentWallet.getBalance();
            return convertToResponseDtoGetBalance(currentWallet);
        } catch (EntityNotFoundException e) {
            throw new DataNotFound("Кошелёк с идентификатором " + walletId + " не найден.");
        }
    }

    private ResponseDtoAfterChangeBalance convertToResponseDtoAfterChangeBalance(Wallet wallet) {
        return modelMapper.map(wallet, ResponseDtoAfterChangeBalance.class);
    }

    private ResponseDtoGetBalance convertToResponseDtoGetBalance(Wallet wallet) {
        return modelMapper.map(wallet, ResponseDtoGetBalance.class);
    }
}
