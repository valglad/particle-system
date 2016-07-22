package particleSystem;

import javax.swing.JFrame;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class PSApp extends JFrame{
		public boolean walls=false;
		public static int width=700;
		public static int height=500;

	public static void main(String[] args){
		if (args.length>0) PSystem.particleSize=Integer.parseInt(args[0]);
		else PSystem.particleSize=5;
		PSystem.size=50;
        JFrame frame = new PSApp();
        frame.setVisible(true); 
	}

	private PSApp(){
		super("Particle System Generator");
		getContentPane().setLayout(new BorderLayout());
		setPreferredSize(new Dimension(width,height));
		PSAppPanel p=new PSAppPanel(walls);
		getContentPane().add(p,BorderLayout.CENTER);
		getContentPane().add(new ControlPanel(p),BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*JLabel label=new JLabel("A label");
		label.setOpaque(true);
		mainFrame.getContentPane().add(label);*/
	}
}
