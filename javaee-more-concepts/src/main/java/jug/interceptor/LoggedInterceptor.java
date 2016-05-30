package jug.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

@Logged
@Interceptor
public class LoggedInterceptor implements Serializable {

	private static final long serialVersionUID = 2801835281062447116L;

	@AroundInvoke
	public Object logMyCall(InvocationContext context) throws Exception {
		
		// you can get the called method 
		Method method = context.getMethod();
		
		// and the class of that method, of course
		Class<?> clazz = method.getDeclaringClass();
		
		// you can get the parameters
		Object[] params = context.getParameters();
		//you can set (!) the parameters....
//		context.setParameters(...);
		
		// you can get the target class (the instance of clazz on which the method was called)
//		context.getTarget();

		
		// do what you need 
		System.out.println("called " + method.getName() + " on class " + clazz+ " params="+listParams(params));
		
		// invoke the method or let the next interceptor do its work
		return context.proceed();
	}

	private String listParams(Object[] params) {
		List<String> pars = Lists.newArrayList();
		for (Object o : params) {
			pars.add(o.getClass().getSimpleName() + "=" + o);
		}
		return "[" + Joiner.on(",").join(pars) + "]";
	}

}
