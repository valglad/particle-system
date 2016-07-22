package particleSystem;

import java.util.Random;

public class Vec{
	public double x;
	public double y;

	public Vec(Random rgen, String s){
		x=rgen.nextDouble()*PSApp.width;
		y=rgen.nextDouble()*PSApp.height;
	}

	public Vec(Random rgen, double range){
		x=rgen.nextDouble()*range-range/2;
		y=rgen.nextDouble()*range-range/2;
	}

	public Vec(double x1, double y1){
		x=x1;
		y=y1;
	}

	public void add(Vec v){
		x+=v.x;
		y+=v.y;
	}

	public Vec addNew(Vec v){
		return new Vec(x+v.x,y+v.y);
	}

	public Vec timesNew(double a){
		return new Vec(x*a,y*a);
	}

	public void times(double a){
		x*=a;
		y*=a;
	}

	public Vec difference(Vec v){
		return new Vec(x-v.x,y-v.y);
	}

	public double distance(Vec v){
		return (x-v.x)*(x-v.x)+(y-v.y)*(y-v.y);
	}

	public double magnitude(){
		return x*x+y*y;
	}

	public String toString(){
		return "("+x+", "+y+")";
	}
}
