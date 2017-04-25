package suduku;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;





public class SudokuClient extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int width = 9;
	private static final int height = 9;
	private int data[][] = new int[height][width];
	private boolean enable[][] = new boolean[height][width];
	private String infoStr = "";
	private final String ID;
	private final String NAME;
	
	
	
	JButton finish = new JButton("Finish"), request = new JButton("request"), clean = new JButton("Clean");
	JTextField sudokus[][] = new JTextField[height][width];
	JTextPane info = new JTextPane();
	JButton save = new JButton("Save"), load = new JButton("Load");
	JLabel messageLabel = new JLabel("");

    private static int PORT = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    

    /**
     * Constructs the client by connecting to a server, laying out the
     * GUI and registering GUI listeners.
     */
    public SudokuClient(String serverAddress,String iD2, String name) throws Exception {
    	ID=iD2;
    	NAME= name;
        // Setup networking
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
       
        // Layout GUI
    	messageLabel.setBackground(Color.lightGray);
    	//frame.getContentPane().add(messageLabel, "South");
    	messageLabel.setText("Welcome, "+ NAME);
    	setLayout(null);
        for (int r = 0; r < 3; ++r)
		{
			for (int c = 0; c < 3; ++c)
			{
				JPanel jp = new JPanel();
				jp.setLayout(null);
				jp.setBounds(r * 95 + 5, c * 95 + 5, 95, 95);
				jp.setBorder(new LineBorder(Color.black));

				for (int x = 0; x < 3; ++x)
					for (int y = 0; y < 3; ++y)
					{
						int j = r * 3 + x, i = c * 3 + y;
						sudokus[i][j] = new JTextField();
						sudokus[i][j].setBounds(30 * x + 5, 30 * y + 5, 25, 25);
						sudokus[i][j].setDocument(new NumberLenghtLimitedDmt());
						jp.add(sudokus[i][j]);
					}
				add(jp);
			}
		}
        
        finish.setBounds(85, 300, 75, 25);
        finish.addActionListener(new finishAL());
        add(finish);

		request.setBounds(175, 300, 75, 25);
		request.addActionListener(new requestAL());
		add(request);

		clean.setBounds(265, 300, 75, 25);
		clean.addActionListener(new cleanAL());
		add(clean);
		
		save.setBounds(100, 330, 75, 25);
		save.addActionListener(new saveAL());
		add(save);
	
		load.setBounds(200, 330, 75, 25);
		load.addActionListener(new loadAL());
		add(load);
		
		JScrollPane jsp = new JScrollPane(info);
		info.setEditable(false);
		jsp.setBounds(300, 5, 140, 285);
		jsp.setBorder(new TitledBorder("Info"));
		add(jsp);
		
		
    }

    
    public void play(JFrame frame) throws Exception {
        String response;
        try {
            response = in.readLine();
            if (response.startsWith("WELCOME: "+ NAME)) {
                char mark = response.charAt(8);
                
                frame.setTitle("Sudoku - Player " + mark);
            }
            while (true) {
                response = in.readLine();
                System.out.println("The response is "+ response);
                if (response.startsWith("SENDING")) {
                    messageLabel.setText("Start to play");
                    String array = in.readLine();
                    int len = array.length();
                    for(int i=0;i<len;i++){
                    	int r= i/9;
                    	int c= i%9;
                    	data[r][c]= array.charAt(i) - '0';
                    	//System.out.print(data[r][c]+" ");
                    	sudokus[r][c].setEnabled(true);
        				sudokus[r][c].setEditable(true);
        				if (data[r][c] == 0)
        				{
        					sudokus[r][c].setText("");
        				} else
        				{
        					sudokus[r][c].setText(Integer.toString(data[r][c]));
        					enable[r][c] = true;
        					sudokus[r][c].setEnabled(false);
        				}
                    }
                    //System.out.println();
                } else if (response.startsWith("RESULT")) {
                    response= in.readLine();
                   
                    messageLabel.setText("The result is : "+response);
                    if(response.equals("Right")){
                    	break;
                    }
                }
            }
            out.println("QUIT");
        }
        finally {
            socket.close();
        }
    }

    class finishAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			infoStr += "Finish the game, want to compare with Result.\n";
			info.setText(infoStr);
			//finish.setEnabled(false);
			out.println("FINISH GAME");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < height; ++i)
			{
				for (int j = 0; j < width; ++j)
				{
					try
					{
						int x = Integer.parseInt(sudokus[i][j].getText());
						sb.append(x);
					} catch (NumberFormatException ex)
					{
						
					}
				}
			}
			out.println(sb.toString());
		}
	}

	class requestAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			infoStr += "Request some new sudoku data.\n";
			info.setText(infoStr);
			out.println("REQUEST GAME");
		}
	}

	class saveAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			infoStr += "Save sudoku data.\n";
			info.setText(infoStr);
			for (int i = 0; i < height; ++i)
			{
				for (int j = 0; j < width; ++j)
				{
					try
					{
						data[i][j] = Integer.parseInt(sudokus[i][j].getText());
						
					} catch (NumberFormatException ex)
					{
						
					}
				}
			}		}
	}
	
	class loadAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			infoStr += "Load sudoku data.\n";
			info.setText(infoStr);
			for (int i = 0; i < height; ++i)
			{
				for (int j = 0; j < width; ++j)
				{
					try
					{
						if (data[i][j] == 0)
        				{
        					sudokus[i][j].setText("");
        				} else
        				{
        					sudokus[i][j].setText(Integer.toString(data[i][j]));
        				}
						if(enable[i][j]) sudokus[i][j].setEnabled(false);
						
					} catch (NumberFormatException ex)
					{
						
					}
				}
			}
		}
	}
	
	class cleanAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			infoStr = "Clean sudoku data.\n";
			info.setText(infoStr);

			int i, j;
			for (i = 0; i < height; ++i)
			{
				for (j = 0; j < width; ++j)
				{
					sudokus[i][j].setText("");
					sudokus[i][j].setEnabled(true);
					sudokus[i][j].setEditable(true);
				}
			}
		}
	}
 
    
    
    
    class NumberLenghtLimitedDmt extends PlainDocument
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public NumberLenghtLimitedDmt()
		{
			super();
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
		{
			if (str == null)
			{
				return;
			}
			char[] upper = str.toCharArray();
			if (upper[0] > '0' && upper[0] <= '9')
			{
				super.remove(0, getLength());
				super.insertString(0, new String(upper, 0, 1), attr);
			}
		}
	}
}




