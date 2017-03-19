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
import clientservice.models.Address;
import clientservice.models.Client;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class ClientRepositoryTest {

	@Autowired
	private ClientRepository repo;

	@Test
	public void shouldCreateAPersonWithId() {

		// Given
		final Address address = Address.ofCountry("Shadaloo").streetNumber(110).streetName("Bison street")
				.city("Shadaloo City").zipcode("123456").build();

		// When
		final Client saved = repo
				.save(Client.ofType(PERSON).firstName("Ken").lastName("Masters").address(address).build());

		// Then
		assertThat(saved.getId(), is(notNullValue()));
		assertThat(saved.getFirstName(), is(equalTo("Ken")));
		assertThat(saved.getAddress().getCountry(), is(equalTo("Shadaloo")));
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
		assertThat(retrieved, is(notNullValue()));
		assertThat(retrieved.getId(), is(equalTo(saved.getId())));
		assertThat(retrieved.getFirstName(), is(equalTo(saved.getFirstName())));
		assertThat(retrieved.getAddress().getCountry(), is(equalTo("Shadaloo")));
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
		assertThat(updated.getId(), is(equalTo(retrieved.getId())));
		assertThat(updated.getEmail(), is(equalTo("kenm@email.com")));
		assertThat(updated.getAddress().getZipcode(), is(equalTo("654321")));
	}

	@Test
	public void shouldDeleteAPerson() {

		// Given
		final Client saved = repo.save(Client.ofType(PERSON).firstName("Ken").lastName("Masters").build());

		// When
		repo.delete(saved);
		boolean exists = repo.exists(saved.getId());

		// Then
		assertThat(exists, is(false));
	}
}
