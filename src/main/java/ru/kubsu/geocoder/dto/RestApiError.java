package ru.kubsu.geocoder.dto;

import feign.Param;

/**
 *  DTO Для ошибки.
 *
 * @param error Ошибка
 * @param status Статус
 * @param path Путь
 */
public record RestApiError(
    @Param
    Integer status,
    @Param
    String error,
    @Param
    String path
) {
    RestApiError() {
        this(0, "", "");
    }
}
