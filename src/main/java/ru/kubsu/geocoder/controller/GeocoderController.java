package ru.kubsu.geocoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kubsu.geocoder.client.NominatimClient;
import ru.kubsu.geocoder.dto.NominatimPlace;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 *  Контроллер для парсинга запросов на Nominatim.
 */
@RestController
@RequestMapping("geocoder")
public class GeocoderController {
    private final NominatimClient nominatimClient;

    @Autowired
    public GeocoderController(final NominatimClient nominatimClient) {
        this.nominatimClient = nominatimClient;
    }

    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NominatimPlace> search(final @RequestParam String address) {

        return nominatimClient.search(address)
                .map(p -> ResponseEntity.status(HttpStatus.OK)
                .body(p)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping(value = "/reverse", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NominatimPlace> reverse(final @RequestParam Double lat, final @RequestParam Double lon) {

        return nominatimClient.reverse(lat, lon)
            .map(p -> ResponseEntity.status(HttpStatus.OK)
                .body(p)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
