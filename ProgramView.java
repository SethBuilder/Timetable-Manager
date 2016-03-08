import javax.swing.*;
import java.awt.*;

/**
 * The view of the program. Responsible for laying out the GUI.
 */
@SuppressWarnings("serial")
public class ProgramView extends JFrame
{
	
	//================================================================================
    // Properties
    //================================================================================
		
	/** The timetable view. */
	private TimetableView tv;
	
	/** The module view. */
	private ModuleView mv;
	
	/** The background color of the GUI. */
	private final Color BACKGROUND_COLOR = new Color(0xA0BAD6);
	
	/** The dimensions of the screen. */
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	/** The edit button. */
	private JButton editButton;
	
	/**
	 * Get the timetable view.
	 * @return the timetable view.
	 */
	public TimetableView getTimetableView()
	{
		return tv;
	}
	
	/**
	 * Get the module view.
	 * @return the module view.
	 */
	public ModuleView getModuleView()
	{
		return mv;
	}
	
	/**
	 * Get the edit button.
	 * @return the edit button.
	 */
	public JButton getEditButton()
	{
		return editButton;
	}
	
	//================================================================================
    // Constructor and setup methods
    //================================================================================
	
	/**
	 * Instantiate the GUI, by setting up the layout, adding components, 
	 * adding action listeners to buttons, and loading data from ModulesIn.txt.
	 */
	public ProgramView(Slot[][] slots, Module[] modules)
	{
		setupBasicOptions();
		setupGridBagLayout();
		layoutGUI(slots, modules);
		this.setVisible(true);
	}

	/**
	 * Set the basic features of the GUI window, like its size and location.
	 * Also sets up custom exit method for saving file on close.
	 */
	private void setupBasicOptions()
	{
		getContentPane().setBackground(BACKGROUND_COLOR);
		setTitle("Timetable Manager");
		setLocation(50,50);
		// set do nothing on close so we can implement custom exit method
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// set size based on screen size
		setSize((int) (
			0.8 * screenSize.getWidth()), 
			(int) (0.8 * screenSize.getHeight())
		);
	}
	
