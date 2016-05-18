package jug.inject2;

import com.google.common.base.Preconditions;

public class OnlyWithFactory {
	private final String id;

	private OnlyWithFactory(String id) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public static OnlyWithFactory create(String id){
		return new OnlyWithFactory(Preconditions.checkNotNull(id));
	}
}

