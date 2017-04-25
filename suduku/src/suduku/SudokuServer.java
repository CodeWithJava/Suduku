package suduku;


import java.net.ServerSocket;
import java.sql.*;


public class SudokuServer {
	 public static void main(String[] args) throws Exception {
	        ServerSocket listener = new ServerSocket(8901);
	        System.out.println("Sudoku Server is Running");
	        try {
	  	      Class.forName("com.mysql.jdbc.Driver");     //加载MYSQL JDBC驱动程序   
	  	      //Class.forName("org.gjt.mm.mysql.Driver");
	  	     System.out.println("Success loading Mysql Driver!");
	  	    }
	  	    catch (Exception e) {
	  	      System.out.print("Error loading Mysql Driver!");
	  	      e.printStackTrace();
	  	    }
	  	    try {
	  	    	Connection connect = DriverManager.getConnection(
	  	          "jdbc:mysql://localhost:3306/welikepigDB","root","56911038");
	  	           //连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码
	  	    	System.out.println("Success connect Mysql server!");
	  	    	Statement stmt = connect.createStatement();
	  	    	while (true) {
	                Game game = new Game();
	                Game.Player player1 = game.new Player(listener.accept(), '1');
	         
	                player1.start();
	            }
	  	    }
	        catch (Exception e) {
	      	      System.out.print("get data error!");
	      	      e.printStackTrace();
	      	}    
	        finally {
	            listener.close();
	        }
	    }
}