	/**
	 * Set up the grid bag layout, which is used to arrange the GUI components.
	 */
	private void setupGridBagLayout()
	{
		GridBagLayout gbl = new GridBagLayout();
		// there are four columns, the second and last are empty
		// and just serve to separate module view from timetable and edge of gui
		gbl.columnWidths = new int[] { 
			(int) (getWidth() * 0.6), 
			(int) (getWidth() * 0.05), 
			(int) (getWidth() * 0.2),
			(int) (getWidth() * 0.05)
		};
		// sets how each column responds to being overfilled
		gbl.columnWeights = new double[] { 1, 1, 1, 1 };
		// layout has 13 rows of equal height
		gbl.rowHeights = new int[13];
		for (int i = 0; i < 13; i++)
			gbl.rowHeights[i] = (int) (getHeight() / 13);
		// sets how each row responds to being overfilled
		gbl.rowWeights = new double[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		// set the content pane to use this layout
		getContentPane().setLayout(gbl);
	}
	 
	/**
	 * Instantiate the program model and add GUI components.
	 */
	private void layoutGUI(Slot[][] slots, Module[] modules)
	{	
		addTimetableView(slots);
		addModulesLabel();		
		addScrollModuleView(modules);
		addEditButton();
	}
	
	/**
	 * Adds the timetable view to the GUI.
	 * @param slots the 2D array of slots with which to create the timetable view.
	 */
	private void addTimetableView(Slot[][] slots)
	{
		tv = new TimetableView(slots);
		addComponent(tv, 0, 1, 1, 11);
	}
	
	/**
	 * Adds the modules label above the module view.
	 */
	private void addModulesLabel()
	{
		// Create label and add to GUI.
		JLabel label = new JLabel("Modules", SwingConstants.CENTER);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.BOLD, 15));
		addComponent(label, 2, 1, 1, 1);
	}
	
	/**
	 * Adds the module view, within a scroll pane, to the GUI.
	 * @param modules the array of modules with which to instantiate the module view.
	 */
	private void addScrollModuleView(Module[] modules)
	{
		// create module view and scroll pane
		mv = new ModuleView(modules);
		JScrollPane scroll = new JScrollPane();
		// set scroll pane parameters, and put module view inside it
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setViewportView(mv);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setAlignmentX(LEFT_ALIGNMENT);
		// this sets the scroll speed
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		// add to GUI
		addComponent(scroll, 2, 2, 1, 7);
	}
	
	/**
	 * Add the edit/save button to the GUI.
	 */
	private void addEditButton()
	{
		// add edit button
		editButton = new JButton("START EDITING");
		// set style
		editButton.setBackground(new Color(0xFFAF26));
		editButton.setForeground(Color.WHITE);
		editButton.setFont(new Font("Arial", Font.BOLD, 15));
		editButton.setBorder(null);
		editButton.setFocusPainted(false);
		// add to GUI
		addComponent(editButton, 2, 10, 1, 1);
	}
	
	/**
	 * Add a component to the GUI.
	 * @param comp the component to add.
	 * @param x the horizontal position within the grid bag layout.
	 * @param y the vertical position within the grid bag layout.
	 * @param width the width in cells of the component.
	 * @param height the height in cells of the component.
	 */
	private void addComponent(Component comp, int x, int y, int width, int height)
	{
		// create grid bag constraints and add component
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		getContentPane().add(comp, c);
	}

	//================================================================================
    // Program methods
    //================================================================================
	
	/**
	 * Enable or disable the buttons in the view, and change the text of the 
	 * edit button.
	 * @param editEnabled true if the buttons should be enabled.
	 */
	public void toggleButtons(boolean enabled) 
	{
		mv.setButtonsEnabled(enabled);
		tv.setButtonsEnabled(enabled);
		editButton.setText(enabled ? "SAVE CHANGES" : "START EDITING");
	}
	
	/**
	 * Set the text of a given module button.
	 * @param module the module whose text needs set.
	 * @param text the text.
	 */
	public void loadUnscheduledModule(Module module, String text)
	{
		mv.setTextForButton(module, text);
	}
	
	/**
	 * Called when a module is scheduled. Clears the text of module's previous slot,
	 * updates the new slot, and clears highlighted slots. In addition, updates
	 * the module in the module view.
	 * @param module the module that has been scheduled.
	 * @param slot the slot in which it has been placed.
	 * @param text the description of the module.
	 * @param selectedSlot the module's previous slot, if any.
	 */
	public void scheduleModule(Module module, Slot slot, String text, 
													Slot selectedSlot)
	{
		if (selectedSlot != null)
			tv.setSlotText(selectedSlot, "");
		// update text on slot button, clear the highlighted slots
		// and make new slot scheduled color
		tv.setSlotText(slot, module.getCode());
		tv.setSlotScheduledColor(slot);
		tv.clearHighlights();
		// change module button background, and set text to module description
		mv.makeScheduled(module);
		mv.setTextForButton(module, text);
	}
	
	/**
	 * Highlight a given module and its valid slots.
	 * @param module the module.
	 * @param valid the slots.
	 */
	public void selectModule(Module module, Slot[] valid) 
	{
		// highlight the module and its valid slots, and store module.
		// note that text is not cleared from selected slot button, so
		// that user is reminded which module they are moving, and where
		// it was previously scheduled
		mv.highlightModule(module);
		tv.highlightSlots(valid);
	}
	
	/**
	 * Set the style of a given module to be unscheduled, and set its text.
	 * Also clear highlighted slots.
	 * @param module the module to be unscheduled.
	 * @param text the text description.
	 */
	public void makeUnscheduled(Module module, String text)
	{
		mv.makeUnscheduled(module);
		mv.setTextForButton(module, text);
		tv.clearHighlights();
	}
}
