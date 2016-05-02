package jug.immutables;

import org.immutables.value.Value;

@Value.Immutable(builder=false)
@Value.Style(typeImmutable="Generated*")
public abstract class AbstractComplex {
	
	public static final AbstractComplex ZERO = GeneratedComplex.of(0, 0);
	public static final AbstractComplex ONE = GeneratedComplex.of(1, 0);
	public static final AbstractComplex I = GeneratedComplex.of(0, 1);
	
	@Value.Parameter
	abstract double re();
	@Value.Parameter
	abstract double im();
	
	public AbstractComplex add(AbstractComplex c) {
		return GeneratedComplex.of(re() + c.re(), im() + c.im());
	}

	public AbstractComplex subtract(AbstractComplex c) {
		return GeneratedComplex.of(re() - c.re(), im() - c.im());
	}

	public AbstractComplex multiply(AbstractComplex c) {
		return GeneratedComplex.of(re() * c.re() - im() * c.im(), re() * c.im() + im() * c.re());
	}

	public AbstractComplex divide(AbstractComplex c) {
		double tmp = c.re() * c.re() + c.im() * c.im();
		return GeneratedComplex.of((re() * c.re() + im() * c.im()) / tmp, (im() * c.re() - re() * c.im()) / tmp);
	}
}
