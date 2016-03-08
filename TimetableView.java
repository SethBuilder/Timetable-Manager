import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.util.*;
import java.awt.*;

/**
 * Class for the timetable component of the GUI. Contains methods to
 * update cells based on user input, and a button for each slot.
 */
@SuppressWarnings("serial")
class TimetableView extends JPanel
{
	//================================================================================
    // Properties
    //================================================================================
	
	/** HashMap allowing buttons to be looked up via slots. */
	private HashMap<Slot, JButton> buttons = new HashMap<Slot, JButton>();
	
	/** The colour to turn valid slots in the timetable when a module is selected. */
	private final Color HIGHLIGHT_COLOR = new Color(0xBBDEFB);
	
	/** The colour to turn slots which have a module scheduled in them. */
	private final Color SCHEDULED_COLOR = new Color(0x1976D2);
	
	/** The background colour of the timetable. */
	private final Color BACKGROUND_COLOR = new Color(0xA0BAD6);
	
	/** The color of the borders of the slots of the timetable. */
	private final Color BORDER_COLOR = new Color(0x889db3);
	
	//================================================================================
    // Constructor and helper methods
    //================================================================================
	
	/**
	 * Instantiate the timetable view, by setting the layout, and adding buttons
	 * and labels.
	 * @param slots the 2D array of {@link Slot}s in the timetable.
	 */
	public TimetableView(Slot[][] slots)
	{
		// Initialize the panel with a grid layout.
		// (extra row above and to the left for labels)
		super(new GridLayout(slots.length + 1, slots[0].length + 1));

		setBackground(BACKGROUND_COLOR);
		
		int rows = slots.length;
		int cols = slots[0].length;
		
		// When adding to a GridLayout, components fill along the row first.
		// Put blank JLabel in top left.
		this.add(new JLabel());
				
		// Add labels at the top for room names and sizes.
		for (int i = 0; i < cols; i++)
			addTopLabel(slots[0][i].getName() + "\n" + slots[0][i].getSize());
		
		// Fill out remaining rows with label on left, then buttons
		// To do this: loop through the rows
		for (int i = 0; i < rows; i++)
		{
			// Add JLabel to the left of each row.
			addLeftLabel(slots[i][0].getTime());
			
			// then fill out the rest of the row with buttons
			for (int j = 0; j < cols; j++)
				addSlotButton(slots[i][j]);
		}
		
		// buttons disabled when program begins
		setButtonsEnabled(false);
	}
	
	/**
	 * Adds a {@link JTextPane} label to the grid layout.
	 * This is used as it enabled multi-line text.
	 * @param text the text to display in the label.
	 */
	private void addTopLabel(String text)
	{
		// create text pane
		JTextPane label = new JTextPane();
		label.setText(text);
		
		// set the text to be centered
		StyledDocument doc = label.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		// set colour and other properties of label, and add to layout
		label.setForeground(Color.WHITE);
		label.setEditable(false);
		label.setHighlighter(null);
		label.setBackground(BACKGROUND_COLOR);
		label.setFont(new Font("Arial", Font.BOLD, 15));
		this.add(label);
	}
	
	/**
	 * Adds a {@link JLabel} to the grid layout, with right aligned text.
	 * @param text the text to display on the label.
	 */
	private void addLeftLabel(String text)
	{
		// create label with given text right aligned
		JLabel label = new JLabel(text, SwingConstants.RIGHT);
		
		// add invisible border so text has space
		label.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// set colour and font, and add to layout
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.BOLD, 15));
		this.add(label);
	}
	
	/**
	 * Adds a button to the layout, storing a reference to its appropriate {@link Slot}
	 * alongside it.
	 * @param slot the {@link Slot} for the button.
	 */
	private void addSlotButton(Slot slot)
	{
		// create button
		JButton button = new JButton();
		
		// set style of button
		button.setBackground(Color.WHITE);
		button.setForeground(Color.WHITE);
		button.setBorder(new LineBorder(BORDER_COLOR, 1));
		button.setFont(new Font("Arial", Font.BOLD, 15));
		button.setFocusPainted(false);
		
		// add to buttons HashMap so button can be looked up via slot, then add to 
		// layout
		buttons.put(slot, button);
		this.add(button);
	}
	
	//================================================================================
    // Program methods
    //================================================================================
	
	/**
	 * Returns the {@link JButton} corresponding to a given slot in the timetable.
	 * @param slot the {@link Slot} in the timetable.
	 * @return the {@link JButton}.
	 */
	public JButton getButtonAtSlot(Slot slot)
	{
		return buttons.get(slot);
	}
	
	/**
	 * Set the text of the button in a given {@link Slot}.
	 * @param slot the {@link Slot} to which the button corresponds.
	 * @param text the text which the button should display.
	 */
	public void setSlotText(Slot slot, String text)
	{
		buttons.get(slot).setText(text);
	}
	
	/**
	 * Set the background colour of a slot button to the colour which 
	 * represents a scheduled module.
	 * @param slot the {@link Slot} to change.
	 */
	public void setSlotScheduledColor(Slot slot)
	{
		buttons.get(slot).setBackground(SCHEDULED_COLOR);
	}
	
	/**
	 * Given an array of {@link Slot}s, turn their corresponding
	 * buttons the highlight colour, which represents valid slots
	 * for a module to be placed into.
	 * @param list the array of {@link Slot}s.
	 */
	public void highlightSlots(Slot[] list)
	{
		// loop through the provided slots
		for (int i = 0; i < list.length; i++)
		{
			// look up button and highlight it
			buttons.get(list[i]).setBackground(HIGHLIGHT_COLOR);
		}
	}
	
	/**
	 * Turn the background of all highlighted slot buttons back to white.
	 */
	public void clearHighlights()
	{
		// loop through buttons and change background to white if it
		// is currently the highlight colour
		JButton[] b = buttons.values().toArray(new JButton[0]);
		for (int i = 0; i < b.length; i++)
			if (b[i].getBackground() == HIGHLIGHT_COLOR)
				b[i].setBackground(Color.WHITE);
			
	}
	
	/**
	 * Toggle whether the slot buttons are enabled or disabled.
	 * @param enabled whether the buttons should be enabled or disabled.
	 */
	public void setButtonsEnabled(boolean enabled)
	{
		// loop through buttons and toggle enabled
		JButton[] b = buttons.values().toArray(new JButton[0]);
		for (int i = 0; i < b.length; i++)
			b[i].setEnabled(enabled);
	}
}