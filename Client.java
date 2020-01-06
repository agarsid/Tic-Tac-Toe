/**
 * This class is used to setup the clients(players) of the Multiplayer Tic-Tac-Toe Game.
 * 
 * @author Siddharth Agarwal
 * @version 1.0
 */
public class Client {
	
	/**
	 * This method instantiates an object of the Controller and View class and sets up the player for the game.
	 * 
	 * @param args Unused
	 */
	public static void main(String[] args){

		View view = new View();
		Controller controller = new Controller(view);
		controller.start();
		try {
			controller.game();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
