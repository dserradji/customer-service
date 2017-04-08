package clientservice;

import org.bson.types.ObjectId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@SpringBootApplication
public class ClientService {

	/* Customize the Java <--> JSON mapper */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizeObjectMapper() {
		return new Jackson2ObjectMapperBuilderCustomizer() {

			@Override
			public void customize(Jackson2ObjectMapperBuilder builder) {
				/* Output ObjectId as a String (toString()) not as an object */
				builder.serializerByType(ObjectId.class, new ToStringSerializer());
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ClientService.class, args);
	}
}
