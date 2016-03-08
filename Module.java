/**
 * Simple class to store details about a module.
 */
public class Module {
	
	//================================================================================
    // Properties
    //================================================================================
	
	/** The unique identifying code for the module. */
	private String code;

	/** The name of the module. */
	private String name;

	/** The number of students taking the module. */
	private int size;

	//================================================================================
    // Constructor
    //================================================================================
	
	/**
	* Instantiate a module from a line of ModulesIn.txt.
	*/
	public Module(String code, int size, String name)
	{
		this.code = code;
		this.size = size;
		this.name = name;
	}
	
	//================================================================================
    // Get methods
    //================================================================================
	
	/**
	 * Get the subject and year code for the module.
	 * @return the 3 digit code.
	 */
	public String getSubjectYear()
	{
		return code.substring(0,3);
	}
	
	/**
	 * Get the full identifying code for the module.
	 * @return the module's unique identifying code.
	 */
	public String getCode()
	{
		return code;
	}
	
	/**
	 * Get the number of people in taking the module.
	 * @return the size of the module.
	 */
	public int getSize()
	{
		return size;
	}
	
	/**
	 * Get the name of the module. This is only used for file output.
	 * @return the full name of the module.
	 */
	public String getName()
	{
		return name;
	}
}
