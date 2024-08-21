package ru.yandex.dto;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    @NotNull(message = "Поле с идентификатором кошелька не может быть пустым.")
    private UUID walletId;
    @NotNull(message = "Укажите тип операции.")
    private OperationType operationType;
    @Positive(message = "Неверно задана сумма операции.")
    private double amount;
}
