package particleSystem;

import java.util.Random;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
//import java.lang.Math;

public class PSystem{
	public int time; //current time in the system
	public static double timeStep=0.001;
	public Particle[] particles;
	public float[][] nextPos; //next predicted position of a particle
	public final int size;
	public static int defaultSize=10;
	private final Random rangen=new Random();
	public static int particleSize; //this is generic
	public HashSet<SourceForce> sourceForces;
	public static volatile boolean interForces=true;
	public static double interForceValue=10;
	public boolean gravityOn=false;
	public boolean changeInterForces=false;
	public double unitDistance=40; //no of pixels that constitute a unit distance wrt which force is applied
	private CollisionManager cMngr;

	public PSystem(int width,int height){ //width and height of the panel on which it will be displayed
		this(defaultSize,width,height);
	}

	public PSystem(int s,int width,int height){ //number of particles, ...
		size=s;
		particles=new Particle[size];
		sourceForces=new HashSet<SourceForce>();
		for (int i=0;i<size;i++){
			particles[i]=new Particle(new Vec(rangen,width,height),new Vec(rangen,100),new Vec(0,0),1*1e3,1,particleSize);
			sourceForces.add(new SourceForce(particles[i],interForceValue,1,100));
		}
		time=0;
		cMngr=new CollisionManager(this);
	}

	public PSystem(int s,int width,int height,double ul){
		this(s,width,height);
		unitDistance=ul;
	}

	public void evolve(){
		Iterator<SourceForce> it;
		boolean sw=false;
		if (changeInterForces) sw=true;
		for (int i=0;i<size;i++){
			particles[i].pos.add(particles[i].vel.timesNew(timeStep));
			if (gravityOn) particles[i].vel.add(new Vec(0,1*1e3*timeStep));
			particles[i].vel.add(particles[i].acc.timesNew(timeStep));
			it=sourceForces.iterator();
			while (it.hasNext()){
				SourceForce force=it.next();
				if (sw) force.attraction=interForceValue;
				if (force.isParticle()){
					cMngr.collide(particles[i],force.particle);
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

	public void evolve(int width,int height){
		Iterator<SourceForce> it;
		for (int i=0;i<size;i++){
			Vec v=nextpos(particles[i]);
			if ((int)(v.x)+particleSize>width || (int)(v.x)+particleSize<0) particles[i].vel.x*=-1;
			if ((int)(v.y)+particleSize>height || (int)(v.y)+particleSize<0) particles[i].vel.y*=-1;
			particles[i].pos.add(particles[i].vel.timesNew(timeStep));
			particles[i].vel.add(particles[i].acc.timesNew(timeStep));
			it=sourceForces.iterator();
			while (it.hasNext()){
				SourceForce force=it.next();
				if (!(interForces) && (force.isParticle())) continue;
				force.apply(particles[i], timeStep,unitDistance);
			}
		}
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

	/*public void manageCollisions(int i){
		Hashset collidingParticles=currentPositions.retainAll(surroundingPositions(int i));
		if (collidingParticles.length>0){
			Iterator it=collidingParticles.iterator();
			while (it.hasNext()){
			}
		}
	}*/

	/*public List[] makeTupleArray(Object[] array){
		List[] posTuples=new List[array.length];
		for (int i=0;i<array.length;i++){
			List tuple=new List();
			tuple.add(array[i]);
			tuple.add(i);
			posTuples[i]=tuple;
		}
	}*/

	public void collide(Particle p1,Particle p2){ //p1 is going to be modified but not p2
		Vec diff=nextpos(p1).difference(nextpos(p2));
		if (diff.magnitude()<=(p1.size+p2.size)*(p1.size+p2.size)){
			double tan=diff.y/diff.x;
			p1.vel.x*=tan;
		}
	}

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
