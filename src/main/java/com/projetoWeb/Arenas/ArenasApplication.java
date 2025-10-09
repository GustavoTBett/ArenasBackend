package com.projetoWeb.Arenas;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArenasApplication {

	public static void main(String[] args) {
        try {
            throw new Exception("This is a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
		SpringApplication.run(ArenasApplication.class, args);
	}

}
