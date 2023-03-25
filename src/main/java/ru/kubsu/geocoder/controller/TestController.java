package ru.kubsu.geocoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kubsu.geocoder.client.NominatimClient;
import ru.kubsu.geocoder.dto.NominatimPlace;
import ru.kubsu.geocoder.model.Test;
import ru.kubsu.geocoder.service.TestService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 *  Тестовый контроллер.
 */
@RestController
@RequestMapping("tests")
public class TestController {

    private final TestService service;
    private final NominatimClient nominatimClient;

    @Autowired
    public TestController(final TestService service, final NominatimClient nominatimClient) {
        this.service = service;
        this.nominatimClient = nominatimClient;
    }

    @GetMapping(value = "/check/{id}", produces = APPLICATION_JSON_VALUE)
    public Test getTest(@PathVariable final Integer id, @RequestParam final String name) {
        return service.build(id, name);
    }

    @GetMapping(value = "/save", produces = APPLICATION_JSON_VALUE)
    public void save(@RequestParam final String name) {
        service.save(name);
    }

    @GetMapping(value = "/load/{name}", produces = APPLICATION_JSON_VALUE)
    public Test load(@PathVariable final String name) {
        return service.load(name);
    }

    @GetMapping(value = "/test", produces = APPLICATION_JSON_VALUE)
    public NominatimPlace test() {
        return nominatimClient.search("кубгу", "json").get(0);
    }
}
