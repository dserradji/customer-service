package clientservice.models;

import org.bson.types.ObjectId;

public abstract class AbstractEntity {

	protected ObjectId id;

	public ObjectId getId() {
		return id;
	}
}
