
import java.util.HashMap;

/**
 * Class to parse the arguments and store them accordingly in a map
 * 
 *
 */
public class ArgumentParser {
	
	private HashMap<String, String> argMap;
	private int argumentCount = 0;
	
	/**
	 * Constructor for the class Argument Parser
	 * @param args array of arguments passes in the command line
	 */
	public ArgumentParser(String[] args) {
		argMap = new HashMap<String, String>();
		parseArgs(args);
	}
	
	/**
	 * This method parses all the arguments that are
	 * received from the Driver and stores them accordingly
	 * @param args
	 */
	private void parseArgs(String[] args) {
		String flag = null, value = null;
		for(String temp: args) {
			temp = temp.trim().toLowerCase();
			if(isFlag(temp)) {
				flag = temp;
			}
			
			if((!isFlag(temp)) && isValue(temp)) {
				argumentCount = argumentCount+1;
				value = temp;
			}
			
			else {
				value = null;
			}

			argMap.put(flag, value);
		}
	}
	
	/**
	 * Checks if the argument coming in is a flag
	 * @param arg each argument in the array of arguments
	 * @return
	 */
	public static boolean isFlag(String arg) {
		
		if(arg.startsWith("-")) {
			return true;
		}
		
		else if(!arg.startsWith("-")) {
			return false;
		}
		
		else {
			return false;
		}
	}
	
	/**
	 * Checks if the argument coming in is a value
	 * @param arg
	 * @return
	 */
	public static boolean isValue(String arg) {
		
		if(!arg.startsWith("-")) {
			return true;
		}
		
		else
			return false;
	}
	
	/**
	 * Checks if the flag is present in the map
	 * @param arg
	 * @return
	 */
	public boolean hasFlag(String arg) {
		
		if(argMap.containsKey(arg)) {
			return true;
		}
		
		else return false;
	}
	
	/**
	 * This method checks if a flag has a value mapped to it
	 * @param arg
	 * @return
	 */
	public boolean hasValue(String arg) {
		String argTest = argMap.get(arg);
		
		if(argTest != null) {
			return true;
		}
		
		else {
			return false;
		}
	}
	
	/**
	 * This method retrieves the value for a particular flag
	 * @param flag argument that starts with "-"
	 * @return
	 */
	public String getValue(String flag) {
		String value = argMap.get(flag);
		
		if(value != null) {
			return value;
		}
		
		else
			return null;
	}
	
	/**
	 * This method returns the count of number of flags present in the map
	 * @return
	 */
	public int numFlags() {
		Integer flagCount = argMap.size();
		return flagCount;
	}
	
	/**
	 * This method returns the count of number of arguments parsed
	 * @return
	 */
	public int numArguments() {
		return argumentCount;
	}
}