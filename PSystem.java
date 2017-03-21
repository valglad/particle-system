package particleSystem;

import java.util.Random;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.lang.Math;

public class PSystem{
	public int time; //current time in the system
	public static double timeStep=0.001;
	public Particle[] particles;
	public float[][] nextPos; //next predicted position of a particle
	public final int size;
	public static int defaultSize=30;
	private final Random rangen=new Random();
	public static int particleSize; //this is average size (corresponds to average mass)
	public static double massVariation=0.8; //as a proportion of average mass
	public static double averageMass=100;
	public HashSet<SourceForce> sourceForces;
	public static volatile boolean interForces=false;
	public static double interForceValue=10;
	public boolean gravityOn=false;
	public boolean changeInterForces=false;
	public double unitDistance=40; //no of pixels that constitute a unit distance wrt which force is applied
	public CollisionManager cMngr;

	public PSystem(int width,int height,CollisionManager cm){ //width and height of the panel on which it will be displayed
		this(defaultSize,width,height,cm);
	}

	public PSystem(int s,int width,int height,double ul,CollisionManager cm){
		this(s,width,height,cm);
		unitDistance=ul;
	}

	public PSystem(int s,int width,int height,CollisionManager cm){ //number of particles, ...
		size=s;
		particles=new Particle[size];
		sourceForces=new HashSet<SourceForce>();
		double mass;
		int pSize;
		for (int i=0;i<size;i++){
			mass=((rangen.nextDouble()-0.5)*massVariation+1)*averageMass;
			pSize=(int)(particleSize*mass/averageMass);
			particles[i]=new Particle(i,new Vec(rangen,width,height),new Vec(rangen,100),new Vec(0,0),1e2,mass,pSize);
			sourceForces.add(new SourceForce(particles[i],interForceValue,1,300));
			Particle p=particles[i];
			if (p.pos.x>=width-p.size) p.pos.x=width-p.size;
			if (p.pos.x<=p.size) p.pos.x=p.size;
			if (p.pos.y>=height-p.size) p.pos.y=height-p.size;
			if (p.pos.y<=p.size) p.pos.y=p.size;
		}
		time=0;
		cMngr=cm;
		for (int i=0;i<size;i++){
			for (int j=0;j<size;j++){
				cMngr.collide(particles[i],particles[j],true,true); //to push the particles away if they overlap
			}
		}
	}

	public void evolve(boolean walls){
		Iterator<SourceForce> it;
		boolean sw=false;
		if (changeInterForces) sw=true;
		for (int i=0;i<size;i++){
			particles[i].pos.add(particles[i].vel.timesNew(timeStep));
			if (gravityOn) particles[i].vel.add(new Vec(0,1*1e3*timeStep));
			it=sourceForces.iterator();
			while (it.hasNext()){
				SourceForce force=it.next();
				if (sw) force.attraction=interForceValue;
				if (force.isParticle()){
					cMngr.collide(particles[i],force.particle,walls,false);
				  	if (!(interForces)) continue;
				}
				force.apply(particles[i], timeStep, unitDistance);
			}
			particles[i].prevPos=particles[i].pos;
		}
		if (sw) changeInterForces=false;
		it=sourceForces.iterator();
		while (it.hasNext()){
			it.next().update();
		}
		time+=timeStep;
	}

	public Vec nextpos(Particle p){
		return p.pos.addNew(p.vel.timesNew(timeStep));
	}

	/*public Vec nextvel(int i){
		return vel[i].addNew(acc[i]);
	}*/

	public void add(SourceForce s){
		sourceForces.add(s);
	}


	public String toString(){
		String s="Particle System: \n";
		for(Integer i=0;i<size;i++)
			s+=i.toString()+": "+particles[i].pos.toString()+", "+particles[i].vel.toString()+", "+particles[i].acc.toString()+"\n";		
		return s;
	}
}	
