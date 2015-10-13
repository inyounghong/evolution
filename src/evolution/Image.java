package evolution;

import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Image extends JFrame {

	public Image() {

		this.getContentPane().setLayout(new FlowLayout());
		JLabel label1 = new JLabel("Example Text");
		ImageIcon icon = new ImageIcon(getClass().getResource("fish.png"));
		JLabel label2 = new JLabel(icon);

		add(label1);
		add(label2);
	}

	private static void createAndShowGUI() {

		JFrame frame = new Image();
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(); 
			}
		});
    }
}