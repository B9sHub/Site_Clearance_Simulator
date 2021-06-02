import java.util.ArrayList;
import java.util.List;

class SiteOperations
{
	/* This section initializes the required class variables for ease of use / reference */

	List<String> command_history = new ArrayList<String>();
	private char current_direction = 'E';
	private String directions = "NESW";
	private int new_pos = 0;
	private int current_x = 0;
	private int current_y = 0;
	private int plain = 1;
	private int rocky = 2;
	private int removable_tree = 2;
	private int uncleared = 3;
	private int protected_tree = 10;
	private int paint_damage = 2;
	private int fuel_usage = 0;
	private int overhead_cost = 0;
	private int total_cost = 0;
	private int uncleared_cost = 0;
	char [][] currentGrid;
	
	/* This creates the site initially from the text file input */
	
    public char[][] createSite(List<String> lines){		
		int rows = lines.size();
		int columns = lines.get(0).length();
        char[][] s = new char[rows][];

        for (int loop = 0; loop < rows; loop++){
			s[loop] = new char[columns];
		}
        for(int i = 0; i < s.length; i++){
            for(int j=0; j < s[i].length; j++){
                s[i][j] = lines.get(i).charAt(j);
            }
        }    
		this.currentGrid = s;
        return s;
    }

	/* This will be used continuously to display the current state of the grid / site */

    public void displayLayout(){
		char a[][] = this.currentGrid;
        for(int i = 0; i < a.length; i++){
            for(int j=0; j < a[i].length; j++){
				System.out.print(a[i][j] + "  ");
            }
            System.out.println('\n');   
		}
    }

	/* 
	This will be used continuously to update the current state of the grid / site 
	There is repeated code that could be refactored (simplfied) at a later date
	*/

	public char[][] updateLayout(int cells){
		char a[][] = this.currentGrid;
		if (this.current_direction == 'N') {
			for (int loop = 0; loop <= cells; loop++){
				if ((this.current_x - loop) >= 0){
					this.calculateCost(a[this.current_x - loop][this.current_y]);
					a[current_x-loop][current_y] = ' ';
				} else {
					System.out.println("Digger has crossed the site boundary. Simulator will now terminate");
					this.displayLayout();
					this.exit_simulator();
				}
			}
			if ((this.current_x - cells) < 0){
				System.out.println("Digger has crossed the site boundary. Simulator will now terminate");
				this.displayLayout();
				this.exit_simulator();
			}
			this.current_x -= cells;
			a[this.current_x][this.current_y] = 'N';
			this.currentGrid = a;
			return a;
		} 
		if (this.current_direction == 'E') {
			for (int loop = 0; loop <= cells; loop++){
				if ((this.current_y + loop) < a[0].length){
					this.calculateCost(a[this.current_x][this.current_y + loop]);
					a[this.current_x][this.current_y + loop] = ' ';
				} else {
					System.out.println("Digger has crossed the site boundary. Simulator will now terminate");
					this.displayLayout();
					this.exit_simulator();
				}
			}
			if ((this.current_y + cells) >= a[0].length){
				System.out.println("Digger has crossed the site boundary. Simulator will now terminate");
				this.displayLayout();
				this.exit_simulator();
			}
			this.current_y += cells;
			a[this.current_x][this.current_y] = 'E';
			this.currentGrid = a;
			return a;
		} 
		if (this.current_direction == 'W') {
			for (int loop = 0; loop <= cells; loop++){
				if ((this.current_y - loop) >= 0){
					this.calculateCost(a[this.current_x][this.current_y - loop]);
					a[this.current_x][this.current_y - loop] = ' ';
				} else {
					System.out.println("Digger has crossed the site boundary. Simulator will now terminate");
					this.displayLayout();
					this.exit_simulator();
				}
			}
			if ((this.current_y - cells) < 0){
				System.out.println("Digger has crossed the site boundary. Simulator will now terminate");
				this.displayLayout();
				this.exit_simulator();
			}
			this.current_y -= cells;
			a[this.current_x][this.current_y] = 'W';
			this.currentGrid = a;
			return a;
		} 
		if (this.current_direction == 'S') {
			for (int loop = 0; loop <= cells; loop++){
				if ((current_x + loop) < a.length){
					this.calculateCost(a[this.current_x + loop][this.current_y]);
					a[this.current_x + loop][this.current_y] = ' ';
				} else {
					System.out.println("DIGGER has crossed the site boundary. Simulator will now terminate");
					this.displayLayout();
					this.exit_simulator();
				}
			}
			if ((this.current_x + cells) >= a.length){
				System.out.println("Digger has crossed the site boundary. Simulator will now terminate");
				this.displayLayout();
				this.exit_simulator();
			}
			this.current_x += cells;
			a[this.current_x][this.current_y] = 'S';
			this.currentGrid = a;
			return a;
		}
		return a;
	}

