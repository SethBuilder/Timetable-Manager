/**
 * Simple class representing a slot in the timetable.
 */
class Slot
{
	/** The time of the slot. */
	private String time;
	
	/** The room name. */
	private String name;
	
	/** The capacity of the slot. */
	private int size;

	/**
	 * Get the time of the slot. 
	 * @return the time.
	 */
	public String getTime()
	{
		return time;
	}
	
	/**
	 * Get the name of the slot.
	 * @return the name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Get the capacity of the slot.
	 * @return the capacity.
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * Instantiate a slot from a given time name and capacity.
	 * @param time the time.
	 * @param name the name.
	 * @param size the capacity.
	 */
	public Slot(String time, String name, int size)
	{
		this.time = time;
		this.name = name;
		this.size = size;
	}
}