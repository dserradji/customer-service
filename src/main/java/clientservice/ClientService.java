package clientservice;

import org.bson.types.ObjectId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
public class ClientService {

	/* Customize the Java <--> JSON mapper */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizeObjectMapper() {
		return new Jackson2ObjectMapperBuilderCustomizer() {

			@Override
			public void customize(Jackson2ObjectMapperBuilder builder) {
				
				/* Serialize dates to ISO-8601 */
				builder.modules(new JavaTimeModule());
				builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				
				/* Use snake_case naming convention */
				builder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
				
				/* Output ObjectId as a String (toString()) not as an object */
				builder.serializerByType(ObjectId.class, new ToStringSerializer());
				
				/* Don't output null fields*/
				builder.serializationInclusion(Include.NON_NULL);
				
				/* Print pretty JSON */
				builder.indentOutput(true);
			}};
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ClientService.class, args);
	}
}
