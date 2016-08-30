package particleSystem;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CollisionManager{
	private PSystem system;
	private double timeStep;
	
	public CollisionManager(PSystem s){
		system=s;
		timeStep=s.timeStep;
	}

//updating the positions of both particles before processing all other collisions will probably be inaccurate but oh well
	public void collide(Particle p1,Particle p2){
		Vec diff=(p1.pos).difference(p2.pos);
		double dist=Math.sqrt(diff.magnitude());
		if (dist<=p1.size+p2.size){ //if particles are overlapping
			diff.normalise();
			p1.pos.add(diff.timesNew((p1.size+p2.size-dist)*0.5));
			p2.pos.add(diff.timesNew(-(p1.size+p2.size-dist)*0.5));
			adjustVel(p1,diff);
			adjustVel(p2,diff);
		/*}else{
			diff2=(p1.prevPos).difference(p2.prevPos);diff=nextpos(p1).difference(nextpos(p2));
			double len=diff2.dot(diff);
			if (len<0){//if particles flew past each other
				diff.times(-len);
				p1.pos.add(diff);
				diff.times(-1);
				p2.pos.add(diff);
			}*/
		}
	}

	public void adjustVel(Particle p,Vec d){ //d is the vector along which the collision is happenning
		double parallel = p.vel.dot(d);
		p.vel.add(d.timesNew(-2*parallel));
	}

	public Vec nextpos(Particle p){
		InputStreamReader in=new InputStreamReader(System.in);
		BufferedReader keyboard=new BufferedReader(in);
		System.out.println("Pos:"+p.pos+" Vel:"+p.vel+" Next:"+p.pos.addNew(p.vel.timesNew(-timeStep)));
		try {keyboard.readLine();} catch (Exception e){System.out.println("input exception");}
		return p.pos.addNew(p.vel.timesNew(timeStep));
	}
}
