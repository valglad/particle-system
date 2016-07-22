package particleSystem;

import javax.swing.JPanel;
import java.awt.Graphics;
import javax.swing.JButton;
import java.awt.event.*;

public class ControlPanel extends JPanel implements ActionListener{
	private static JButton newSystem=new JButton("New system");
	private static JButton putForce=new JButton("Create a force source");
	private PSAppPanel animationPanel;

	public ControlPanel(PSAppPanel p){
		super();
		animationPanel=p;
		newSystem.setActionCommand("new");
		newSystem.setToolTipText("Restart the simulation with a new set of particle and source forces");
		putForce.setToolTipText("Create a new source of force at selected location");
		newSystem.addActionListener(this);
		putForce.addActionListener(this);
		add(newSystem);
		add(putForce);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if (e.getActionCommand()=="new"){
			if (animationPanel.animation.isAlive()) animationPanel.sw=false;
			animationPanel.system=new PSystem();
			animationPanel.animation=new Thread(animationPanel,"animation");
			animationPanel.repaint();
		}else{
			Thread thread=new Thread(new Runnable(){
				@Override
				public void run(){
					boolean toRestart=false;
					if (animationPanel.animation.isAlive()){ 
						toRestart=true;
						animationPanel.sw=false;
					}
					animationPanel.forceBeingSet=true;
					while (animationPanel.forceBeingSet){}
					animationPanel.system.add(new SourceForce(animationPanel.mouseX,animationPanel.mouseY,5e4));
					if (toRestart) {
						animationPanel.sw=true;
						animationPanel.animation=new Thread(animationPanel,"animation");
						animationPanel.animation.start();
					} else animationPanel.repaint();
				}
			});
			thread.start();
		}
	}

	public void pause(int delay){
		try{
			Thread.sleep(delay);
		}
		catch (InterruptedException e){
		}
	}
}

	
