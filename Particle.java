package particleSystem;

public class Particle{
	public Vec pos;
	public Vec prevPos; //previous position; this is for managing collisions
	public Vec vel;
	public Vec acc;
	public double attraction;
	//attraction of 1 kg 1 metre away from source; can be negative - repulsion
	public final int number; //the index in the array of the particle system; necessary for processing each particle once only during collisions

	public double mass;
	public int size; //radius on screen

	public Particle(int n,Vec p, Vec v, double at, double m, int s){
		number=n;
		pos=p;
		prevPos=p;
		vel=v;
		acc=new Vec(0,0);
		attraction=at;
		mass=m;
		size=s;
	}

	public Particle(Particle p){
		number=p.number;
		pos=p.pos;
		vel=p.vel;
		acc=p.acc;
		attraction=p.attraction;
		mass=p.mass;
		size=p.size;
	}
}
