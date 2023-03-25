package ru.kubsu.geocoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main app class.
 */

@SuppressWarnings({"PMD.UseUtilityClass", "HideUtilityClassConstructor"})
@SpringBootApplication
public class GeocoderApplication {
    public static void main(final String[] args) {
        SpringApplication.run(GeocoderApplication.class, args);
    }
}
