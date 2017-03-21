package particleSystem;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CollisionManager{
	private PSAppPanel panel;
	private double timeStep;
	
	public CollisionManager(PSAppPanel s,double t){
		panel=s;
		timeStep=t;
	}

double gap=1;
//updating the positions of both particles before processing other collisions is inaccurate but will do for now; maybe employ matrices to do everything simultaneously in the future (or concurrency)
	public void collide(Particle p1,Particle p2,boolean walls,boolean start){
		if (p1.number>=p2.number) return;
		if (walls && !(start)){
			collideAtWalls(p1);
			collideAtWalls(p2);
		}
		Vec diff=(p1.pos).difference(p2.pos);
		double dist=Math.sqrt(diff.magnitude());
		double crtDist=p1.size+p2.size;
		if (dist<=crtDist){ //if particles are overlapping
			diff.normalise();
			p1.pos.add(diff.timesNew((crtDist-dist)/2+gap));
			p2.pos.add(diff.timesNew((dist-crtDist)/2));
			if (!start) collideInLine(p1,p2,diff);
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


//this uses cons of momentum and energy in 1-dimension (along the line of collision) to work out new velocities
	private void collideInLine(Particle p1,Particle p2,Vec d){ //d is the vector along which the collision is happenning
		double vel1 = p1.vel.dot(d); //speeds along the line of collision
		double vel2 = p2.vel.dot(d);
		double m1=p1.mass, m2=p2.mass;
		double newVel1=((m1-m2)*vel1+2*m2*vel2)/(m1+m2);
		double newVel2=((m2-m1)*vel2+2*m1*vel1)/(m1+m2);
		p1.vel.add(d.timesNew(newVel1-vel1));
		p2.vel.add(d.timesNew(newVel2-vel2));
	}

	private void collideAtWalls(Particle p){
		double width=panel.getWidth();
		double height=panel.getHeight();
		if (p.pos.x>=width-p.size) {p.pos.x=width-p.size; p.vel.x*=-1;}
		if (p.pos.x<=p.size) {p.pos.x=p.size; p.vel.x*=-1;}
		if (p.pos.y>=height-p.size) {p.pos.y=height-p.size; p.vel.y*=-1;}
		if (p.pos.y<=p.size) {p.pos.y=p.size; p.vel.y*=-1;}
	}


	public Vec nextpos(Particle p){
		InputStreamReader in=new InputStreamReader(System.in);
		BufferedReader keyboard=new BufferedReader(in);
		System.out.println("Pos:"+p.pos+" Vel:"+p.vel+" Next:"+p.pos.addNew(p.vel.timesNew(-timeStep)));
		try {keyboard.readLine();} catch (Exception e){System.out.println("input exception");}
		return p.pos.addNew(p.vel.timesNew(timeStep));
	}
}

/*m1*(v1-vel1)=m2*(vel2-v2)
m1*(v1^2-vel1^1)=m2*(vel2^2-v2)
v1+vel1=vel2+v2
vel1-vel2=v2-v1
m1*vel1+m2*vel2=m1*v1+m2*v2
[1 -1; m1 m2]
1/(m1+m2) [m2 1; -m1 1] = (v2-v1; m1*v1+m2*v2)

vel1=(2*m2*v2+(m1-m2)*v1)/(m1+m2)
vel2=(2*m1*v1+(m2-m1)*v2)/(m1+m2)*/
