import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The controller of the program. Responsible for 
 * managing button presses and updating view (made up of timetable view and 
 * module view) and model (program model) appropriately.
 */
public class ProgramController
{
	
	//================================================================================
    // Properties
    //================================================================================
	
	/** The model for the program, which stores all scheduling info. */
	private static ProgramModel model;
	
	/** The view for the program. */
	private ProgramView view;
	
	/** The module currently selected for scheduling. */
	private Module selectedModule;
	
	/** The slot in which the currently selected module was before it was selected. */
	private Slot selectedSlot;
	
	/** Whether editing the timetable is enabled. */
	private boolean editEnabled;
	
	//================================================================================
    // Constructor and setup methods
    //================================================================================
	
	/**
	 * Instantiate the GUI, by setting up the layout, adding components, 
	 * adding action listeners to buttons, and loading data from ModulesIn.txt.
	 */
	public ProgramController ()
	{
		model = new ProgramModel();
		view = new ProgramView(model.getSlots(), model.getModules());
		setupTimetableButtons();
		setupModuleButtons();
		setupEditButton();
		setupQuitButton();
		loadData();	
	}
	
	/**
	 * Adds an {@link ActionListener} to each slot button in the timetable
	 * view, so that slotPressed is called when the button is pressed.
	 */
	private void setupTimetableButtons()
	{
		Slot[][] slots = model.getSlots();
		
		// loop over slots and add action listener to matching button
		for (int i = 0; i < slots.length; i++)
			for (int j = 0; j < slots[0].length; j++)
			{
				final Slot slot = slots[i][j];
				view.getTimetableView().getButtonAtSlot(slot).addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							slotPressed(slot);
						}
					}
				);
			}
	}
	
	/**
	 * Adds an {@link ActionListener} to each module button in the module
	 * view, so that modulePressed is called when the button is pressed.
	 */
	private void setupModuleButtons()
	{
		Module[] modules = model.getModules();
		
		// loop over slots and add action listener to matching button
		for (int i = 0; i < modules.length; i++)
		{
			final Module module = modules[i];
			view.getModuleView().getButtonForModule(module).addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							modulePressed(module);
						}
					});
		}
	}
	
	/**
	 * Adds an {@link ActionListener} to the edit/save button, so that
	 * editPressed is called when it is pressed.
	 */
	private void setupEditButton()
	{
		view.getEditButton().addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editPressed();
				}
			});
	}
	
	/**
	 * Set the program to call the save to file method
	 * when the gui is closed, then quit the program.
	 */
	private void setupQuitButton()
	{
		// add custom window close method
		view.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event){
				saveToFile();
				System.exit(0);
			}
		});
	}
	
	/**
	 * Load the schedule data from the timetable into the views 
	 * (both timetable and module views).
	 */
	private void loadData()
	{
		// get the slots which have a module in them
		Slot[] filledSlots = model.getFilledSlots();
		// load each slot into view (text field blank as text is set below)
		for (int i = 0; i < filledSlots.length; i++)
			view.scheduleModule(
					model.moduleInSlot(filledSlots[i]), 
					filledSlots[i],
					"",
					null
				);

		// get all the modules
		Module[] ms = model.getModules();
		// for each module
		for (int i = 0; i < ms.length; i++)
			view.loadUnscheduledModule(ms[i], model.lineForModule(ms[i]));
	}
	
	//================================================================================
    // Button press methods
    //================================================================================
	
	/**
	 * Called when the edit/save button is pressed. Enabled or disable view buttons
	 * accordingly, and change edit button text. In addition, save changes if 
	 * appropriate.
	 */
	public void editPressed()
	{
		// enable or disable buttons, and update edit button
		view.toggleButtons(!editEnabled);
		
		// save if necessary
		if (editEnabled) 
		{
			// important to clear the selected module before buttons are disabled
			deselectModule();
			saveToFile();
		}
		
		// toggle edit enabled
		editEnabled = editEnabled ? false : true;
	}
	
	/**
	 * Called whenever a slot button is pressed. Updates model and view
	 * appropriately.
	 * @param slot the slot that was pressed.
	 */
	public void slotPressed(Slot slot)
	{
		// schedule selected module if one is selected
		if (selectedModule != null)
			// if the selected module is successfully scheduled, return
			if (scheduleModule(selectedModule, slot))
				return;
		// if no module selected, or module wasn't successfully scheduled,
		// and if there is a module in the slot
		if (model.moduleInSlot(slot) != null)
			// act as if the module button for that module was pressed
			modulePressed(model.moduleInSlot(slot));
		// otherwise, do nothing
	}
	
	/**
	 * Called whenever a module button is pressed. Updates model and view
	 * appropriately.
	 * @param module
	 */
	public void modulePressed(Module module)
	{
		// if there is a module selected
		if (selectedModule != null)
			// if pressed module is the selected module, de-select it
			if (selectedModule == module)
				deselectModule();
			else 
			{
				// pressing a different module de-selects selected module
				// and selects new module instead
				deselectModule();
				selectModule(module);
			}
		// if no module is currently selected, select the pressed module
		else 
			selectModule(module);
	}
	
	//================================================================================
    // Scheduling helper methods
    //================================================================================
	
	/**
	 * Adds a module to a given slot in the timetable, provided the slot is a valid
	 * place to put the module, and updates views to reflect the change.
	 * @param module the module to schedule.
	 * @param slot the slot in which to put it.
	 * @return whether the module was scheduled.
	 */
	private boolean scheduleModule(Module module, Slot slot)
	{
		// check if slot is valid place to put module
		if (model.moduleFitsInSlot(module, slot))
		{
			// update model
			model.addModuleToSlot(module, slot);
			// and view
			view.scheduleModule(
					module, 
					slot, 
					model.lineForModule(module), 
					selectedSlot
				);
			// clear selected module and return true since module 
			// was successfully scheduled
			selectedModule = null;
			return true;
		}
		// if not valid slot, return false
		return false;
	}
	
	/**
	 * Select a given module, and highlight the slots in the 
	 * timetable view into which the module may be placed.
	 * @param module the module to be selected.
	 */
	private void selectModule(Module module)
	{
		// save the slot in which the module is currently scheduled
		selectedSlot = model.slotForModule(module);
		// clear that slot in timetable if not null
		if (selectedSlot != null)
			model.addModuleToSlot(null, selectedSlot);
		// update the view
		view.selectModule(module, model.validSlotsForModule(module));
		selectedModule = module;
	}
	
	/**
	 * Deselect the currently selected module - put it back into its previous slot if
	 * there was one, otherwise keep it unscheduled, and update views accordingly.
	 */
	private void deselectModule()
	{
		// need to save reference to selected module, as calling
		// scheduleModule makes selected module null
		Module module = selectedModule;
		// if no module is selected, there is nothing to be done
		if (module == null)
			return; 
		// if the selected module was previously scheduled
		if (selectedSlot != null)
			// reschedule it in its previous slot
			scheduleModule(module, selectedSlot);
		else
			// otherwise, return the selected module button to its unscheduled style
			view.makeUnscheduled(module, model.lineForModule(module));

		selectedModule = null;
	}
	
	//================================================================================
    // File saving
    //================================================================================
	
	/**
	 * Save the changes to the output file, and show a message to the user.
	 */
	private void saveToFile()
	{
		// only save if necessary
		if (!editEnabled) 
			return;
		model.saveToFile();
		// create and display message to user
		UIManager.put("OptionPane.background", Color.WHITE);
		UIManager.put("Panel.background", Color.WHITE);
		JOptionPane.showMessageDialog(
				null, 
				"Changes saved to ModulesOut.txt.", 
				"Changes Saved", 
				JOptionPane.INFORMATION_MESSAGE
			);
	}
}
