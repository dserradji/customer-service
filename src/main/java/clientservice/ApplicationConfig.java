package clientservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
public class ApplicationConfig {

	/* Serialize dates to ISO-8601 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizeObjectMapper() {
		return new Jackson2ObjectMapperBuilderCustomizer() {

			@Override
			public void customize(Jackson2ObjectMapperBuilder builder) {
				builder.modules(new JavaTimeModule());
				builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			}};
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationConfig.class, args);
	}
}
