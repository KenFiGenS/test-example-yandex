package ru.yandex.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Violation {
    private String errorCode;
    private String massage;
    private String timeStamp;
}
