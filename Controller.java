import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JOptionPane;
 /**
  * This class controls the client(player) side of the game, sets up the sockets of the clients for the game and handles player actions on the board.
  * 
  * @author Siddharth Agarwal
  * @version 1.0
  */
public class Controller {

	private View view;

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	 /**
	  * The constructor which initializes its instance variable view with an object of the View class
	  * @param view Object of the View class
	  */
	public Controller(View view) {
		this.view = view;
	}
	
	/**
	 * Sets up the sockets of the clients for the game and initializes instance variables.
	 * Contains toy action listeners of some buttons of the game.
	 */
	public void start() {
		try {
			this.socket = new Socket("localhost", 8000);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		view.exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		view.instruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "Some information about the game:\nCriteria for a valid move:\n"
						+ "- The move is not occupied by any mark.\n- The move is made in the player’s turn."
						+ "\n- The move is made within the 3 x 3 board.\n"
						+ "The game would continue and switch among the opposite player"
						+ " until it reaches either one of the following conditions:\n"
						+ "- Player 1 wins.\n- Player 2 wins.\n - Draw.";
				JOptionPane.showMessageDialog(view.frame, message);
			}
		});

		view.submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String n = view.name.getText();
				if (!n.trim().isEmpty()) {
					view.store.setText("Welcome " + n);
					view.frame.setTitle("Tic Tac Toe-Player: " + n);
					view.name.setEditable(false);
					view.submit.setEnabled(false);
					out.println("Name" + n);
					for (JButton b : view.block)
						b.setEnabled(true);
				}
			}

		});

		for (int i = 0; i < 9; i++) {

			final int pressed = i;
			view.block[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					view.blockPressed = view.block[pressed];
					out.println("Mark " + pressed);

				}

			});

		}

	}
	
	/**
	 * Analyzes the responses sent from the server and takes suitable actions to represent these requests to the player on his own screen.
	 * 
	 * @throws Exception Error while closing the socket
	 */
	public void game() throws Exception {
		try {
			String response;
			char move = '0', oppMove = '0';

			while ((response = in.readLine()) != null) {

				if (response.startsWith("Hello")) {

					move = response.charAt(5);
					if (move == 'X')
						oppMove = 'O';
					else
						oppMove = 'X';

				} else if (response.startsWith("No")) {

					view.store.setText("Wait for opponenent to enter name.");
					Thread.sleep(500);
					view.store.setText("WELCOME "+ view.name.getText());

				}else if (response.startsWith("Move")) {

					view.store.setText("Valid move, wait for your opponent.");
					view.blockPressed.setForeground(move == 'X' ? Color.GREEN : Color.RED);
					view.blockPressed.setText("" + move);

				} else if (response.startsWith("Opp_Move")) {

					int index = Integer.parseInt(response.substring(8));
					view.block[index].setForeground(oppMove == 'X' ? Color.GREEN : Color.RED);
					view.block[index].setText("" + oppMove);
					view.store.setText("Your opponent has moved, now is your turn.");

				} else if (response.startsWith("Win")) {

					JOptionPane.showMessageDialog(view.frame, "Congratulations. You win.");
					break;

				} else if (response.startsWith("Loss")) {

					JOptionPane.showMessageDialog(view.frame, "You lose.");
					break;

				} else if (response.startsWith("Draw")) {

					JOptionPane.showMessageDialog(view.frame, "Draw.");
					break;

				} else if (response.startsWith("Opp_Left")) {

					JOptionPane.showMessageDialog(view.frame, "Game Ends. One of the players left.");
					break;

				} else if (response.startsWith("Invalid")) {
					;
				}
			}
			out.println("Exit");

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			this.socket.close();
			view.frame.dispose();
		}
	}
}
