package particleSystem;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.text.NumberFormat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class ControlPanel extends JPanel implements ActionListener,ItemListener,PropertyChangeListener{
	private static JButton newSystem=new JButton("New system");
	private static JButton putForce=new JButton("Create a force source");
	private static JCheckBox interForces=new JCheckBox("Interforces");
	private static JCheckBox walls=new JCheckBox("Walls");
	private static JCheckBox gravity=new JCheckBox("Gravity");
	private static JCheckBox merge=new JCheckBox("Merge");
	private JFormattedTextField unitDistance=new JFormattedTextField(NumberFormat.getNumberInstance());
	private JFormattedTextField systemSize=new JFormattedTextField(NumberFormat.getNumberInstance());
	private JFormattedTextField interForceValue=new JFormattedTextField(NumberFormat.getNumberInstance());
	private PSAppPanel animationPanel;
	private MultiOptionDialog forceParameters;
	private int nextSystemSize=PSystem.defaultSize;
	private double nextUnitDistance;

	public ControlPanel(PSAppPanel p){
		super();
		forceParameters=new MultiOptionDialog(new JFrame(),"Force Parameters",this);

		animationPanel=p;
		nextUnitDistance=animationPanel.system.unitDistance;
		
		interForces.setSelected(animationPanel.system.interForces);
		interForces.addItemListener(this);

		gravity.setSelected(animationPanel.system.gravityOn);
		gravity.addItemListener(this);

		walls.setSelected(animationPanel.walls);
		walls.addItemListener(this);

		merge.setSelected(animationPanel.walls);
		merge.addItemListener(this);

		systemSize.setValue(PSystem.defaultSize);
		systemSize.setColumns(4);
		systemSize.addPropertyChangeListener("value",this);
		systemSize.setToolTipText("number of particles");

		unitDistance.setValue(nextUnitDistance);
		unitDistance.setColumns(4);
		unitDistance.addPropertyChangeListener("value",this);
		unitDistance.setToolTipText("pixel length of the unit distance");

		interForceValue.setValue(animationPanel.system.interForceValue);
		interForceValue.setColumns(4);
		interForceValue.addPropertyChangeListener("value",this);
		interForceValue.setToolTipText("strength of inter-particle forces");

		newSystem.setActionCommand("new");
		newSystem.setToolTipText("Restart the simulation with a new set of particle and source forces");
		newSystem.addActionListener(this);

		putForce.setToolTipText("Create a new source of force at selected location");
		putForce.addActionListener(this);

		add(systemSize);
		add(interForceValue);
		add(interForces);
		add(walls);
		add(gravity);
        add(merge);
		add(newSystem);
		add(putForce);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if (e.getActionCommand()=="new"){
			if (animationPanel.animation.isAlive()) animationPanel.sw=false;
			animationPanel.system=new PSystem(nextSystemSize,animationPanel.getWidth(),animationPanel.getHeight(),nextUnitDistance,animationPanel.system.cMngr);
			animationPanel.animation=new Thread(animationPanel,"animation");
			animationPanel.repaint();
		}else{
			Thread thread=new Thread(new Runnable(){
				@Override
				public void run(){
					boolean toRestart=false;
					if (animationPanel.animation.isAlive()){ //animation will be stopped while force is being put
						toRestart=true;
						animationPanel.sw=false;
					}
					forceParameters.setVisible(true);
					animationPanel.forceBeingSet=true;
					while (animationPanel.forceBeingSet){}
					animationPanel.system.add(new SourceForce(animationPanel.mouseX,animationPanel.mouseY,forceParameters.attraction*10,forceParameters.distPower,forceParameters.natLength));
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
		Object source=e.getItemSelectable();
		if (source == interForces) animationPanel.system.interForces=!animationPanel.system.interForces;
		else if (source==walls) animationPanel.walls=!animationPanel.walls;
		else if (source==gravity) animationPanel.system.gravityOn=!animationPanel.system.gravityOn;
        else animationPanel.merge=!animationPanel.merge;
	}

	public void propertyChange(PropertyChangeEvent e){
		Object source=e.getSource();
		if (source==systemSize)
			nextSystemSize=((Number)systemSize.getValue()).intValue();
		else if (source==interForceValue){
			animationPanel.system.interForceValue=((Number)interForceValue.getValue()).doubleValue();
			animationPanel.system.changeInterForces=true;
		}else
			nextUnitDistance=((Number)unitDistance.getValue()).doubleValue();
	}

	public void pause(int delay){
		try{
			Thread.sleep(delay);
		}
		catch (InterruptedException e){
		}
	}
}
