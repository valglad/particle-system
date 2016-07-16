package particleSystem;

import java.util.Random;

public class Vec{
	public double x;
	public double y;

	public Vec(Random rgen, String s){
		x=rgen.nextDouble()*PSAppPanel.width;
		y=rgen.nextDouble()*PSAppPanel.height;
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

	public String toString(){
		return "("+x+", "+y+")";
	}
}
