package clientservice.models;

import java.io.Serializable;

import org.bson.types.ObjectId;

public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1971331382735300376L;

	ObjectId id;
	
	public ObjectId getId() {
		return id;
	}
}
