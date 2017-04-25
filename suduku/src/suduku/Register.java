package suduku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Register extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int user=0;
	String ID;
	String name;
	JTextField nameTag;
	boolean finishRegistration = false;
 
	public Register(){//We put a welcome image and a JButton to jump to another JFrame
		Container c=getContentPane();

		
		JButton s=new JButton("finish");
		JLabel l1=new JLabel("Sudoku Registration",JLabel.CENTER);
		JLabel l2=new JLabel("Name",JLabel.CENTER);
		JTextField nameTag = new JTextField("PLEASE ENTER YOUR NAME");
		nameTag.setFont(new Font("TimesRoman", Font.PLAIN, 44));
		nameTag.setEditable(true);
		
		Font font=new Font("Sudoku Registration",Font.CENTER_BASELINE,40);
		Font font1=new Font("name",Font.ITALIC,25);
		Font font2=new Font("finish",Font.CENTER_BASELINE,30);
		l1.setFont(font);
		l2.setForeground(Color.WHITE);
		l2.setFont(font1);
		
		s.setFont(font2);
		s.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user++;
				name= nameTag.getText();
				ID=String.valueOf(user);
				finishRegistration = true;
				dispose();
			}
		});
		s.setBackground(Color.gray);
		
		JPanel p1=new JPanel();
		
		JPanel p2= new JPanel();
		JPanel p3=new JPanel();
         
		p1.setLayout(new BorderLayout());
		p1.setBackground(Color.lightGray);
		p1.add(l1,BorderLayout.NORTH);
		p1.add(l2,BorderLayout.SOUTH);
		p2.add(nameTag, BorderLayout.CENTER);
		p3.add(s);
		p3.setBackground(Color.lightGray);
        

		c.add(p1,BorderLayout.NORTH);
		c.add(p2,BorderLayout.CENTER);
		c.add(p3,BorderLayout.SOUTH); 
		
		
		setTitle("WELCOME TO REGISTRATION");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		setSize(610,700);
	    setResizable(false);
	    setVisible(true);
	    setLocationRelativeTo(null);

	}
	
	public static void main(String[] args) throws Exception {
		Register r = new Register();
		while (true) {
			Thread.sleep(1000);
			System.out.println(r.finishRegistration);
			if(r.finishRegistration){
	            String serverAddress =  "localhost";
	            System.out.println(r.ID);
	            System.out.println(r.name);
	            SudokuClient client = new SudokuClient(serverAddress,r.ID,r.name);
	            JFrame frame = new JFrame("Sudoku");
	            frame.getContentPane().add(client, "Center");
	            frame.getContentPane().add(client.messageLabel, "South");
	            frame.setLocationByPlatform(true);
	            frame.setSize(450, 400);
	            frame.setResizable(true);
	            frame.setVisible(true);
	  
	            client.play(frame);
	            int response = JOptionPane.showConfirmDialog(frame,
	                    "Want to play one more game?",
	                    "Congratulations, You Win",
	                    JOptionPane.YES_NO_OPTION);
	            frame.dispose();
	            if( response != JOptionPane.YES_OPTION){
	            	break;
	            }
			}
        }
    }
}
