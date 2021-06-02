import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SiteSimulator{

	List<String> lines = new ArrayList<String>();

	/* This main class handles the end users inputs and interacts with the backend Site Operations class */

	public static void main(String args[]) throws Exception, IOException { 
		
		/* This sectipn initialises all of the required input (streams) to be used in the section (s) below */
		
		File file = new File(args[0]);
		ReadFromFile read = new ReadFromFile();
		SiteSimulator site = new SiteSimulator();
		SiteOperations operations = new SiteOperations();
		Scanner forward = new Scanner(System.in);
		List<String> command_history = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		site.lines = read.readFile(file);
		operations.createSite(site.lines);
		System.out.println("Digger is now entering the site facing Easterly at co-ordinates 0,0");
		operations.updateLayout(0);
		operations.displayLayout();
		System.out.println("Press any key to proceed");
		String loop = reader.readLine();

		/* This section iterates through the 4 main commands that the user can enter and then interacts accordingly with the simulator logic */

		while (loop != "Q") {
        	System.out.println("Please enter the move you wish to make: Advance (A), Left (L), Right (R), Quit (Q)"); 
        	String move = reader.readLine();
			operations.overhead_cost(1);

			if (move.equals("Q")) {
				System.out.println("exit...");
				command_history.add("Simulator will now exit");
				System.out.println(command_history);
				operations.exit_simulator();
			} 
			else if (move.equals("A")) {
				System.out.println("How many spaces do you want to move?");
				int steps = Integer.parseInt(forward.next());
				operations.overhead_cost(1);
				command_history.add(move + ": Robot moved " + steps + " spaces");
				operations.updateLayout(steps);
				operations.displayLayout();
			} 
			else if (move.equals("L")) {
				System.out.println("Robot will now rotate 90* anti-clockwise");
				command_history.add(move + ": Robot will now rotate 90* anti-clockwise");
				System.out.println(operations.get_current_direction('L'));
				operations.updateLayout(0);
				operations.displayLayout();
			} 
			else if (move.equals("R")) {
				System.out.println("Robot will now rotate 90* clockwise");
				command_history.add(move + ": Robot will now rotate 90* clockwise");
				System.out.println(operations.get_current_direction('R'));
				operations.updateLayout(0);
				operations.displayLayout();
			} 
			else {
				System.out.println("A valid command (A,L,R,Q) has not been entered ... please retry");
				operations.displayLayout();
			}
			operations.get_command_history(command_history);
			
			/* This section will check if the board has been fully cleared and will then exit if fully cleared */

			if (operations.uncleared_cost() <= 3){
				System.out.println("The board has been cleared ... ");

			loop = move;

			}
		}
	}
	
	/* This class handles the functionality to read in the site layout from the text file */
	
	public static class ReadFromFile {
		List<Integer> line_lengths = new ArrayList<Integer>();
		List<String> lines = new ArrayList<String>();

		public List<String> readFile(File file) throws FileNotFoundException {
			
			/* This section handles and exceptions that might occur */
			
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				StringBuffer sb = new StringBuffer();
				String line;
				while((line = br.readLine())!=null){
					line_lengths.add(line.length());
					lines.add(line);
					sb.append(line);
					sb.append('\n');
				}
				fr.close();
				
				for (Integer outer : line_lengths) {
					if (outer != line_lengths.get(0)){
				 		System.out.println("Not all lines are equal length");
				 		System.out.println("Please check the input File");
				 		System.out.println("This program will now terminate ...");
				 		System.exit(0);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return lines;
		}
	}
}