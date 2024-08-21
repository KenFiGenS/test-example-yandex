package ru.yandex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.model.Wallet;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
