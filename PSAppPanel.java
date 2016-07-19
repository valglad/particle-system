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
//import java.lang.*;

public class PSAppPanel extends JPanel implements Runnable,MouseListener{
	public static int width=700;
	public static int height=500;
	private BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);;
	private PSystem system;
	private Thread animation;
	public boolean walls;

	public PSAppPanel(boolean w){
		walls=w;
		system=new PSystem(3);
		system.add(new SourceForce(width/2,height/2,1e4));
		//SourceForce f1=new SourceForce(system,3*width/4,3*height/4,7e9);
		setPreferredSize(new Dimension(width,height));
		addMouseListener(this);
		/*JLabel label=new JLabel("A label");
		label.setOpaque(true);
		mainFrame.getContentPane().add(label);*/
		//setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		drawParticles(system);
		g.drawImage(image,0,0,null);
	}

	private void drawParticles(PSystem system){
		Graphics g=image.createGraphics();
		g.setColor(Color.black);
  		g.fillRect(0, 0, image.getWidth(), image.getHeight());
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
			if (walls) system.evolve(width,height); else system.evolve();
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
		sw=!(sw);
		if (sw){
			animation=new Thread(this,"animation");
			animation.start();
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
