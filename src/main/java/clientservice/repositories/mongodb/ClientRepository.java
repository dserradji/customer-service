package clientservice.repositories.mongodb;

import java.math.BigInteger;

import org.springframework.data.repository.CrudRepository;

import clientservice.model.Client;


public interface ClientRepository extends CrudRepository<Client, BigInteger> {

}
