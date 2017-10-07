package particleSystem;

public class UniformForce{
	public final double attraction;
	public Vec direction;
	public final double natLength;

	public UniformForce(double a, Vec d, double n){
		attraction = a;
		direction = d.timesNew(1/Math.sqrt(d.magnitude())); //normalising
		natLength = n;
	}

	public UniformForce(double a, Vec d){
		this(a, d, 0);
	}

	public void apply(Particle p, double timeStep, double unitDistance){
		p.vel.add(direction.timesNew(attraction*timeStep*unitDistance));
	}
}
