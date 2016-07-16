package particleSystem;

import javax.swing.JFrame;
import java.awt.EventQueue;

public class PSApp extends JFrame{
		public boolean walls=true;

	public static void main(String[] args){
		if (args.length>0) PSystem.particleSize=Integer.parseInt(args[0]);
		else PSystem.particleSize=5;
        JFrame frame = new PSApp();
        frame.setVisible(true); 
	}

	private PSApp(){
		super("Particle System Generator");
		add(new PSAppPanel(walls));
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*JLabel label=new JLabel("A label");
		label.setOpaque(true);
		mainFrame.getContentPane().add(label);*/
	}
}
