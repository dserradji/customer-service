package clientservice.repositories.mongodb;

import static clientservice.models.enums.ClientType.PERSON;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import clientservice.ApplicationConfig;
import clientservice.models.Client;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class ClientRepositoryTest {

	@Autowired
	ClientRepository repo;

	@Test
	public void shouldInsertAPersonAndCreateItsId() {

		// Given

		// When
		final Client saved = repo.save(Client.ofType(PERSON).withFirstName("John").withLastName("Doe").build());

		// Then
		assertThat(saved.getId(), is(notNullValue()));
		assertThat(saved.getFirstName(), is(equalTo("John")));
	}
}
