package clientservice.restapi;

import static clientservice.models.enums.ClientType.COMPANY;
import static clientservice.models.enums.ClientType.PERSON;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
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
			.andExpect(status().isOk())
			.andExpect(content().contentType(APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$..clientType").isArray())
			.andExpect(jsonPath("$..clientType").value(hasItems(PERSON.toString(), COMPANY.toString())));
	}

	@Test
	public void shouldReturnNoClients() throws Exception {

		given(repo.findAll()).willReturn(Collections.emptyList());

		mvc.perform(get("/clients").accept(APPLICATION_JSON_UTF8)).andExpect(status().isNoContent());
	}
}
