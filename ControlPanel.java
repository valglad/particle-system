package particleSystem;

import javax.swing.JPanel;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.*;
import javax.swing.JFormattedTextField;
import java.text.NumberFormat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class ControlPanel extends JPanel implements ActionListener,ItemListener,PropertyChangeListener{
	private static JButton newSystem=new JButton("New system");
	private static JButton putForce=new JButton("Create a force source");
	private static JCheckBox interForces=new JCheckBox("Enable inter-particle attraction");
	private JFormattedTextField systemSize=new JFormattedTextField(NumberFormat.getNumberInstance());
	private PSAppPanel animationPanel;
	private int nextSystemSize=PSystem.defaultSize;

	public ControlPanel(PSAppPanel p){
		super();
		animationPanel=p;
		interForces.setSelected(true);
		interForces.addItemListener(this);
		systemSize.setValue(PSystem.defaultSize);
		systemSize.setColumns(4);
		systemSize.addPropertyChangeListener("value",this);
		systemSize.setToolTipText("number of particles");
		newSystem.setActionCommand("new");
		newSystem.setToolTipText("Restart the simulation with a new set of particle and source forces");
		putForce.setToolTipText("Create a new source of force at selected location");
		newSystem.addActionListener(this);
		putForce.addActionListener(this);
		add(systemSize);
		add(interForces);
		add(newSystem);
		add(putForce);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if (e.getActionCommand()=="new"){
			if (animationPanel.animation.isAlive()) animationPanel.sw=false;
			animationPanel.system=new PSystem(nextSystemSize,animationPanel.getWidth(),animationPanel.getHeight());
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
					animationPanel.system.add(new SourceForce(animationPanel.mouseX,animationPanel.mouseY,1e4));
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

	public void itemStateChanged(ItemEvent e){
		animationPanel.system.interForces=!animationPanel.system.interForces;
	}

	public void propertyChange(PropertyChangeEvent e){
		nextSystemSize=((Number)systemSize.getValue()).intValue();
	}

	public void pause(int delay){
		try{
			Thread.sleep(delay);
		}
		catch (InterruptedException e){
		}
	}
}

	
