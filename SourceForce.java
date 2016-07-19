package particleSystem;

//a radially symmetric source of force; inversely proportional to the square of the distance from the source by default

public class SourceForce extends Particle{
	public double distPower; //inversely proportional to distance^distPower; distPower must be positive for now;
	public final Particle particle;

	public SourceForce(double x, double y,double at){
		super(new Vec(x,y),new Vec(0,0),new Vec(0,0),at,1,5);
		//pos=new Vec(x,y);
		//size=5;
		//attraction=at;
		particle=null;
		distPower=2;
	}

	public SourceForce(Particle p){
		super(p);
		particle=p;
		distPower=2;
	}

	public boolean isParticle(){
		if (particle==null) return false;
		else return true;
	}

	public void update(){
		if (isParticle()){
			pos=particle.pos;
			vel=particle.vel;
			acc=particle.acc;
		}
	}
}
