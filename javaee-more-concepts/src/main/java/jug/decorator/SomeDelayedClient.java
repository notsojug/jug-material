package jug.decorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import com.google.common.base.Throwables;

@Decorator
public abstract class SomeDelayedClient implements Client{

	@Inject
	@Delegate
	@Any
	Client inner;

	@Override
	public String getValueFromTheInternet(String id) {

		if("yo".equals(id)){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw Throwables.propagate(e);
			}
		}
		
		return "delayed=" + inner.getValueFromTheInternet(id);
	}
}
