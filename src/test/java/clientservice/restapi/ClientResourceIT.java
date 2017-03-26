package clientservice.restapi;

import static clientservice.models.enums.ClientType.COMPANY;
import static clientservice.models.enums.ClientType.PERSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import clientservice.models.Client;
import clientservice.repositories.mongodb.ClientRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(ClientResource.class)
public class ClientResourceIT {

	@MockBean
	private ClientRepository repo;

	@Autowired
	private MockMvc mvc;

	@Test
	public void shouldReturnAllClients() throws Exception {

		final List<Client> clients = Arrays.asList(
				Client.ofType(PERSON).build(), 
				Client.ofType(COMPANY).build());
		
		given(repo.findAll()).willReturn(clients);

		mvc.perform(get("/clients").accept(APPLICATION_JSON_UTF8))
			.andExpect(status().isOk())	// HTTP 200
			.andExpect(content().contentType(APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$..clientType").isArray())
			.andExpect(jsonPath("$..clientType").value(hasItems(PERSON.toString(), COMPANY.toString())));
	}

	@Test
	public void shouldReturnEmptyBodyWhenNoClients() throws Exception {

		given(repo.findAll()).willReturn(Collections.emptyList());

		// Expect HTTP 204
		mvc.perform(get("/clients").accept(APPLICATION_JSON_UTF8)).andExpect(status().isNoContent());
	}
	
	@Test
	public void shouldAddANewClient() throws Exception {
		
		final Client newClient = Client.ofType(PERSON).build();

		final ObjectId id = new ObjectId(1000, 2000, (short)1, 5000);
		ReflectionTestUtils.setField(newClient, "id", id);
		
		given(repo.save(any(Client.class))).willReturn(newClient);
		
		mvc.perform(post("/clients").contentType(APPLICATION_JSON_UTF8).content("{\"clientType\":\"PERSON\"}"))
			.andExpect(status().isCreated())	// HTTP 201
			.andExpect(header().string("Location", is(equalTo(String.format("/clients/%s", id)))));
	}
	
	@Test
	public void shouldNotAddClientIfContentIsNotValid() throws Exception {
		
		final String BAD_JSON = "{\"bad_property\":\"PERSON\"}";
		mvc.perform(post("/clients").contentType(APPLICATION_JSON_UTF8).content(BAD_JSON))
			.andExpect(status().isBadRequest());	// HTTP 400
	}
	
	@Test
	public void shouldNotAddClientIfClientAlreadyExists() throws Exception {
		
		given(repo.exists(any(ObjectId.class))).willReturn(true);
		final ObjectId id = new ObjectId(1000, 2000, (short)1, 5000);
		
		final String UPDATE = String.format("{\"id\":\"%s\",\"clientType\":\"COMPANY\"}", id);
		mvc.perform(post("/clients").contentType(APPLICATION_JSON_UTF8).content(UPDATE))
			.andExpect(status().isBadRequest());	// HTTP 400
	}
	
	@Test
	public void shouldUpdateAnExistingClient() throws Exception {
		
//		final String PATCH = "{\"firstName\":\"John\", \"lastName\":\"Doe\"}";
//		final ObjectId id = new ObjectId(1000, 2000, (short)1, 5000);
//		
//		mvc.perform(patch(String.format("/clients/%s", id)).contentType(APPLICATION_JSON_UTF8).content(PATCH))
//			.andExpect(status().isOk());
	}
}
