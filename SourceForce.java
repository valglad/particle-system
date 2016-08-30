package particleSystem;

//a radially symmetric source of force; inversely proportional to the square of the distance from the source by default (attraction or repulsion)

//in "elastic" mode distPower increases by 1 during contraction

public class SourceForce extends Particle{
	public double distPower; //inversely proportional to (distance-natLength)^distPower
	public final Particle particle;
	public double natLength; //0 if force is not elastic

	public SourceForce(double x, double y,double at){
		this(x,y,at,-2,0);
	}

	public SourceForce(double x, double y,double at,double dp,double l){
		super(new Vec(x,y),new Vec(0,0),new Vec(0,0),at,1,5);
		particle=null;
		distPower=dp;
		natLength=l;
	}

	public SourceForce(Particle p){
		super(p);
		particle=p;
		distPower=-2;
	}

	public SourceForce(Particle p,double at,double dp,double l){
		super(p);
		particle=p;
		attraction=at;
		distPower=dp;
		natLength=l;
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

	public void apply(Particle p, double timeStep, double unitDistance){
				if (particle==p) return;
				Vec diff=pos.difference(p.pos);
				double diffLength=Math.sqrt(diff.magnitude());
				double dist=diffLength-natLength;
				double factor;
				if (dist==0){
					if (distPower<1) { p.vel=new Vec(0,0); return; }
					else factor=0;
				}else factor=attraction*timeStep*Math.pow(dist/unitDistance,distPower)/diffLength;
				if (dist<0) factor*=-dist;
				p.vel.add(diff.timesNew(factor));
	}
}
