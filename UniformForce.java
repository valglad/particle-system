package particleSystem;

public class UniformForce implements java.io.Serializable{
	public final double attraction;
	public Vec direction;
	public final double natLength;

	public UniformForce(double a, Vec d, double n){
		attraction = a;
		direction = d.times(1/Math.sqrt(d.magnitude())); //normalising
		natLength = n;
	}

	public UniformForce(double a, Vec d){
		this(a, d, 0);
	}

	public void apply(Particle p, double unitDistance){
		p.acc = p.acc.add(direction.times(attraction*unitDistance/p.mass));
	}
}
