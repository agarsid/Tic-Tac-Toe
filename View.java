import javax.swing.*;
import java.awt.*;
 /**
  * This class sets up the GUI framework of the Tic-Tac-Toe Game.
  * 
  * @author Siddharth Agarwal
  * @version 1.0
  */
public class View {
	
	public JFrame frame;

	private JMenuBar menuBar;
	private JMenu control;
	private JMenu help;
	public JMenuItem exit;
	public JMenuItem instruction;

	private JPanel north;
	private JPanel center;
	private JPanel south;

	public JLabel store;
	
	public JButton block[];
	public JButton blockPressed;

	public JTextField name;
	public JButton submit;
	/**
	 * The View class constructor which initializes the instance variables and calls the go() function to set up the GUI of the game for the player.
	 */
	public View() {
		
		frame = new JFrame();

		menuBar = new JMenuBar();
		control = new JMenu("Control");
		help = new JMenu("Help");
		exit = new JMenuItem("Exit");
		instruction = new JMenuItem("Instruction");

		north = new JPanel();
		center = new JPanel();
		south = new JPanel();

		store = new JLabel("Enter your player name...");
		block = new JButton[9];

		submit = new JButton("Submit");
		name = new JTextField(20);

		go();
	}

	private void go() {
		north.setLayout(new FlowLayout(FlowLayout.LEADING));
		north.add(store);

		center.setLayout(new GridLayout(3, 3));

		for (int i = 0; i < 9; i++) {
			block[i] = new JButton("");
			block[i].setFont(new Font("Arial", Font.PLAIN, 30));
			block[i].setFocusPainted(false);
			block[i].setEnabled(false);
			center.add(block[i]);
		}

		south.add(name);
		south.add(submit);

		control.add(exit);
		help.add(instruction);
		menuBar.add(control);
		menuBar.add(help);

		frame.add(north, BorderLayout.NORTH);
		frame.add(center, BorderLayout.CENTER);
		frame.add(south, BorderLayout.SOUTH);

		frame.setJMenuBar(menuBar);
		frame.setTitle("Tic Tac Toe");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(400, 400);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
	}

}
