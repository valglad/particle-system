package particleSystem;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.text.NumberFormat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class ControlPanel extends JPanel implements ActionListener,ItemListener,PropertyChangeListener{
	private static JButton newSystem=new JButton("New system");
	private static JButton save=new JButton("Save");
	private static JButton load=new JButton("Load");
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
	private MultiOptionDialog saveLoadName;
	private int nextSystemSize=PSystem.defaultSize;
	private double nextUnitDistance;

	public ControlPanel(PSAppPanel p){
		super();

        Number[] vals = new Number[3];
        vals[0] = (Number)10;
        vals[1] = (Number)(-2);
        vals[2] = (Number)0;
		forceParameters = new MultiOptionDialog(3, vals, new JFrame(), "Force Parameters", this);

        String[] vals2 = new String[1];
        vals2[0] = "";
		saveLoadName=new MultiOptionDialog(1, vals2, new JFrame(), "Save/Load file name", this);

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

		save.setActionCommand("save");
		save.setToolTipText("Save the system in its current state");
		save.addActionListener(this);

		load.setActionCommand("load");
		load.setToolTipText("Load a previously saved system");
		load.addActionListener(this);

		putForce.setToolTipText("Create a new source of force at selected location");
		putForce.addActionListener(this);

		add(systemSize);
		add(interForceValue);
		add(interForces);
		add(walls);
		add(gravity);
        add(merge);
		add(newSystem);
		add(save);
		add(load);
		add(putForce);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if (e.getActionCommand() == "new"){
            //New System
			if (animationPanel.animation.isAlive()) animationPanel.sw = false;
			PSystem system = new PSystem(nextSystemSize, animationPanel.getWidth(), animationPanel.getHeight(), nextUnitDistance, animationPanel.system.cMngr);
			animationPanel.restart(system);

        } else if (e.getActionCommand() == "save"){
            //Save System
            boolean toRestart = false;
            if (animationPanel.animation.isAlive()){
                animationPanel.stop();
                toRestart = true;
            }
            saveLoadName.awaitingInput = true;
            System.out.println(saveLoadName.awaitingInput);
            saveLoadName.setVisible(true);
            while (saveLoadName.awaitingInput) {}
            String name = (String)saveLoadName.fields[0].getValue();
            SavedSystems.save(animationPanel.system, name);
            if (toRestart){
                animationPanel.restart();
            }

        } else if (e.getActionCommand() == "load"){
            //Load System
            if (animationPanel.animation.isAlive()){
                animationPanel.sw = false;
            }

            saveLoadName.awaitingInput = true;
            saveLoadName.setVisible(true);
            while (saveLoadName.awaitingInput) {}
            String name = (String)saveLoadName.fields[0].getValue();
            PSystem system = (PSystem)SavedSystems.load(name);
            system.cMngr = animationPanel.system.cMngr;
            animationPanel.restart(system);

		} else {
			Thread thread=new Thread(new Runnable(){
				@Override
				public void run(){
					boolean toRestart = false;
					if (animationPanel.animation.isAlive()){ //animation will be stopped while force is being put
						toRestart = true;
						animationPanel.stop();
					}

					forceParameters.setVisible(true);
					animationPanel.awaitingInput = true;
					while (animationPanel.awaitingInput){}
                    Double attraction = ((Number)forceParameters.fields[0].getValue()).doubleValue();
                    Double distPower = ((Number)forceParameters.fields[1].getValue()).doubleValue();
                    Double natLength = ((Number)forceParameters.fields[2].getValue()).doubleValue();
					animationPanel.system.add(new SourceForce(animationPanel.mouseX, animationPanel.mouseY, attraction, distPower, natLength));

                    if (toRestart){
                        animationPanel.restart();
					} else animationPanel.repaint();

				}
			});

			thread.start();
		}
	}

	public void itemStateChanged(ItemEvent e){
		Object source = e.getItemSelectable();
		if (source == interForces) animationPanel.system.interForces = !animationPanel.system.interForces;
		else if (source == walls) animationPanel.walls = !animationPanel.walls;
		else if (source == gravity) animationPanel.system.gravityOn = !animationPanel.system.gravityOn;
        else animationPanel.merge = !animationPanel.merge;
	}

	public void propertyChange(PropertyChangeEvent e){
		Object source = e.getSource();
		if (source == systemSize)
			nextSystemSize = ((Number)systemSize.getValue()).intValue();
		else if (source == interForceValue){
			animationPanel.system.interForceValue = ((Number)interForceValue.getValue()).doubleValue();
			animationPanel.system.changeInterForces = true;
		}else
			nextUnitDistance = ((Number)unitDistance.getValue()).doubleValue();
	}

	public void pause(int delay){
		try{
			Thread.sleep(delay);
		}
		catch (InterruptedException e){
		}
	}
}
