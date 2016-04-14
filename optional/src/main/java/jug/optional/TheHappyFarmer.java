package jug.optional;

import static com.google.common.base.Preconditions.checkNotNull;
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
	
	public TheHappyFarmer harvest(String what, @Nullable Integer howMuch) {
		map.put(checkNotNull(what), howMuch);
		return this;
	}
	
	/** I'm idiot-proof! */
	public Optional<Integer> pickOut(String what) {
		return Optional.fromNullable(map.get(checkNotNull(what)));
	}

}
