/*	DB1.java
	Author:		Brian Newby
	Date:		4/16/2017
	This program reads from StudentReg database and creates a report of preregistered students.	*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.io.*;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DB1 extends JPanel
{
	
	/*	Declare variables*/
	private String fullName = "";
	private String studID = "";
	private	int studentCount = 0;
	private	int enrollCount = 0;
	private	Connection connect = null;
	private JButton reportButton, exitButton;
	
	public DB1() 
	{
		
		
		/*Size and color of the JPanel*/
		setPreferredSize(new Dimension(400, 70));
		setBackground(Color.lightGray);
		
		/*Button objects*/
		reportButton = new JButton("PRINT REPORT");
		exitButton = new JButton("EXIT");
		
		/*Add buttons to JPanel*/
		add(reportButton);
		add(exitButton);
		
		/*Declare button listeners.*/
		reportButton.addActionListener(new ReportListener());
		exitButton.addActionListener(new ExitListener());	
	}
	
	
	/*Listener for the exit button. It closes the application*/
	private class ExitListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			System.exit(0);
		}
	}
	
	/* Listener for the button to create RegisteredReport.txt*/
	private class ReportListener implements ActionListener
	{
		/*	*/
		public void actionPerformed(ActionEvent event)
		{
			/*Create instance of today's date and date object*/
			Calendar now = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			
			try
			{
				/*Open stream to output to text file.*/
				PrintWriter outFile = null;
				outFile = new PrintWriter(new FileWriter("RegistrationReport.txt"));
				
				/* Set up a connection to the StudentReg database*/
				connect = DriverManager.getConnection("jdbc:mysql://localhost/studentsreg","root","");
						
				if (connect != null)
				{		
					//Print today's date and report header.
					outFile.println("Date of report: " + df.format(now.getTime()));
					outFile.println();
					outFile.printf("%-20s %-24s %-10s %-10s", "Student ID", "Name", "Major", "Course");
					outFile.println();
					outFile.println("---------------------------------------------------------------------");
				
					/*	Process SQL statement against the database.	*/
					Statement statement = connect.createStatement();
					
					/*	Statements joining the Students table and Registration table on an inner join. */
					String 	   sqlStmnt = "SELECT Students.StudentID, Students.FirstName, Students.LastName, Students.Major,";
					sqlStmnt = sqlStmnt + " Registration.CourseID";
					sqlStmnt = sqlStmnt + " FROM Students INNER JOIN Registration";
					sqlStmnt = sqlStmnt + " ON Students.StudentID = Registration.StudentID";
					sqlStmnt = sqlStmnt + " ORDER BY Students.StudentID;";
					
					statement.execute(sqlStmnt);
				
					ResultSet rs = statement.getResultSet();
				
				/*	Print the report.  */
				if (rs != null)
				{
					while (rs.next())
					{
						/* Compare strings to prevent printing repetitive data */
						if (!(rs.getString(1).equals(studID)))
						{
							fullName = rs.getString(2) + " " + rs.getString(3);
							
							studID = rs.getString(1);
							outFile.println("---------------------------------------------------------------------");
							outFile.printf("%-19s %-25s %-10s %-10s", rs.getString(1), fullName, rs.getString(4), 
											rs.getString(5));
							outFile.println();
							studentCount++;
							enrollCount++;
						}
						else
						{							
							
							outFile.printf("%63s", rs.getString(5));
							outFile.println();
							enrollCount++;
						}						
					}
					
					/* print student count and courses count */
					outFile.println();
					outFile.printf("%-20s %-5s", "Number of students enrolled:", studentCount);
					outFile.println();
					outFile.printf("%-20s %-5s", "Count of enrollment in classes:", enrollCount);
				}
			}
				/* Close database connection and output stream. */
				connect.close();
				outFile.close();				
				
			}
			
			//	Error checking
			catch (SQLException ex) {
				System.out.println("SQLException: " + ex.getMessage());
				ex.printStackTrace();

			} 
			catch (Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
				ex.printStackTrace();
			}
			//	Dialog box with message indicating report is ready.
			JOptionPane.showMessageDialog(null, "Pre-registered Student Report is ready. Open RegistrationReport.txt to read" +
												 " the report.");
		}
	}
}