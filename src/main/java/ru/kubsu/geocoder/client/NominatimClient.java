/**
 * Copyright 2023 Andrey Shilnikov
 */

package ru.kubsu.geocoder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kubsu.geocoder.dto.NominatimPlace;

import java.util.List;
import java.util.Optional;

/**
 * @author Andrey Shilnikov
 */
@FeignClient(value = "nominatim", url = "https://nominatim.openstreetmap.org")
public interface NominatimClient {

        String JSON_FORMAT = "json";

        @RequestMapping(method = RequestMethod.GET, value = "/search", produces = "application/json")
        List<NominatimPlace> search(@RequestParam("q") String query, @RequestParam("format") String format);

        /**
         * Поиск объекта на карте по адресу (Возвращается самый релевантный)
         *
         * @param query Строка поиска
         * @return Объект адреса
         */
        default Optional<NominatimPlace> search(String query) {
                try {
                        return Optional.of(search(query, JSON_FORMAT).get(0));
                } catch (Exception ex) {
                        return Optional.empty();
                }
        }

}
