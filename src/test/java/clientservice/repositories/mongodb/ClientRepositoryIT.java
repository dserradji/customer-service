package clientservice.repositories.mongodb;

import static clientservice.models.enums.ClientType.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import clientservice.ApplicationConfig;
import clientservice.models.Address;
import clientservice.models.Client;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class ClientRepositoryIT {

	@Autowired
	private ClientRepository repo;

	@After
	public void cleanDB() {
		repo.deleteAll();
	}
	
	@Test
	public void shouldCreateAPersonWithId() {

		// Given
		final Address address = Address.ofCountry("Shadaloo").streetNumber(110).streetName("Bison street")
				.city("Shadaloo City").zipcode("123456").build();

		// When
		final Client saved = repo
				.save(Client.ofType(PERSON).firstName("Ken").lastName("Masters").address(address).build());

		// Then
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getFirstName()).isEqualTo("Ken");
		assertThat(saved.getAddress().getCountry()).isEqualTo("Shadaloo");
	}

	@Test
	public void shouldReadAPersonWithId() {

		// Given
		final Address address = Address.ofCountry("Shadaloo").streetNumber(110).streetName("Bison street")
				.city("Shadaloo City").zipcode("123456").build();
		final Client saved = repo
				.save(Client.ofType(PERSON).firstName("Ken").lastName("Masters").address(address).build());

		// When
		final Client retrieved = repo.findOne(saved.getId());

		// Then
		assertThat(retrieved).isNotNull();
		assertThat(retrieved.getId()).isEqualTo(saved.getId());
		assertThat(retrieved.getFirstName()).isEqualTo(saved.getFirstName());
		assertThat(retrieved.getAddress().getCountry()).isEqualTo("Shadaloo");
	}

	@Test
	public void shouldUpdateAPerson() {

		// Given
		Address address = Address.ofCountry("Shadaloo").streetNumber(110).streetName("Bison street")
				.city("Shadaloo City").zipcode("123456").build();
		final Client saved = repo
				.save(Client.ofType(PERSON).firstName("Ken").lastName("Masters").address(address).build());
		Client retrieved = repo.findOne(saved.getId());
		address = Address.from(address).zipcode("654321").build();
		retrieved = Client.from(retrieved).email("kenm@email.com").address(address).build();

		// When
		final Client updated = repo.save(retrieved);

		// Then
		assertThat(updated.getId()).isEqualTo(retrieved.getId());
		assertThat(updated.getEmail()).isEqualTo("kenm@email.com");
		assertThat(updated.getAddress().getZipcode()).isEqualTo("654321");
	}

	@Test
	public void shouldDeleteAPerson() {

		// Given
		final Client saved = repo.save(Client.ofType(PERSON).firstName("Ken").lastName("Masters").build());

		// When
		repo.delete(saved);
		boolean exists = repo.exists(saved.getId());

		// Then
		assertThat(exists).isFalse();
	}

	@Test
	public void shouldReturnAllClients() {

		// Given
		final Address address = Address.ofCountry("Shadaloo").streetNumber(110).streetName("Bison street")
				.city("Shadaloo City").zipcode("123456").build();
		repo.save(Client.ofType(PERSON).firstName("Ken").lastName("Masters").address(address).build());
		repo.save(Client.ofType(COMPANY).firstName("Ken").lastName("Masters").address(address).build());

		// When
		final Iterable<Client> clients = repo.findAll();

		// Then
		assertThat(clients).isNotNull();
		assertThat(clients.iterator()).hasSize(2);
	}

	@Test
	public void shouldReturnNoClients() {

		// Given

		// When
		final Iterable<Client> clients = repo.findAll();

		// Then
		assertThat(clients).isNotNull();
		assertThat(clients.iterator()).hasSize(0);
	}
}
