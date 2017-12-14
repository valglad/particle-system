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
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.lang.Void;

public class PSAppPanel extends JPanel implements Runnable,MouseListener{
	private BufferedImage image;
	public PSystem system;
	public Thread animation=new Thread(this, "animation");
	public boolean walls;
	public boolean merge=false;
//the following 3 variables is for setting a new SourceForce from ControlPanel;
	public volatile boolean awaitingInput=false;
	public int mouseX=0;
	public int mouseY=0;

	public PSAppPanel(boolean w){
		walls = w;
		system = new PSystem(PSApp.width, PSApp.height, new CollisionManager(this, PSystem.timeStep));
		addMouseListener(this);
	}

    public void stop(){
        // stop animation
        sw = false;
    }

    public void restart(){
        // resume a paused animation
        sw = true;
		animation = new Thread(this, "animation");
		animation.start();
    }

    public void restart(PSystem s){
        // restart animation with a given system
        // (don't start the animation)
        sw = false;
        system = s;
		animation = new Thread(this, "animation");
		repaint();
    }

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
        if (merge) drawParticlesMerge(system);
        else drawParticles(system);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	private void drawParticlesMerge(PSystem system){
		image=new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        //System.out.println("drawing");

        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        List<Future<?>> futures = new ArrayList<>();

        for (int xt = 0; xt < getWidth(); xt += numThreads){
        final int xtf = xt;
        for (int id = 0; id < numThreads; id++){
            final int idf = id;
            futures.add(executor.submit(() -> {
                synchronized(image){
                int x = xtf + idf;
                double particlePotential = system.averageMass/(double)system.particleSize;
                for (int y = 0; y < getHeight(); y++){
                    double potential = 0;
                    for (int i = 0; i < system.size; i++){
                        double x_p = system.particles[i].pos.x;
                        double y_p = system.particles[i].pos.y;
                        double r = Math.sqrt((x - x_p) * (x-x_p) + (y-y_p) * (y - y_p));
                        if (r > 0){
                            potential = potential + system.particles[i].mass/r;
                            //System.out.println(r+" "+potential+" "+1/r+" "+system.particles[i].size);
                        }else{
                            image.setRGB(x, y, (new Color(0, 200, 0)).getRGB());
                            break;
                        }
                        //System.out.println(particlePotential);
                        if (potential >= particlePotential){
                            image.setRGB(x, y, (new Color(0, 200, 0)).getRGB());
                            //Vec p = painted.get(0);
                            //System.out.println("Thread row "+x+" "+y+" "+image.getRGB(x, y)+" "+(new Color(0,200,0)).getRGB());}
                            break;
                        }
                    }
                }
                }
            }));
        }
        }

        executor.shutdown();
        try{
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
         }catch(InterruptedException e){
                System.out.println("Something went wrong with the graphics");
                System.exit(0);
         }


    }

	private void drawParticles(PSystem system){
		image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		g.setColor(Color.black);
  		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.setColor(new Color(0, 200, 0));
		for (int i = 0; i < system.size; i++){
			int size = system.particles[i].size;
			g.fillOval((int)(system.particles[i].pos.x - size), (int)(system.particles[i].pos.y - size), 2 * size, 2 * size);
		}
		Iterator<SourceForce> it = system.sourceForces.iterator();
		while (it.hasNext()){
			SourceForce f = it.next();
			if (!(f.isParticle())){
				g.setColor(new Color(200, 0, 0));
				g.fillOval((int)(f.pos.x - f.size), (int)(f.pos.y - f.size), 2 * f.size, 2 * f.size);
			}
		}
	}	

	@Override
	public void run(){
		while(sw){
			system.evolve(walls);
			repaint();
			pause((int)(PSystem.timeStep * 1000));
		}
	}

	@Override
	public void addNotify(){
		super.addNotify();
	}

	Boolean sw=false;	
	public void mouseClicked(MouseEvent e){
		if (!(awaitingInput)){
			sw = !(sw);
			if (sw){
				animation.start();
			}else{
				animation = new Thread(this, "animation");
			}
		}else{
			mouseX = e.getX();
			mouseY = e.getY();
			awaitingInput = false;
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
