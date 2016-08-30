package particleSystem;

public class Particle{
	public Vec pos;
	public Vec prevPos; //previous position; this is for managing collisions
	public Vec vel;
	public Vec acc;
	public double attraction;
	//attraction of 1 kg 1 metre away from source; can be negative - repulsion

	public double mass;
	public int size; //radius on screen

	public Particle(Vec p, Vec v, Vec a, double at, double m, int s){
		pos=p;
		prevPos=p;
		vel=v;
		acc=a;
		attraction=at;
		mass=m;
		size=s;
	}

	public Particle(Particle p){
		pos=p.pos;
		vel=p.vel;
		acc=p.acc;
		attraction=p.attraction;
		mass=p.mass;
		size=p.size;
	}
}
