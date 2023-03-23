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
import ru.kubsu.geocoder.service.TestService;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("geocoder")
public class GeocoderController {
    private TestService service;
    private final NominatimClient nominatimClient;

    @Autowired
    public GeocoderController(final TestService service, final NominatimClient nominatimClient) {
        this.service = service;
        this.nominatimClient = nominatimClient;
    }

    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NominatimPlace> search(final @RequestParam String address) {

        return nominatimClient.search(address)
                .map(p -> ResponseEntity.status(HttpStatus.OK)
                .body(p)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
