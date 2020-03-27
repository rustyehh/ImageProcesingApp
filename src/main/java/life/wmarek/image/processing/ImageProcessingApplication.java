package life.wmarek.image.processing;

import life.wmarek.image.processing.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
@Slf4j
public class ImageProcessingApplication implements CommandLineRunner {

	private final AppConfig appConfig;

	public ImageProcessingApplication(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	public static void main(String[] args) {
		SpringApplication.run(ImageProcessingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
