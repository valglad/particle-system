package particleSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class MultiOptionDialog extends JDialog implements ActionListener{
	private JButton okay=new JButton("ok");
	public volatile boolean awaitingInput=false;
    protected int fieldNum; //number of fields
    protected JFormattedTextField[] fields;
    protected Object[] fieldVals;
	private ControlPanel cp;
//NumberFormat.getNumberInstance();
	
	public MultiOptionDialog(int n, Object[] defaultVals, JFrame frame, String title, ControlPanel c){
        // field types can be 'n' or 's' for number or string
		super(frame, title, true);
		cp = c;
        fieldNum = n;
        fields = new JFormattedTextField[n];
        fieldVals = new Object[n];

		Container pane = getContentPane();

		okay.addActionListener(this);

		setLocationRelativeTo(null);
		pane.setLayout(new FlowLayout());
		pane.add(okay);

        for (int i = 0; i < n; i++){
	        fields[i] = new JFormattedTextField(defaultVals[i]);
            fields[i].setColumns(6);
	        fieldVals[i] = defaultVals[i];
            pane.add(fields[i]);
        }
		
		pack();
	}

	public void actionPerformed(ActionEvent e){
        awaitingInput = false;
		setVisible(false);
	}

/*
	public void propertyChange(PropertyChangeEvent e){
		Object source=e.getSource();
		if (source==at)
			attraction=((Number)source.getValue()).doubleValue();
		if (source==dp)
			distPower=((Number)dp.getValue()).doubleValue();
		if (source==nl)
			natLength=((Number)nl.getValue()).doubleValue();
	}
*/

}
