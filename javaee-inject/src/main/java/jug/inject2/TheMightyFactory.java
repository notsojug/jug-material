package jug.inject2;

import javax.enterprise.inject.Produces;

public class TheMightyFactory {

	@Produces
	OnlyWithFactory getOnlyWithFactory(){
		OnlyWithFactory instance = OnlyWithFactory.create("42");
		return instance;
	}
	
	
	@Produces
	AbstractImmutableWithFactory getImmutableBlahBlah(){
		return ImmutableWithFactory.of("46");
	}
	
	@Produces
	SomethingWithLotsOfConfiguration createSomethingWithLotsOfConfiguration(){
		SomethingWithLotsOfConfiguration s = SomethingWithLotsOfConfiguration.create("/dev/null");
		s.init();
		return s;
	}
}
