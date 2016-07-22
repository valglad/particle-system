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
	public final int size;
	public static int defaultSize=10;
	private final Random rangen=new Random();
	public static int particleSize; //this is generic
	public HashSet<SourceForce> sourceForces;
	public static volatile boolean interForces=true;

	public PSystem(int width,int height){ //width and height of the panel on which it will be displayed
		size=defaultSize;
		particles=new Particle[size];
		sourceForces=new HashSet<SourceForce>();
		for (int i=0;i<size;i++){
			particles[i]=new Particle(new Vec(rangen,width,height),new Vec(rangen,100),new Vec(0,0),1000,1,particleSize);
			sourceForces.add(new SourceForce(particles[i])); //in case interparticle forces are introduced midway trough the simulation
		}
		//currentPositions=new HashSet(makeTupleArray(pos));
		time=0;
	}

	public PSystem(int s,int width,int height){ //number of particles, ...
		size=s;
		particles=new Particle[size];
		sourceForces=new HashSet<SourceForce>();
		for (int i=0;i<size;i++){
			particles[i]=new Particle(new Vec(rangen,width,height),new Vec(rangen,100),new Vec(0,0),1000,1,particleSize);
			sourceForces.add(new SourceForce(particles[i])); //in case interparticle forces are introduced midway trough the simulation
		}
		//currentPositions=new HashSet(makeTupleArray(pos));
		time=0;
	}

	public void evolve(){
		Iterator<SourceForce> it;
		for (int i=0;i<size;i++){
			particles[i].pos.add(particles[i].vel.timesNew(timeStep));
			particles[i].vel.add(particles[i].acc.timesNew(timeStep));
			it=sourceForces.iterator();
			while (it.hasNext()){
				SourceForce force=it.next();
				if (!(interForces) && (force.isParticle())) continue;
				Vec diff=force.pos.difference(particles[i].pos);
				double dist=Math.pow(diff.magnitude(),force.distPower/2);
				double factor=force.attraction*timeStep/dist;
				if (dist != 0) particles[i].vel.add(diff.timesNew(factor));
				else if (!(force.particle==particles[i])) { particles[i].vel=new Vec(0,0); }
				//if (force.isParticle()){force.particle.vel.add(diff.timesNew(-1*factor));}
			}
		}
		it=sourceForces.iterator();
		while (it.hasNext()){
			it.next().update();
		}
		//currentPositions=new HashSet(pos);
		time+=timeStep;
	}

	public void evolve(int width,int height){
		Iterator<SourceForce> it;
		for (int i=0;i<size;i++){
			Vec v=nextpos(i);
			if ((int)(v.x)+particleSize>width || (int)(v.x)+particleSize<0) particles[i].vel.x*=-1;
			if ((int)(v.y)+particleSize>height || (int)(v.y)+particleSize<0) particles[i].vel.y*=-1;
		//a very ineffective way of managing collisions (assuming elastic collisions for now)	
		/*for (int j=0;j<size;j++){
				if ((nextpos[j].x-nextpos[i].x)*(nextpos[j].x-nextpos[i].x)+(nextpos[j].y-nextpos[i].y)*(nextpos[j].y-nextpos[i].y)<4*particleSize*particleSize) vel[i].x*/
			particles[i].pos.add(particles[i].vel.timesNew(timeStep));
			particles[i].vel.add(particles[i].acc.timesNew(timeStep));
			it=sourceForces.iterator();
			while (it.hasNext()){
				SourceForce force=it.next();
				Vec diff=force.pos.difference(particles[i].pos);
				double dist=Math.pow(diff.magnitude(),force.distPower/2);
				double factor=force.attraction*timeStep/dist;
				if (dist != 0) particles[i].vel.add(diff.timesNew(factor));
				else if (!(force.particle==particles[i])) { particles[i].vel=new Vec(0,0); }
				//if (force.isParticle()){force.particle.vel.add(diff.timesNew(-1*factor));}
			}
		}
		it=sourceForces.iterator();
		while (it.hasNext()){
			it.next().update();
		}
		//currentPositions=new HashSet(pos);
		time+=timeStep;
	}

	public Vec nextpos(int i){
		return particles[i].pos.addNew(particles[i].vel);
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
