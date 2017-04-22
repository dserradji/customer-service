package clientservice.repository.mongodb;

import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import clientservice.domain.Client;

public interface ClientRepository extends ReactiveCrudRepository<Client, ObjectId> {

}
