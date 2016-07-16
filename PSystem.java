package particleSystem;

import java.util.Random;

public class PSystem{
	public int time; //current time in the system
	public static double timeStep=0.1;
	public Vec[] pos;
	public Vec[] vel;
	public Vec[] acc;
	public static int size;
	private final Random rangen=new Random();
	public static int particleSize;

	public PSystem(int num){ //number of particles
		pos=new Vec[num];
		vel=new Vec[num];
		acc=new Vec[num];
		for (int i=0;i<num;i++){
			pos[i]=new Vec(rangen,"pos");
			vel[i]=new Vec(rangen,10);
			acc[i]=new Vec(rangen,2);
		}
		time=0;
		size=num;
	}

	public void evolve(){
		for (int i=0;i<size;i++){
			pos[i].add(vel[i]);
			vel[i].add(acc[i]);
		}
		time+=timeStep;
	}

	public void evolve(int width,int height){
		for (int i=0;i<size;i++){
			if ((int)(pos[i].x+vel[i].x)+particleSize>width || (int)(pos[i].x+vel[i].x)+particleSize<0) vel[i].x*=-1;
			if ((int)(pos[i].y+vel[i].y)+particleSize>height || (int)(pos[i].y+vel[i].y)+particleSize<0) vel[i].y*=-1;
			pos[i].add(vel[i]);
			vel[i].add(acc[i]);
		}
		time+=timeStep;
	}

	public String toString(){
		String s="Particle System: \n";
		for(Integer i=0;i<size;i++)
			s+=i.toString()+": "+pos[i].toString()+", "+vel[i].toString()+", "+acc[i].toString()+"\n";		
		return s;
	}
}	
