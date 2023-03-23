package ru.kubsu.geocoder.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kubsu.geocoder.client.NominatimClient;
import ru.kubsu.geocoder.dto.NominatimPlace;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GeocoderControllerTest {

    @LocalServerPort
    Integer PORT;

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @MockBean
    private NominatimClient nominatimClient;

    @Test
    void search() {
        final NominatimPlace testPlace = buildTestPlace();
        when(nominatimClient.search(anyString()))
                .thenReturn(Optional.of(testPlace));

        ResponseEntity<NominatimPlace> response = testRestTemplate.
                getForEntity("http://localhost:" + PORT + "/geocoder/search?address=кубгу", NominatimPlace.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final NominatimPlace body = response.getBody();
        assertEquals(testPlace, body);
    }

    @Test
    void searchWhenNominatimNotResponse() {
        when(nominatimClient.search(anyString())).thenReturn(Optional.empty());

        ResponseEntity<NominatimPlace> response = testRestTemplate.
                getForEntity("http://localhost:" + PORT + "/geocoder/search?address=кубгу", NominatimPlace.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        final NominatimPlace body = response.getBody();
        assertNull(response.getBody());
    }

    private static NominatimPlace buildTestPlace() {
        return new NominatimPlace (45.12345678, 35.12345678912345, "Кубанский государственный университет", "university");
    }

}