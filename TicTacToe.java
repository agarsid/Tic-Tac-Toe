import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * This class models the Tic-Tac-Toe Game server, communicates with the clients
 * and handles their requests.
 * 
 * @author Siddharth Agarwal
 * @version 1.0
 */
class TicTacToe {

	private ServerSocket server;

	private String[] block = new String[9];
	private Player activePlayer;

	/**
	 * The constructor of the class which assigns the ServerSocket to its instance
	 * variable t.
	 * 
	 * @param t2 The ServerSocket of the game
	 */
	public TicTacToe(ServerSocket t2) {
		this.server = t2;
	}

	/**
	 * Initiates the process of creating the two players for the game by waiting for
	 * two sockets to make a connection to the server.
	 * 
	 * @throws IOException On input/output error
	 */
	public void start() throws IOException {

		try {
			var pool = Executors.newFixedThreadPool(200);
			pool.execute(new Player(server.accept(), "X"));
			pool.execute(new Player(server.accept(), "O"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Checks if the Tic-Tac-Toe Game board is full or not.
	 * 
	 * @return false if the game board is full, otherwise true.
	 */
	public boolean hasSpace() {
		for (int i = 0; i < block.length; i++) {
			if (block[i] == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if any of the two players has won the game.
	 * 
	 * @return false if no-one has won, otherwise true.
	 */
	public boolean hasWinner() {

		if (block[4] != null && ((block[2] == block[4] && block[4] == block[6])
				|| (block[0] == block[4] && block[4] == block[8]) || (block[3] == block[4] && block[4] == block[5])
				|| (block[1] == block[4] && block[4] == block[7])))
			return true;

		if (block[2] != null
				&& ((block[0] == block[1] && block[1] == block[2]) || (block[2] == block[5] && block[5] == block[8])))
			return true;

		if (block[6] != null
				&& ((block[0] == block[3] && block[3] == block[6]) || (block[6] == block[7] && block[7] == block[8])))
			return true;

		return false;
	}

	/**
	 * The function checks if the player's move is valid and then updates the
	 * Tic-Tac-Toe Game board on the server. It is synchronized and hence allows
	 * only one and the current player move's to occur.
	 * 
	 * @param player   The current player who has pressed the Tic-Tac-Toe Game
	 *                 board.
	 * @param location The particular block of the board pressed by the player
	 * @throws Exception If the player does not have an opponent ot it is not the
	 *                   player's turn.
	 */
	public synchronized void mark(Player player, int location) throws Exception {
		if (player.name == null || player.againstPlayer.name == null )
			throw new Exception("No Name");		
		else if (player != activePlayer || player.againstPlayer == null || block[location] != null)
			throw new Exception("Invalid");

		block[location] = activePlayer.action;
		activePlayer = activePlayer.againstPlayer;
	}

	class Player implements Runnable {

		Socket socket;
		Scanner input;
		PrintWriter output;

		String name;
		String action;
		Player againstPlayer;

		/**
		 * The constructor of the player class which initializes instance variables and
		 * sets up receiving and sending requests to the socket.
		 * 
		 * @param socket The socket which needs to be connected to the server
		 * @param action Either "X" or "O" which is the mark for the player
		 * @throws IOException input/output error
		 */
		public Player(Socket socket, String action) throws IOException {
			this.socket = socket;
			this.action = action;
			input = new Scanner(socket.getInputStream());
			output = new PrintWriter(socket.getOutputStream(), true);
		}

		/**
		 * Automatically called when the thread (any player) is executed and it begins
		 * server-client connection. It also checks if there a player has left the game.
		 * 
		 */
		@Override
		public void run() {
			try {
				begin();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (againstPlayer != null && againstPlayer.output != null) {
					againstPlayer.output.println("Opp_Left");
				}
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void begin() throws IOException {

			output.println("Hello" + action);

			if (action == "X") {
				activePlayer = this;
			} else {
				againstPlayer = activePlayer;
				againstPlayer.againstPlayer = this;
			}

			while (input.hasNextLine()) {
				String request = input.nextLine();
				if (request.startsWith("Name")) {
					this.name = request.substring(4);
				}
				if (request == ("Exit")) {
					return;
				} else if (request.startsWith("Mark")) {
					doAction(Integer.parseInt(request.substring(5)));
				}
			}
		}

		private void doAction(int location) {
			try {
				mark(this, location);
				output.println("Move");
				againstPlayer.output.println("Opp_Move" + location);
				if (hasWinner()) {
					output.println("Win");
					againstPlayer.output.println("Loss");
				} else if (!hasSpace()) {
					output.println("Draw");
					againstPlayer.output.println("Draw");
				}
			} catch (Exception e) {
				output.println(e.getMessage());
			}
		}
	}
}