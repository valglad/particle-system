package particleSystem;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.Iterator;
import java.util.HashSet;
import java.awt.BorderLayout;
//import java.lang.*;

public class PSAppPanel extends JPanel implements Runnable,MouseListener{
	private BufferedImage image;
	public PSystem system;
	public Thread animation=new Thread(this,"animation");
	public boolean walls;
	public volatile boolean forceBeingSet=false; //the following 3 variables is for setting a new SourceForce from ControlPanel;
	public int mouseX=0;
	public int mouseY=0;

	public PSAppPanel(boolean w){
		walls=w;
		system=new PSystem(PSApp.width,PSApp.height);
		addMouseListener(this);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		drawParticles(system);
		g.drawImage(image,0,0,getWidth(),getHeight(),null);
	}

	private void drawParticles(PSystem system){
		image=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics g=image.createGraphics();
		//g.setColor(Color.black);
  		//g.fillRect(0, 0, image.getWidth(), image.getHeight());
		int size=system.particleSize*2;
		g.setColor(new Color(0,200,0));
		for (int i=0;i<system.size;i++){
			g.fillOval((int)system.particles[i].pos.x,(int)system.particles[i].pos.y,size,size);
		}
		Iterator<SourceForce> it=system.sourceForces.iterator();
		while (it.hasNext()){
			SourceForce f=it.next();
			if (!(f.isParticle())) g.setColor(new Color(200,0,0)); else g.setColor(new Color(0,200,0));
			g.fillOval((int)f.pos.x,(int)f.pos.y,f.size,f.size);
		}
		Toolkit.getDefaultToolkit().sync();
	}	

	@Override
	public void run(){
		while(sw){
			if (walls) system.evolve(getWidth(),getHeight()); else system.evolve();
			repaint();
			pause((int)(PSystem.timeStep*1000));
		}
	}

	@Override
	public void addNotify(){
		super.addNotify();
	}

	Boolean sw=false;	
	public void mouseClicked(MouseEvent e){
		if (!(forceBeingSet)){
			sw=!(sw);
			if (sw){
				animation.start();
			}else{
				animation=new Thread(this,"animation");
			}
		}else{
			mouseX=e.getX();
			mouseY=e.getY();
			forceBeingSet=false;
		}	
	}

	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}

	public void pause(int delay){
		try{
			Thread.sleep(delay);
		}
		catch (InterruptedException e){
		}
	}
}
