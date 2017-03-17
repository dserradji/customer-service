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
	public void shouldCreateAPersonWithId() {

		// Given

		// When
		final Client saved = repo.save(Client.ofType(PERSON).withFirstName("John").withLastName("Doe").build());

		// Then
		assertThat(saved.getId(), is(notNullValue()));
		assertThat(saved.getFirstName(), is(equalTo("John")));
	}

	@Test
	public void shouldReadAPersonWithId() {

		// Given
		final Client saved = repo.save(Client.ofType(PERSON).withFirstName("John").withLastName("Doe").build());

		// When
		final Client retrieved = repo.findOne(saved.getId());

		// Then
		assertThat(retrieved, is(notNullValue()));
		assertThat(retrieved.getId(), is(equalTo(saved.getId())));
		assertThat(retrieved.getFirstName(), is(equalTo(saved.getFirstName())));
	}

	@Test
	public void shouldUpdateAPerson() {

		// Given
		final Client saved = repo.save(Client.ofType(PERSON).withFirstName("John").withLastName("Doe").build());
		final Client retrieved = repo.findOne(saved.getId());
		retrieved.setEmail("johnd@email.com");

		// When
		final Client updated = repo.save(retrieved);

		// Then
		assertThat(updated.getId(), is(equalTo(retrieved.getId())));
		assertThat(updated.getEmail(), is(equalTo("johnd@email.com")));
	}

	@Test
	public void shouldDeleteAPerson() {

		// Given
		final Client saved = repo.save(Client.ofType(PERSON).withFirstName("John").withLastName("Doe").build());

		// When
		repo.delete(saved);
		final Client retrieved = repo.findOne(saved.getId());

		// Then
		assertThat(retrieved, is(nullValue()));
	}
}
