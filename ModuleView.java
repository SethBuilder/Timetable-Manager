import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Class to display the list of modules as part of the GUI.
 * Modules are displayed as {@link JButtons}, with one
 * corresponding to each module. Includes methods to change 
 * colour and text of a given button.
 */
@SuppressWarnings("serial")
public class ModuleView extends JPanel {
	
	//================================================================================
    // Properties
    //================================================================================
	
	/** 
	 * The buttons, as a {@link HashMap} enabling a button to be looked
	 * up via its module.
	 */
    private HashMap<Module, JButton> buttons = new HashMap<Module, JButton>();
    
    /** The colour to turn the module button for the
     * module which is being moved at the moment. 
     */
    private final Color HIGHLIGHT_COLOR = new Color(0xBBDEFB);
    
    /** The colour to turn a module button once it has been 
     * scheduled.
     */
    private final Color SCHEDULED_COLOR = new Color(0x1976D2);
    
    /** The color of the borders of the slots of the timetable. */
	private final Color BORDER_COLOR = new Color(0x889db3);
    
    //================================================================================
    // Constructor and helper methods
    //================================================================================
    
    /**
     * Instantiate the module view from a given array of modules.
     * @param modules the modules to display in the list.
     */
	public ModuleView(Module[] modules) 
	{
		// set up grid layout
		super(new GridLayout(modules.length, 1));

		// add a button for each module to the grid layout
		for (int i = 0; i < modules.length; i++)
			addButtonForModule(modules[i]);
		
		// buttons not enabled at the start of the program
		setButtonsEnabled(false);
	}
	
	/**
	 * Add a module button to the view.
	 * @param module the module for which to add a button.
	 */
	private void addButtonForModule(Module module)
	{
		// create the button
		JButton b = new JButton();
		
		// set the style of the button
		b.setBackground(Color.WHITE);
		b.setForeground(SCHEDULED_COLOR);
		b.setFont(new Font("Arial", Font.BOLD, 15));
		b.setBorder(BorderFactory.createCompoundBorder(
		        BorderFactory.createLineBorder(BORDER_COLOR, 1), 
		        BorderFactory.createEmptyBorder(10, 0, 10, 0)));
		b.setFocusPainted(false);
		
		// add button to view, and store in buttons HashMap
		this.add(b);
		buttons.put(module, b);
	}
	
	//================================================================================
    // Program Methods
    //================================================================================
	
	/**
	 * Get the button which corresponds to a given module.
	 * @param module the given module.
	 * @return the {@link JButton} corresponding to it.
	 */
	public JButton getButtonForModule(Module module)
	{
		// look up the button in the buttons HashMap
		return buttons.get(module);
	}
	
	
	/**
	 * Change the colour of a module button to indicate that
	 * its module is being moved.
	 * @param module the module to highlight.
	 */
	public void highlightModule(Module module)
	{
		// get the button and change its style
		JButton button = buttons.get(module);
		button.setBackground(HIGHLIGHT_COLOR);
		button.setForeground(SCHEDULED_COLOR);
	}
	
	/**
	 * Change the colour of a module button to indicate that
	 * its module has been scheduled.
	 * @param module the module which has been scheduled.
	 */
	public void makeScheduled(Module module)
	{
		// get the button and change its style.
		JButton button = buttons.get(module);
		button.setBackground(SCHEDULED_COLOR);
		button.setForeground(Color.WHITE);
	}
	
	/**
	 * Change the colour of a module button to indicate that
	 * its module is no longer scheduled.
	 * @param module the module which has been scheduled.
	 */
	public void makeUnscheduled(Module module)
	{
		// get the button and change its style.
		JButton button = buttons.get(module);
		button.setBackground(Color.WHITE);
		button.setForeground(SCHEDULED_COLOR);
	}
	
	/**
	 * Update the text on a given module button.
	 * @param module the module whose button needs changed.
	 * @param text the new text to display.
	 */
	public void setTextForButton(Module module, String text)
	{
		buttons.get(module).setText(text);
	}
	
	/**
	 * Set all the buttons in the view to be either enabled
	 * or disabled.
	 * @param enabled whether the buttons are to be enabled or not.
	 */
	public void setButtonsEnabled(boolean enabled)
	{
		// get the buttons and set each en/dis-abled
		JButton[] b = buttons.values().toArray(new JButton[0]);
		for (int i = 0; i < b.length; i++)
			b[i].setEnabled(enabled);
	}
}
