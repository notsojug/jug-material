package jug.guava;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class FilteringTest {

	private List<State> states;

	@Before
	public void setUp() {
		states = Lists.newArrayList();

		states.add(new State("WI", "Wisconsin", "MDW", 5726398));
		states.add(new State("FL", "Florida", "SE", 19317568));
		states.add(new State("IA", "Iowa", "MDW", 3078186));
		states.add(new State("CA", "California", "W", 38041430));
		states.add(new State("NY", "New York", "NE", 19570261));
		states.add(new State("CO", "Colorado", "W", 5187582));
		states.add(new State("OH", "Ohio", "MDW", 11544225));
		states.add(new State("ME", "Maine", "NE", 1329192));
		states.add(new State("SD", "South Dakota", "MDW", 833354));
		states.add(new State("TN", "Tennessee", "SE", 6456243));
		states.add(new State("OR", "Oregon", "W", 3899353));
	}
	
	Condition<State> hasMdwRegion = new Condition<State>() {
		@Override
		public boolean matches(State value) {
			return "MDW".equals(value.getRegionCode());
		}
	};

	@Test
	public void shouldFilterStateOfMDW() {

		List<State> mdwStates = Lists.newArrayList();
		for (State state : states) {

			if ("MDW".equals(state.getRegionCode())) {
				mdwStates.add(state);
			}
		}

		assertThat(mdwStates).areExactly(4, hasMdwRegion);
	}
	
	Predicate<State> byMdwRegion = new Predicate<State>() {

		@Override
		public boolean apply(State input) {
			return "MDW".equals(input.getRegionCode());
		}
	};

	@Test
	public void get_mdw_states_with_guava_Collections2() {
		Collection<State> mdwStates = Collections2.filter(states, byMdwRegion);

		assertThat(mdwStates).areExactly(4, hasMdwRegion);
	}

	@Test
	public void get_mdw_states_with_guava_Iterables() {
		Iterable<State> iter = Iterables.filter(states, byMdwRegion);
		
		assertThat(iter).areExactly(4, hasMdwRegion);
	}

	@Test
	public void get_mdw_states_with_guava_FluentIterable() {
		List<State> mdwStates = FluentIterable
                .from(states)
                .filter(byMdwRegion)
                .toList();

		assertThat(mdwStates).areExactly(4, hasMdwRegion);
	}
}
