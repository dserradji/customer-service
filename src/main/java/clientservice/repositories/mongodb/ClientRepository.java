package clientservice.repositories.mongodb;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import clientservice.models.Client;

public interface ClientRepository extends CrudRepository<Client, ObjectId> {

}
