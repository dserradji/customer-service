package clientservice.repositories.mongodb;

import static clientservice.models.enums.ClientType.COMPANY;
import static clientservice.models.enums.ClientType.PERSON;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import clientservice.ClientService;
import clientservice.models.Address;
import clientservice.models.Client;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ClientService.class)
public class ClientRepositoryTest {

	@Autowired
	private ClientRepository repo;

	/**
	 * Class level @DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
	 * annotation can be used instead of this method to reset the context for
	 * each test but execution will be much slower.
	 */
	@Before
	public void cleanDB() {
		repo.deleteAll().block();
	}

	@Test
	public void shouldCreateAPerson() {

		// Given
		final Address address = Address.ofCountry("Shadaloo").withStreetNumber(110).withStreetName("Bison street")
				.withCity("Shadaloo City").withZipcode("123456").build();

		// When
		final Client saved = repo
				.save(Client.ofType(PERSON).withFirstName("Ken").withLastName("Masters").withAddress(address).build())
				.block();

		// Then
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getFirstName()).isEqualTo("Ken");
		assertThat(saved.getAddress().getCountry()).isEqualTo("Shadaloo");
	}

	@Test
	public void shouldFindAPersonWithItsId() {

		// Given
		final Address address = Address.ofCountry("Shadaloo").withStreetNumber(110).withStreetName("Bison street")
				.withCity("Shadaloo City").withZipcode("123456").build();
		final Client saved = repo
				.save(Client.ofType(PERSON).withFirstName("Ken").withLastName("Masters").withAddress(address).build())
				.block();

		// When
		final Client retrieved = repo.findOne(saved.getId()).block();

		// Then
		assertThat(retrieved).isNotNull();
		assertThat(retrieved.getId()).isEqualTo(saved.getId());
		assertThat(retrieved.getFirstName()).isEqualTo(saved.getFirstName());
		assertThat(retrieved.getAddress().getCountry()).isEqualTo("Shadaloo");
	}

	@Test
	public void shouldUpdateAPerson() {

		// Given
		Address address = Address.ofCountry("Shadaloo").withStreetNumber(110).withStreetName("Bison street")
				.withCity("Shadaloo City").withZipcode("123456").build();
		final Client saved = repo
				.save(Client.ofType(PERSON).withFirstName("Ken").withLastName("Masters").withAddress(address).build())
				.block();
		Client retrieved = repo.findOne(saved.getId()).block();
		address = Address.from(address).withZipcode("654321").build();
		retrieved = Client.from(retrieved).withEmail("kenm@email.com").withAddress(address).build();

		// When
		final Client updated = repo.save(retrieved).block();

		// Then
		assertThat(updated.getId()).isEqualTo(retrieved.getId());
		assertThat(updated.getEmail()).isEqualTo("kenm@email.com");
		assertThat(updated.getAddress().getZipcode()).isEqualTo("654321");
	}

	@Test
	public void shouldDeleteAPerson() {

		// Given
		final Client saved = repo.save(Client.ofType(PERSON).withFirstName("Ken").withLastName("Masters").build())
				.block();

		// When
		repo.delete(saved).block();
		boolean exists = repo.exists(saved.getId()).block();

		// Then
		assertThat(exists).isFalse();
	}

	@Test
	public void shouldReturnAllClients() {

		// Given
		final Address address = Address.ofCountry("Shadaloo").withStreetNumber(110).withStreetName("Bison street")
				.withCity("Shadaloo City").withZipcode("123456").build();
		repo.save(Client.ofType(PERSON).withFirstName("Ken").withLastName("Masters").withAddress(address).build())
				.block();
		repo.save(Client.ofType(COMPANY).withFirstName("Ken").withLastName("Masters").withAddress(address).build())
				.block();

		// When
		final Iterable<Client> clients = repo.findAll().toIterable();

		// Then
		assertThat(clients).isNotNull();
		assertThat(clients.iterator()).hasSize(2);
	}

	@Test
	public void shouldReturnNoClients() {

		// Given

		// When
		final Iterable<Client> clients = repo.findAll().toIterable();

		// Then
		assertThat(clients).isNotNull();
		assertThat(clients.iterator()).hasSize(0);
	}
}
