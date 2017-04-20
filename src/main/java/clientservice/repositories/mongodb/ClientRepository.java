package clientservice.repositories.mongodb;

import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import clientservice.models.Client;

public interface ClientRepository extends ReactiveCrudRepository<Client, ObjectId> {

}
