package it.demo.twitterlike.server;

import it.demo.twitterlike.server.config.Constants;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.SimpleCommandLinePropertySource;

@SpringBootApplication
public class Application {

	public static void main(String... args) {

		SpringApplication app = new SpringApplication(Application.class);

		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(
				args);

		// Check if the selected profile has been set as argument.
		// if not the development profile will be added
		addDefaultProfile(app, source);

		app.run(args);

	}

	/**
	 * Set a default profile if it has not been set
	 */
	private static void addDefaultProfile(SpringApplication app,
			SimpleCommandLinePropertySource source) {
		if (!source.containsProperty("spring.profiles.active")) {
			app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
		}
	}
}