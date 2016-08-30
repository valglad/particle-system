package particleSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class MultiOptionDialog extends JDialog implements ActionListener,PropertyChangeListener{
	private JButton okay=new JButton("ok");
	private JFormattedTextField at=new JFormattedTextField(NumberFormat.getNumberInstance());
	private JFormattedTextField nl=new JFormattedTextField(NumberFormat.getNumberInstance());
	private JFormattedTextField dp=new JFormattedTextField(NumberFormat.getNumberInstance());
	private ControlPanel cp;
	public double attraction=10;
	public double distPower=-2;
	public double natLength=0;
	
	public MultiOptionDialog(JFrame frame, String title,ControlPanel c){
		super(frame,title,true);
		cp=c;
		
		Container pane = getContentPane();

		okay.addActionListener(this);
		
		at.setValue(attraction);
		at.setColumns(4);

		nl.setValue(0);
		nl.setColumns(4);

		dp.setValue(-2);
		dp.setColumns(4);

		setLocationRelativeTo(null);
		pane.setLayout(new FlowLayout());
		pane.add(at);
		pane.add(nl);
		pane.add(dp);
		pane.add(okay);
		pack();
	}

	public void actionPerformed(ActionEvent e){
		setVisible(false);
	}

	public void propertyChange(PropertyChangeEvent e){
		Object source=e.getSource();
		if (source==at)
			attraction=((Number)at.getValue()).doubleValue();
		if (source==dp)
			distPower=((Number)dp.getValue()).doubleValue();
		if (source==nl)
			natLength=((Number)nl.getValue()).doubleValue();
	}
}
