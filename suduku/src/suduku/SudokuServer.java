package suduku;


import java.net.ServerSocket;



public class SudokuServer {
	 public static void main(String[] args) throws Exception {
	        ServerSocket listener = new ServerSocket(8901);
	        System.out.println("Sudoku Server is Running");
	        try {
	            while (true) {
	                Game game = new Game();
	                Game.Player player1 = game.new Player(listener.accept(), '1');
	         
	                player1.start();
	            }
	        } finally {
	            listener.close();
	        }
	    }
}
