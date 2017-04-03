package clientservice;

import static clientservice.models.enums.ClientType.PERSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Month;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.JsonNode;

import clientservice.models.Client;

/**
 * Top to bottom integration test.
 * <p>
 * The specified server will be started and the service will be available with a
 * random port number.
 * <p>
 * This class tests all CRUD operation in one big test.
 * 
 * @author Djallal Serradji
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ClientServiceTest {

	@Autowired
	private TestRestTemplate restTemplate;

	/**
	 * This is necessary for the self signed certificate to be trusted.<br>
	 * It has no effect if SSL is not active.
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws IOException
	 */
	@PostConstruct
	void init() throws Exception {

		final SSLContextBuilder ctxBuilder = SSLContextBuilder.create();
		final SSLContext sslCtx = ctxBuilder.loadTrustMaterial(new TrustSelfSignedStrategy()).build();
		final SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslCtx);
		final HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(factory).build();

		((HttpComponentsClientHttpRequestFactory) restTemplate.getRestTemplate().getRequestFactory())
				.setHttpClient(httpClient);
	}

	@Test
	public void testCRUDOperationsAllTogether() throws IOException {

		// Prepare HTTP headers used for requests
		final HttpHeaders headers = new HttpHeaders();
		headers.add(ACCEPT, APPLICATION_JSON_UTF8_VALUE);
		headers.add(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE);
		headers.add("Authorization", String.format("Bearer %s", requestToken()));

		final Client newClient = Client.ofType(PERSON)
				.birthDate(LocalDate.of(1990, Month.AUGUST, 16))
				.build();

		// ---------- Create ----------
		HttpEntity<?> request = new HttpEntity<>(newClient, headers);
		ResponseEntity<Client> response = restTemplate.exchange("/clients", POST, request, Client.class);

		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		final String newClientUrl = response.getHeaders().get("Location").get(0);
		assertThat(newClientUrl).contains("/clients/");

		// ---------- Read ----------
		request = new HttpEntity<>(headers);
		response = restTemplate.exchange(newClientUrl, GET, request, Client.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getId()).isNotNull();

		// ---------- Update ----------
		final Client createdClient = response.getBody();
		final Client updatedClient = Client.from(createdClient).firstName("John").lastName("Doe").build();
		request = new HttpEntity<>(updatedClient, headers);
		response = restTemplate.exchange(newClientUrl, PUT, request, Client.class);

		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
		assertThat(response.getBody()).isNull();
		response = restTemplate.exchange(newClientUrl, GET, request, Client.class);
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody().getId()).isEqualTo(updatedClient.getId());
		assertThat(response.getBody().getLastName()).isEqualTo("Doe");

		// ---------- Delete ----------
		response = restTemplate.exchange(newClientUrl, DELETE, request, Client.class);

		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
		assertThat(response.getBody()).isNull();
		response = restTemplate.exchange(newClientUrl, GET, request, Client.class);
		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
	}

	private String requestToken() throws IOException {

		final MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
		postParams.add("grant_type", "client_credentials");

		final JsonNode resp = restTemplate
				.withBasicAuth("clientId", "clientSecret")
				.postForObject("/oauth/token", postParams, JsonNode.class);

		return resp.get("access_token").asText();
	}
}
