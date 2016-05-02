package jug.immutables;

public final class Complex {
	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex I = new Complex(0, 1);

	private final double re;
	private final double im;

	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	// Accessors with no corresponding mutators
	public double realPart() {
		return re;
	}

	public double imaginaryPart() {
		return im;
	}

	public Complex add(Complex c) {
		return new Complex(re + c.re, im + c.im);
	}

	public Complex subtract(Complex c) {
		return new Complex(re - c.re, im - c.im);
	}

	public Complex multiply(Complex c) {
		return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
	}

	public Complex divide(Complex c) {
		double tmp = c.re * c.re + c.im * c.im;
		return new Complex((re * c.re + im * c.im) / tmp, (im * c.re - re * c.im) / tmp);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(im);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(re);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Complex other = (Complex) obj;
		if (Double.doubleToLongBits(im) != Double.doubleToLongBits(other.im))
			return false;
		if (Double.doubleToLongBits(re) != Double.doubleToLongBits(other.re))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Complex [re=" + re + ", im=" + im + "]";
	}

}