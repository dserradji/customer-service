package customerservice.domain;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.io.IOException;
import java.time.LocalDate;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import customerservice.domain.enums.CustomerType;
import customerservice.domain.enums.Gender;
import customerservice.domain.enums.MaritalStatus;
import customerservice.domain.enums.PhoneType;

/**
 * Custom deserializer (JSON to Java) for the Customer object
 * <p>
 * We need this deserializer because we are using a Builder for Customer
 * <p>
 * Unknown fields will be ignored and deserialization will try to map known
 * fields and build a Customer object, if all mandatory fields are provided the
 * mapping will succeed otherwise it will fail (see unit test {@link CustomerDeserializerTest})
 *
 */
public class CustomerDeserializer extends StdDeserializer<Customer> {

	private static final long serialVersionUID = -2947593430592749197L;

	public CustomerDeserializer() {
		super(Customer.class);
	}

	protected CustomerDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Customer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		final JsonNode tree = p.getCodec().readTree(p);

		final CustomerType customerType = ofNullable(tree.findValue("customer_type"))
				.map(node -> CustomerType.valueOf(node.asText())).orElse(null);
		final Customer.Builder builder = Customer.ofType(customerType);

		ofNullable(tree.findValue("id")).ifPresent(node -> builder.withId(new ObjectId(node.asText())));
		ofNullable(tree.findValue("first_name")).ifPresent(node -> builder.withFirstName(node.asText()));
		ofNullable(tree.findValue("last_name")).ifPresent(node -> builder.withLastName(node.asText()));
		ofNullable(tree.findValue("gender")).ifPresent(node -> builder.withGender(Gender.valueOf(node.asText())));
		ofNullable(tree.findValue("birth_date"))
				.ifPresent(node -> builder.withBirthDate(LocalDate.parse(node.asText())));
		ofNullable(tree.findValue("marital_status"))
				.ifPresent(node -> builder.withMaritalStatus(MaritalStatus.valueOf(node.asText())));
		ofNullable(tree.findValue("address")).ifPresent(node -> {

			final String country = ofNullable(node.findValue("country")).map(countryNode -> countryNode.asText())
					.orElse(null);
			final Address.Builder addressBuilder = Address.ofCountry(country);

			ofNullable(node.findValue("street_number"))
					.ifPresent(addressNode -> addressBuilder.withStreetNumber(addressNode.asInt()));
			ofNullable(node.findValue("street_name"))
					.ifPresent(addressNode -> addressBuilder.withStreetName(addressNode.asText()));
			ofNullable(node.findValue("city")).ifPresent(addressNode -> addressBuilder.withCity(addressNode.asText()));
			ofNullable(node.findValue("zipcode"))
					.ifPresent(addressNode -> addressBuilder.withZipcode(addressNode.asText()));
			ofNullable(node.findValue("state_or_province"))
					.ifPresent(addressNode -> addressBuilder.withStateOrProvince(addressNode.asText()));

			builder.withAddress(addressBuilder.build());
		});

		ofNullable(tree.findValue("phones")).map(node -> node.fields()).map(entries -> {
			entries.forEachRemaining(entry -> {
				builder.withPhone(PhoneType.valueOf(entry.getKey()), entry.getValue().asText());
			});
			return empty();
		});

		ofNullable(tree.findValue("email")).ifPresent(node -> builder.withEmail(node.asText()));

		return builder.build();
	}

}
