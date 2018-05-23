/*
	DB1Driver.java
	Author:	Brian Newby
	Date:	4/17/2017
	Demonstrates a graphical user interface and an event listener.
*/

import javax.swing.JFrame;
import java.io.*;
public class DB1Driver 
{
	/*Creates the frame for the DB1 program*/
	public static void main(String[] args) throws IOException
	{
		JFrame frame = new JFrame ("Create Pre-registered Student Report");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DB1 panel = new DB1();
		frame.getContentPane().add(panel);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}