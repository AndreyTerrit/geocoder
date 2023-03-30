package ru.kubsu.geocoder.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kubsu.geocoder.client.NominatimClient;
import ru.kubsu.geocoder.dto.NominatimPlace;
import ru.kubsu.geocoder.model.Address;
import ru.kubsu.geocoder.repository.AddressRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
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

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
    }

    @Test
    void search() {
        final String query = "кубгу";
        final Address testAddress = buildTestAddress(query);
        when(nominatimClient.search(anyString()))
                .thenReturn(Optional.of(buildTestPlace()));

        ResponseEntity<Address> response = testRestTemplate.
                getForEntity("http://localhost:" + PORT + "/geocoder/search?query=" + query, Address.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final Address body = response.getBody();
        assertEquals(testAddress, body);
    }

    @Test
    void searchWhenNominatimNotResponse() {
        final String query = "кубгу";
        when(nominatimClient.search(anyString()))
            .thenReturn(Optional.empty());

        ResponseEntity<Address> response = testRestTemplate.
            getForEntity(
                "http://localhost:" + PORT + "/geocoder/search?query=" + query,
                Address.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void reverse() {
        final Double lat = 45.12345678;
        final Double lon = 35.12345678912345;
        final Address testAddress = buildTestAddress(lat, lon);
        when(nominatimClient.reverse(anyDouble(), anyDouble()))
            .thenReturn(Optional.of(buildTestPlace()));

        ResponseEntity<Address> response = testRestTemplate.
            getForEntity("http://localhost:" + PORT + "/geocoder/reverse?lat=" + lat + "&lon=" + lon, Address.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final Address body = response.getBody();
        assertEquals(testAddress, body);
    }

    @Test
    void reverseWhenNominatimNotResponse() {
        final Double lat = 45.12345678;
        final Double lon = 35.12345678912345;
        when(nominatimClient.reverse(anyDouble(), anyDouble()))
            .thenReturn(Optional.empty());

        ResponseEntity<Address> response = testRestTemplate.
            getForEntity(
                "http://localhost:" + PORT + "/geocoder/reverse?lat=" + lat + "&lon=" + lon,
                Address.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    private static NominatimPlace buildTestPlace() {
        return new NominatimPlace (45.12345678, 35.12345678912345, "Кубанский государственный университет", "university");
    }

    private static Address buildTestAddress(final String query) {
        return Address.of(buildTestPlace(), query);
    }

    private static Address buildTestAddress(final Double lat, final Double lon) {
        return Address.ofCoordinates(buildTestPlace(), lat, lon);
    }
}
