package clientservice.models;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import clientservice.models.enums.ClientType;
import clientservice.models.enums.Gender;
import clientservice.models.enums.MaritalStatus;
import clientservice.models.enums.PhoneType;

public class ClientDeserializer extends StdDeserializer<Client> {

	private static final long serialVersionUID = -2947593430592749197L;

	public ClientDeserializer() {
		super(Client.class);
	}

	protected ClientDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Client deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		final JsonNode tree = p.getCodec().readTree(p);

		final ClientType clientType = Optional.ofNullable(tree.findValue("client_type"))
				.map(node -> ClientType.valueOf(node.asText())).orElse(null);
		final Client.Builder builder = Client.ofType(clientType);

		Optional.ofNullable(tree.findValue("id")).ifPresent(node -> builder.withId(new ObjectId(node.asText())));
		Optional.ofNullable(tree.findValue("first_name")).ifPresent(node -> builder.withFirstName(node.asText()));
		Optional.ofNullable(tree.findValue("last_name")).ifPresent(node -> builder.withLastName(node.asText()));
		Optional.ofNullable(tree.findValue("gender")).ifPresent(node -> builder.withGender(Gender.valueOf(node.asText())));
		Optional.ofNullable(tree.findValue("birth_date"))
				.ifPresent(node -> builder.withBirthDate(LocalDate.parse(node.asText())));
		Optional.ofNullable(tree.findValue("marital_status"))
				.ifPresent(node -> builder.withMaritalStatus(MaritalStatus.valueOf(node.asText())));
		Optional.ofNullable(tree.findValue("address")).ifPresent(node -> {

			final String country = Optional.ofNullable(node.findValue("country"))
					.map(countryNode -> countryNode.asText()).orElse(null);
			final Address.Builder addressBuilder = Address.ofCountry(country);

			Optional.ofNullable(node.findValue("street_number"))
					.ifPresent(addressNode -> addressBuilder.streetNumber(addressNode.asInt()));
			Optional.ofNullable(node.findValue("street_name"))
					.ifPresent(addressNode -> addressBuilder.streetName(addressNode.asText()));
			Optional.ofNullable(node.findValue("city"))
					.ifPresent(addressNode -> addressBuilder.city(addressNode.asText()));
			Optional.ofNullable(node.findValue("zipcode"))
					.ifPresent(addressNode -> addressBuilder.zipcode(addressNode.asText()));
			Optional.ofNullable(node.findValue("state_or_province"))
					.ifPresent(addressNode -> addressBuilder.stateOrProvince(addressNode.asText()));

			builder.withAddress(addressBuilder.build());
		});

		Optional.ofNullable(tree.findValue("phones")).map(node -> node.fields()).map(entries -> {
			entries.forEachRemaining(entry -> {
				builder.withPhone(PhoneType.valueOf(entry.getKey()), entry.getValue().asText());
			});
			return Optional.empty();
		});

		Optional.ofNullable(tree.findValue("email")).ifPresent(node -> builder.withEmail(node.asText()));

		return builder.build();
	}

}
