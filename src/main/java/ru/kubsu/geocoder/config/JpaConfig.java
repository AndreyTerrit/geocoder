package ru.kubsu.geocoder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "ru.kubsu.geocoder")
public class JpaConfig {
}
