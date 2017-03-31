package suduku;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Game {

   
    int[] oriData;
    
    int[] results;
    
   
    class Player extends Thread {
        char mark;
        Socket socket;
        
        BufferedReader input;
        PrintWriter output;

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, char mark) {
        	//Store the data
        	oriData = new int[81];
        	results = new int[81];
        	int data[][] = { { 8, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 3, 6, 0, 0, 0, 0, 0 }, { 0, 7, 0, 0, 9, 0, 2, 0, 0 },

        			{ 0, 5, 0, 0, 0, 7, 0, 0, 0 }, { 0, 0, 0, 0, 4, 5, 7, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0, 3, 0 },

        			{ 0, 0, 1, 0, 0, 0, 0, 6, 8 }, { 0, 0, 8, 5, 0, 0, 0, 1, 0 }, { 0, 9, 0, 0, 0, 0, 4, 0, 0 }, };
        	
        	int[][] data2 =  { { 8, 1, 2, 7, 5, 3, 6, 4, 9 }, { 9, 4, 3, 6, 8, 2, 1, 7, 5 }, { 6, 7, 5, 4, 9, 1, 2, 8, 3 },

        			{ 1, 5, 4, 2, 3, 7, 8, 9, 6 }, { 3, 6, 9, 8, 4, 5, 7, 2, 1 }, { 2, 8, 7, 1, 6, 9, 5, 3, 4 },

        			{ 5, 2, 1, 9, 7, 4, 3, 6, 8 }, { 4, 3, 8, 5, 2, 6, 9, 1, 7 }, { 7, 9, 6, 3, 1, 8, 4, 0, 0 }, };
            for(int i=0;i<9;i++){
            	for(int j=0;j<9;j++){
            		oriData[i*9+j] = data2[i][j];
            	}
            } 
            DLX dlx = new DLX(9 * 9 * 9 + 1, 4 * 9 * 9);
    		dlx.setLimitation(1);
    		dlx.solve(data);
    		List<int[][]> solutions = dlx.getSolutions();
        	int[][] temp = solutions.get(0);
        	for(int i=0;i<9;i++){
            	for(int j=0;j<9;j++){
            		results[i*9+j] = temp[i][j];
            	}
            } 
        	//Handle the socket
        	
            this.socket = socket;
            this.mark = mark;
            try {
                
            	input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME " + mark);
               
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
            
        }

        
        public void run() {
            try {
            	output.println("MESSAGE Game Start");
            	
            	while (true) {
            		String info =input.readLine();
            		System.out.println("The response is "+ info);
            		if(info.startsWith("REQUEST")){
	            		
	                	output.println("SENDING GAME");
	                	StringBuilder sb= new StringBuilder();
	                	for(int i=0;i<oriData.length;i++){
	                		sb.append(oriData[i]);
	                	}
	                	output.println(sb.toString());
	                	System.out.println("The response is "+ info);
            		}
            		
            		else if (info.startsWith("FINISH")){
            			String res= input.readLine();
	                    String ans= "Right";
	                    for (int i = 0; i < res.length(); ++i){
	                    	int v = res.charAt(i) - '0';
	                    	if(v!= results[i]) ans="Wrong";
	                    }
	                    output.println("RESULT COMING");
	                    output.println(ans);
                    }else if (info.startsWith("QUIT")) {
                        return;
                    }
            	}
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
            	try {
            		socket.close();
                }
            	catch(IOException e) {}
            }
        }
        
    }
}