import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Arrays;

/**
 * The model for the program; stores the timetable, and contains methods
 * to retrieve information about the it, and to update it as required.
 */
public class ProgramModel {
	
	//================================================================================
    // Properties
    //================================================================================
	
	/** Number of different class times. */
	private final static int ROWS = 10;
	
	/** Number of different rooms. */
	private final static int COLS = 8;

	/** Array of class times. */
	private final String[] times = new String[] {
		"MonAM",
		"MonPM",
		"TueAM",
		"TuePM",
		"WedAM",
		"WedPM",
		"ThuAM",
		"ThuPM",
		"FriAM",
		"FriPM"
	};
	
	/** Array of room names. */
	private final String[] roomNames = new String[] {"A","B","C","D","E","F","G","H"};
	
	/** Array of room sizes. */
	private final int[] roomSizes = new int[] {100,100,60,60,60,30,30,30};
	
	/** 2D array of slots in the timetable. */
	private Slot[][] slots;
	
	/** {@link HashMap} enabling looking up the module in a given slot. */
	private HashMap<Slot, Module> schedule = new HashMap<Slot, Module>();
	
	/** Array of all the module that need scheduling. */
	private Module[] modules;
	
	//================================================================================
    // Get methods
    //================================================================================
	
	/**
	 * Get method for the slots of the timetable.
	 * @return 2D array of slots.
	 */
	public Slot[][] getSlots()
	{
		return slots;
	}
	
	/**
	 * Get method for the modules to be scheduled.
	 * @return array of modules.
	 */
	public Module[] getModules()
	{
		return modules;
	}
	
	/**
	 * Get the slots into which a module has been placed.
	 * @return array of filled slots.
	 */
	public Slot[] getFilledSlots()
	{
		return schedule.keySet().toArray(new Slot[0]);
	}
	
	//================================================================================
    // Constructor and helper methods
    //================================================================================
	
	/**
	 * Instantiate the program model by creating the slots and the modules.
	 */
	public ProgramModel()
	{
		createSlots();
		createModules();
	}
	
	/**
	 * Create the 2D array of slots representing the positions in the timetable.
	 */
	private void createSlots()
	{
		// Create slots 2D array.
		slots = new Slot[ROWS][COLS];
				
		// Populate slots array with new slot objects.
		for (int i = 0; i < ROWS; i++)
			for (int j = 0; j < COLS; j++)
				slots[i][j] = new Slot(times[i], roomNames[j], roomSizes[j]);
	}
	
	/**
	 * Create the array of modules from the ModulesIn.txt file.
	 */
	private void createModules()
	{
		// get the lines of the file and create module array of same length
		String[] lines = getFileLines();
		modules = new Module[lines.length];
		
		// loop over the lines
		for(int i = 0; i < lines.length; i++)
		{
			// extract the fields from each and create a module object
			String[] ln = lines[i].split(" ");
			modules[i] = new Module(ln[0], Integer.parseInt(ln[4]), ln[1]);
			
			// schedule the module if time and room have been provided
			if (!ln[2].equals("?????"))
				addModuleToSlot(
					modules[i], 
					slots[
						Arrays.asList(times).indexOf(ln[2])
					][
						Arrays.asList(roomNames).indexOf(ln[3])
					]
				);
		}
	}
	
