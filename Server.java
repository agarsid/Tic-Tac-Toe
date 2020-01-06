import java.io.IOException;
import java.net.ServerSocket;

/**
 * This class is used to setup the server of the multiplayer Tic-Tac-Toe Game.
 * 
 * @author Siddharth Agarwal
 * @version 1.0
 */
public class Server {
	
	/**
	 * This method instantiates an object of the TicTacToe class and starts the server.
	 * 
	 * @param args Unused
	 * @throws IOException On Input Error if the port chosen is not allowed.
	 */
	public static void main(String[] args) throws IOException {

		System.out.println("Tic Tac Toe Server is Running...");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("Server Stopped.");
			}
		}));

		try (var listener = new ServerSocket(8000)) {
			while (true) {
				TicTacToe game = new TicTacToe(listener);
				game.start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}