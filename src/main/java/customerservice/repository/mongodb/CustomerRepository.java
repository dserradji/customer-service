package customerservice.repository.mongodb;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import customerservice.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, ObjectId> {

}
