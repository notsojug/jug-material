package jug.optional;

import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public class TheHappyFarmer {
	
	final Map<String, Integer> map;
	
	public static TheHappyFarmer of() {
		return new TheHappyFarmer();
	}

	private TheHappyFarmer() {
		super();
		this.map = Maps.newHashMap();
	}
	
	public TheHappyFarmer harvest(String key, @Nullable Integer value) {
		map.put(key, value);
		return this;
	}
	
	/** I'm idiot-proof! */
	public Optional<Integer> pickOut(String key) {
		return Optional.fromNullable(map.get(key));
	}

}
