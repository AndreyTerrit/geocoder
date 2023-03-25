package ru.kubsu.geocoder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 *  Класс-модель для возвращаемых Nominatim данных.
 *
 * @param displayName Наименование объекта
 * @param longitude  Долгота
 * @param latitude Широта
 * @param type Тип
 */
public record NominatimPlace(
    @JsonProperty("lat")
    Double latitude,
    @JsonProperty("lon")
    Double longitude,
    @JsonProperty("display_name")
    String displayName,
    @JsonProperty("type")
    String type
) {
    public NominatimPlace() {
        this(0.0, 0.0, "", "");
    }
}

