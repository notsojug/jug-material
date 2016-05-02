package jug.immutables;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class MetadataFluentInterfaceTest {
	@Test
	public void extractOnlyNames() throws Exception {
		ArrayList<Metadata> employees = Lists.newArrayList(
				Metadata.builder().name("Alberto").surname("Rossi").age(23).role(Role.DEV).build(),
				Metadata.of("Stefano", "Bianchi", 26, Role.MANAGER),
				Metadata.of("Annibale", "Khan", 55, Role.OP),
				Metadata.of("Morgan", "Freeman", 78, Role.GOD));
		
		ImmutableList<String> sortedNAmes = FluentIterable.from(employees)
			.transform(MetadataFunctions.getName())
			.toSortedList(Ordering.natural());
		
		assertThat(sortedNAmes)
			.hasSize(4)
			.containsExactly("Alberto", "Annibale", "Morgan", "Stefano");
	}
}
