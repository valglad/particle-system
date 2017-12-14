package particleSystem;

import java.util.Random;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.lang.Math;

public class PSystem implements java.io.Serializable{
	public int time; //current time in the system
	public static double timeStep=0.001;
	public Particle[] particles;
	public float[][] nextPos; //next predicted position of a particle
	public final int size;
	public static int defaultSize=20;//number of particles in the system by default
	private final Random rangen=new Random();
	public static int particleSize; //this is average size (corresponds to average mass)
	public static double massVariation=0.8; //as a proportion of average mass
	public static double averageMass=100;
	public static double averageSpeed=50; //vertically/horizontally
	public HashSet<SourceForce> sourceForces;
	public static volatile boolean interForces=false;
	public static double interForceValue=10;
	public static volatile boolean gravityOn=false;
	public UniformForce gravity;
	public boolean changeInterForces=false;
	public double unitDistance=40; //no of pixels that constitute a unit distance wrt which force is applied
	public transient CollisionManager cMngr;

    public PSystem(Particle[] particles, CollisionManager cm){
        particles = particles;
        size = particles.length;
        cMngr = cm;
    }

	public PSystem(int width, int height, CollisionManager cm){ //width and height of the panel on which it will be displayed
		this(defaultSize, width, height, cm);
	}

	public PSystem(int s, int width, int height, double ul, CollisionManager cm){
		this(s,width,height,cm);
		unitDistance = ul;
	}

	public PSystem(int s, int width, int height, CollisionManager cm){ //number of particles, ...
		size = s;
		particles = new Particle[size];
		sourceForces = new HashSet<SourceForce>();
		double mass, vx, vy;
        Vec velocity;
		int pSize, dir;
		for (int i = 0; i < size; i++){
			mass = ((rangen.nextDouble() - 0.5) * massVariation + 1) * averageMass;
            dir = rangen.nextInt(2);
            if (dir == 0) dir = -1;
            vx = dir * ((rangen.nextDouble() - 0.5) * 0.6 + 1) * averageSpeed;
            dir = rangen.nextInt(2);
            if (dir == 0) dir = -1;
            vy = dir*((rangen.nextDouble() - 0.5) * 0.6 + 1) * averageSpeed;
            velocity = new Vec(vx, vy);
			pSize = (int)(particleSize * mass / averageMass);
			particles[i] = new Particle(i, new Vec(rangen, width, height), velocity, 1e2, mass, pSize);
			sourceForces.add(new SourceForce(particles[i], interForceValue, 1, 300));
			Particle p = particles[i];
			if (p.pos.x >= width - p.size) p.pos.x = width - p.size;
			if (p.pos.x <= p.size) p.pos.x = p.size;
			if (p.pos.y >= height-p.size) p.pos.y = height - p.size;
			if (p.pos.y <= p.size) p.pos.y = p.size;
		}
		time = 0;
		cMngr = cm;
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				cMngr.collide(particles[i], particles[j], true); //to push the particles away if they overlap
			}
		}
		gravity = new UniformForce(9.8, new Vec(0, 1));
	}

	public void evolve(boolean walls){
		Iterator<SourceForce> it;
		boolean sw = false;
		if (changeInterForces) sw = true;
		for (int i = 0; i < size; i++){
			particles[i].prevPos = particles[i].pos;
			particles[i].pos.add(particles[i].vel.timesNew(timeStep));
            if (walls) cMngr.collideAtWalls(particles[i]);
			it = sourceForces.iterator();
			while (it.hasNext()){
				SourceForce force = it.next();
				if (sw) force.attraction = interForceValue;
				if (force.isParticle()){
					cMngr.collide(particles[i], force.particle, false);
				  	if (!(interForces)) continue;
				}
				force.apply(particles[i], timeStep, unitDistance);
			}
			if (gravityOn) gravity.apply(particles[i], timeStep, unitDistance);
		}
		if (sw) changeInterForces=false;
		it = sourceForces.iterator();
		while (it.hasNext()){
			it.next().update();
		}
		time += timeStep;
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
		for(Integer i = 0; i < size; i++)
			s+=i.toString()+": "+particles[i].pos.toString()+", "+particles[i].vel.toString()+", "+particles[i].acc.toString()+"\n";		
		return s;
	}
}	

