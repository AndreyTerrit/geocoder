package ru.kubsu.geocoder.dto;

import java.util.Objects;

public record RestApiError (
    Integer status,
    String error,
    String path
) {
    RestApiError() {
        this(0, "", "");
    }
}