	/* These methods handle updating the diggers co-ordinates sequentially and enables the private variables be accessed outside the class */
	
	public char get_current_direction(char rotate){
		int index = this.directions.indexOf(this.current_direction);
		if (rotate == 'L'){
			if (index == 0){ this.new_pos = 3; } else { this.new_pos = ((index - 1) % 4); }
			this.current_direction = directions.charAt(this.new_pos);
		}
		if (rotate == 'R'){
			if (index == 3){ this.new_pos = 0; } else { this.new_pos = ((index + 1) % 4); }
			this.current_direction = directions.charAt(this.new_pos);
		}
		return this.current_direction;
	}
	
	public int get_x_position(int steps){
		return this.current_x += steps;
	}

	public int get_current_y_position(int steps){
		return this.current_y += steps;
	}

	/* These methods retrieve the related costs - usage sequentially */

	public int uncleared_cost(){
		this.uncleared_cost = 0;
		char a[][] = this.currentGrid;
        for(int i = 0; i < a.length; i++){
            for(int j=0; j < a[i].length; j++){
                if (a[i][j] != ' '){
					this.uncleared_cost += uncleared;
				}
            }
        }
		return this.uncleared_cost;
	}
	
	public int get_total_cost(){
		this.total_cost = this.total_cost + this.overhead_cost + this.uncleared_cost;
		return this.total_cost;
	}
	
	public void calculateCost(char letter){
		int squareCost = 0;
		if (letter == 'o') { squareCost = this.plain; }
		if (letter == 'r') { squareCost = this.rocky; }
		if (letter == 't') { squareCost = this.removable_tree + this.paint_damage; }
		if (letter == 'T') { System.out.println("Digger has attempted to clear a protected tree. Simulator will now terminate");
							squareCost = this.protected_tree; this.exit_simulator();}
		if (letter == ' ') { squareCost = this.plain; }
		this.fuel_usage += squareCost;
	}

	public int overhead_cost(int latest_cost){
		this.overhead_cost += latest_cost;
		return overhead_cost;
	}
	
	public int get_overhead_cost(){
		return (this.overhead_cost);
	}
	
	public int get_fuel_usage(){
		return this.fuel_usage;
	}
	
	/* 
	This methods enables the simulator to exit when the exit criteria are met ... either by Q, Protected Tree clearance or Breeaking Boundary 
	N.B. The mthod is called in the corresponding section of the code
	*/
	
	public void exit_simulator(){
		System.out.println("TOTAL FUEL USAGE: " + this.get_fuel_usage() + " HAVE BEEN ADDED TO THE OVERALL COSTS FIGURE");
		overhead_cost(this.fuel_usage);
		System.out.println("TOTAL OVERHEAD COSTS: " + this.get_overhead_cost());
		System.out.println("TOTAL UNCLEARED COST: " + this.uncleared_cost());
		System.out.println("\nTOTAL OVERALL COSTS: " + this.get_total_cost());
		System.out.println("The command history is shown below: \n" + this.command_history);
        System.exit(0);
	}
	
	public void get_command_history(List<String> command_list ){
		this.command_history = command_list;
	}
}