	/**
	 * Read the lines of the input file and return an array of strings,
	 * one for each line.
	 * @return array of strings representing the lines in the file.
	 */
	private String[] getFileLines() {
		
		// create a buffered reader for the input file
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader("ModulesIn.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// loop through the lines of the file and add each to a list
		String str;
		List<String> list = new ArrayList<String>();
		try {
			while((str = in.readLine()) != null)
				list.add(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// return the list as an array
		return list.toArray(new String[0]);
	}
	
	//================================================================================
    // Program methods
    //================================================================================
	
	/**
	 * Add a module to a given slot in the schedule.
	 * @param module the module to schedule.
	 * @param slot the slot to put it in.
	 */
	public void addModuleToSlot(Module module, Slot slot)
	{
		// add module to slot in schedule
		schedule.put(slot, module);
	}
	
	/**
	 * Obtain an array of all slots into which a given module
	 * can be placed, according the rules of the scenario.
	 * @param module the module to be placed.
	 * @return array of valid slots.
	 */
	public Slot[] validSlotsForModule(Module module)
	{
		// we will be returning an array of slots of indeterminate length, so 
		// use array list
		ArrayList<Slot> s = new ArrayList<Slot>();
		
		// for each slot
		for (int i = 0; i < ROWS; i++)
			for (int j = 0; j < COLS; j++)
				// check if module fits, and if it does, add to our array list
				if (moduleFitsInSlot(module, slots[i][j]))
					s.add(slots[i][j]);
		// convert array list to regular array on return
		return s.toArray(new Slot[0]);
	}
	
	/**
	 * Check whether a given module may be legally placed into a given
	 * slot.
	 * @param module the module to be placed.
	 * @param slot the slot to test.
	 * @return true if the module may be placed in the slot, false otherwise.
	 */
	public boolean moduleFitsInSlot(Module module, Slot slot)
	{
		// is a module already scheduled for the slot? If so return false
		if (schedule.get(slot) != null)
			return false;
		// does the slot have enough seats for the module? If not, return false
		if (module.getSize() > slot.getSize())
			return false;
		// is there already a class for that year at this time? If so, return false:
		// to check this condition, first get the row of the module in the slots array
		int t = Arrays.asList(times).indexOf(slot.getTime());
		// loop over all slots in that row (at that time)
		for (int i = 0; i < COLS; i++)
		{
			// get the module in the slot
			Module m = schedule.get(slots[t][i]);
			// if there is a module scheduled, and it is the same subject and year
			// return false
			if (m != null)
				if (m.getSubjectYear().equals(module.getSubjectYear()))
					return false;
		}
		// if none of above, return true
		return true;
	}
	
	/**
	 * Get the slot into which a given module has been placed, 
	 * or null if it has not been scheduled yet.
	 * @param module the module for which to find the slot.
	 * @return the slot in which the module is scheduled.
	 */
	public Slot slotForModule(Module module)
	{
		// loop over the modules which have been scheduled
		for (Entry<Slot, Module> m : schedule.entrySet()) 
		{
			// if one is equal to module in question, return its key (slot)
	        if (Objects.equals(module, m.getValue())) {
	            return m.getKey();
	        }
	    }
		// otherwise return null as module not scheduled
	    return null;
	}
	
	/**
	 * Get the module in a given slot.
	 * @param slot the slot for which to get the module.
	 * @return the module in the slot, or null if there is none.
	 */
	public Module moduleInSlot(Slot slot)
	{
		// look up slot in the schedule HashMap
		return schedule.get(slot);
	}
	
	/**
	 * Get the string description of a module as it should appear
	 * in the module view.
	 * @param module module for which to obtain description.
	 * @return the string description.
	 */
	public String lineForModule(Module module)
	{
		// if module not scheduled, slot description ends in question marks
		String slotDescription = "  -  ?????  ?";
		// get the slot into which the module has been placed
		Slot s = slotForModule(module);
		// if the module has been scheduled, replace question marks with info
		if (s != null)
			slotDescription = "  -  " + s.getTime() + "  " + s.getName();
		// return result
		return module.getCode() + "  " + module.getSize() + slotDescription;
	}
	
	//================================================================================
    // File Saving methods
    //================================================================================
	
	/**
	 * Get the string description of a module as it should appear in the output
	 * text file.
	 * @param module the module for which to obtain the description.
	 * @return the string description.
	 */
	private String outputLineForModule(Module module)
	{
		// if module is not scheduled, slot description ends in question marks
		String slotDescription = "????? ?";
		// get the slot into which the module has been placed
		Slot s = slotForModule(module);
		// if the module has been scheduled, replace question marks with info
		if (s != null)
			slotDescription = s.getTime() + " " + s.getName();
		// return result
		return module.getCode() + " " + module.getName() + " " 
			+ slotDescription + " " + module.getSize();
	}
	
	/**
	 * Write the module details to the output file.
	 */
	public void saveToFile()
	{
		// the string that will hold all the modules' details
		String s = ""; 
		// loop through all modules and add their details
		for(int i = 0; i < modules.length; i++) 
			s += outputLineForModule(modules[i]) + "\n";
		
		// make a Print Writer object
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("ModulesOut.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// write module details to file and close print writer
		writer.println(s);
		writer.close();
	}
}
