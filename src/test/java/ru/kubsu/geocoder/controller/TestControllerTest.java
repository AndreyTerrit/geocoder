package ru.kubsu.geocoder.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kubsu.geocoder.dto.RestApiError;
import ru.kubsu.geocoder.repository.TestRepository;
import ru.kubsu.geocoder.util.TestUtil;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestControllerTest {

    private TestRepository repository;
    @Autowired
    public TestControllerTest(TestRepository repository) {
        this.repository = repository;
    }
    @LocalServerPort
    Integer PORT;

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @BeforeEach
    void setUp() {
        System.out.println("BeforeEach");
    }

    @AfterEach
    void tearDown() {
        System.out.println("AfterEach");
    }

    @Test
    void integrationTest() {
        ResponseEntity<ru.kubsu.geocoder.model.Test> response = testRestTemplate.
                getForEntity("http://localhost:" + PORT + "/tests/check/1?name=test", ru.kubsu.geocoder.model.Test.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final ru.kubsu.geocoder.model.Test body = response.getBody();
        assertEquals(1, body.getId());
        assertEquals("test", body.getName());
        assertEquals(null, body.getMark());
        assertEquals(null, body.getDone());
    }

    @Test
    void integrationTestWhenNameIsNull() {
        ResponseEntity<Map<String, String>> response = testRestTemplate.
                exchange("http://localhost:" + PORT + "/tests/check/1", HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, String>>() {});

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        final Map<String, String> body = response.getBody();
        System.out.println(body);
        assertEquals("400", body.get("status"));
        assertEquals("Bad Request", body.get("error"));
        assertEquals("/tests/check/1", body.get("path"));
    }

    @Test
    void integrationTestWhenIdIsString() {
        ResponseEntity<RestApiError> response = testRestTemplate.
                exchange("http://localhost:" + PORT + "/tests/check/abc?name=test", HttpMethod.GET, null, RestApiError.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        final RestApiError body = response.getBody();
        System.out.println(body);
        assertEquals(400, body.getStatus());
        assertEquals("Bad Request", body.getError());
        assertEquals("/tests/check/abc", body.getPath());
    }

    // New tests

    @Test
    void saveMethodIntegrationTest() {
      ResponseEntity<ru.kubsu.geocoder.model.Test> response = testRestTemplate.
        getForEntity("http://localhost:" + PORT + "/tests/save/?name=test", ru.kubsu.geocoder.model.Test.class);

      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals("test", this.repository.findByName("test").get().getName());
    }

    @Test
    void saveMethodIntegrationTestWithoutQueryParam() {
        ResponseEntity<ru.kubsu.geocoder.model.Test> response = testRestTemplate.
            getForEntity("http://localhost:" + PORT + "/tests/save", ru.kubsu.geocoder.model.Test.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void loadMethodIntegrationTest() {
        ResponseEntity<ru.kubsu.geocoder.model.Test> response = testRestTemplate.
            getForEntity("http://localhost:" + PORT + "/tests/load/test", ru.kubsu.geocoder.model.Test.class);


        final ru.kubsu.geocoder.model.Test body = response.getBody();
        assertEquals("test", body.getName());
        assertEquals(null, body.getMark());
        assertEquals(null, body.getDone());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void loadMethodIntegrationTestWithoutName() {
        ResponseEntity<ru.kubsu.geocoder.model.Test> response = testRestTemplate.
            getForEntity("http://localhost:" + PORT + "/tests/load", ru.kubsu.geocoder.model.Test.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll");

    }

    @AfterAll
    static void afterAll() {
        System.out.println("afterAll");
    }
}
