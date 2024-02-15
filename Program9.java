//********************************************************************
//
//  Author:        Marshal Pfluger
//
//  Program #:     Program Nine
//
//  File Name:     Program9.java
//
//  Course:        COSC 4302 Operating Systems
//
//  Due Date:      12/04/2023
//
//  Instructor:    Prof. Fred Kumi
//
//  Java Version:  17
//
//  Description:    program that implements the FCFS, SSTF, SCAN, C-SCAN, LOOK, C-LOOK
//                  disk scheduling algorithms. 
//********************************************************************
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Program9 {
	
    public static void main(String[] args) {
    	Program9 obj = new Program9();
    	obj.developerInfo();
    	obj.runDemo();
    }
    
    //***************************************************************
    //
    //  Method:       runDemo
    // 
    //  Description:  Runs the program operations 
    //
    //  Parameters:   N/A
    //
    //  Returns:     N/A
    //
    //**************************************************************
    public void runDemo() {
    	int randomRequestNum = 1000;
    	int numRequests = 0;
    	ArrayList<Integer> cylinderRequests = null;
        boolean rerun = true;
    	do {
    		printOutput("Would you like to generate random cylinder requests?(y/n)");
    		
        	// Generate random numbers or allow user to enter string
        	if (stringInputValidation(userChoice()).equalsIgnoreCase("y")){
        		// Comment this next line, and uncomment lines 57, 58 to let user choose num random
        		numRequests = randomRequestNum;
        		//printOutput("How many numbers would you like to generate?:");
        		//numRequests = intInputValidation(userChoice(), 2);
        		// Generate list of requests
        		cylinderRequests = generateRandomNumbers(numRequests);
        		printOutput(arrayListToString(cylinderRequests));
        	}
        	else {
                // Get user input for the page-reference string and number of page frames
                printOutput("Enter cylinder requests (space-separated, only 0-4999): ");
                // Pass the user string to the list parse method
                cylinderRequests = parseReference(userChoice());
                // Get the number of requests from the requests list and then remove it
                numRequests = cylinderRequests.get(0);
                cylinderRequests.remove(0);
        	}

        	// Prompt user to enter the number of frames
            printOutput("Enter the initial head position: ");
            int initialPosition = intInputValidation(userChoice(), 1);

            // Instantiate object of pageReplacement class
            DiskScheduling diskObj = new DiskScheduling(cylinderRequests, initialPosition, numRequests);
            
            // Run the algorithms
            runAlgorithms(diskObj);
            
            // ALlow the user to run the program again
            printOutput("Would you like to run the program again?(enter to rerun or 0 to exit)");
            if(userChoice().equalsIgnoreCase("0"))
            	rerun = false;
            	
    	}while (rerun);
    	printOutput("Program has terminated, have a great day");
    }
    
    //***************************************************************
    //
    //  Method:       runAlgorithms
    // 
    //  Description:  runs all of the algorithms for the program 
    //
    //  Parameters:   DiskScheduling diskObj
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void runAlgorithms(DiskScheduling diskObj) {
        // Test FCFS algorithm
        int headMovementFCFS = diskObj.runFCFS();
        printOutput("FCFS total head Movement: " + headMovementFCFS);

        // Test SSTF algorithm
        int headMovementSSTF = diskObj.runSSTF();
        printOutput("SSTF total head movement: " + headMovementSSTF);

        // Test Scan algorithm
        int headMovementScan = diskObj.runScan();
        printOutput("Scan total head movement: " + headMovementScan);
        
        // Test CScan algorithm
        int headMovementCScan = diskObj.runCScan();
        printOutput("CScan total head movement: " + headMovementCScan);
        
        // Test Look algorithm
        int headMovementLook = diskObj.runLook();
        printOutput("Look total head movement: " + headMovementLook);
        
        // Test CLook algorithm
        int headMovementCLook = diskObj.runCLook();
        printOutput("CLook total head movement: " + headMovementCLook);
    }
    
    //***************************************************************
    //
    //  Method:       arrayListToString
    // 
    //  Description:  converts an arrayList of ints to a concatenated string 
    //
    //  Parameters:   ArrayList<Integer> list
    //
    //  Returns:     String
    //
    //**************************************************************
    private String arrayListToString(ArrayList<Integer> list) {
    	
    	// Instantiate new string builder
        StringBuilder stringBuilder = new StringBuilder();

        // loop through list and append to the string with a space seperation
        for (Integer number : list) {
            stringBuilder.append(number).append(" ");
        }
        // Return the new string
        return stringBuilder.toString();
    }
    
    //***************************************************************
    //
    //  Method:       generateRandomNumbers
    // 
    //  Description:  generates a list of random numbers for the program
    //
    //  Parameters:   int numberOfDigits
    //
    //  Returns:     ArrayList<Integer>
    //
    //**************************************************************
    public ArrayList<Integer> generateRandomNumbers(int numberOfDigits) {

        ArrayList<Integer> randomNumbers = new ArrayList<>();
        Random random = new Random();

        // Generate list of random numbers
        for (int i = 0; i < numberOfDigits; i++) {
        	// Generates a random number between 0 and 4999
            randomNumbers.add(random.nextInt(5000)); 
        }

        return randomNumbers;
    }
    
    //***************************************************************
    //
    //  Method:       intInputValidation (Non Static)
    // 
    //  Description:  validates the user input
    //
    //  Parameters:   None
    //
    //  Returns:      N/A 
    //
    //**************************************************************
    public int intInputValidation(String userInput, int validationType) {
    	
    	int userInt = 0;
    	boolean validation = true;
    	
    	// Make sure input is numeric
        do {
        	validation = true;
        	try {
        		// If user input is for cylinder requests
        		if(validationType == 1) {
        			userInt = Integer.parseInt(userInput);
                    if (userInt > 4999 || userInt < 0) {
                        throw new NumberFormatException();
                    }
        		}
        		// If user input is for the number of requests
        		else if(validationType == 2) {
        			userInt = Integer.parseInt(userInput);	
        		}
        } catch (NumberFormatException e) {
            // If user input is invalid prompt user to enter new input
        	validation = false;
            System.out.println("Invalid input. Please enter a valid number.");
            userInput = userChoice(); 
        }
        }while (!validation);
        return userInt;
    }
    
    //***************************************************************
    //
    //  Method:       stringinputValidation (Non Static)
    // 
    //  Description:  validates the user input
    //
    //  Parameters:   None
    //
    //  Returns:      N/A 
    //
    //**************************************************************
    public String stringInputValidation(String userInput) {
    	boolean validation = true;
    	// Make sure input is valid
        do {
        	try {
        		// Check user input for initial menu item
        		if(userInput.equalsIgnoreCase("y") || userInput.equalsIgnoreCase("n")) {
        			validation = true;
        		}
        		else {
        			throw new Exception(); 
        		}
        		} catch (Exception e) {
        			// If user input is invalid prompt user to enter new input
        			validation = false;
        			System.out.println("Invalid input. Please enter a option.");
                    userInput = userChoice(); 
                    }
        	}while (!validation);
        return userInput;
    }
    
    //**************************************************************
    //
    //  Method:       parseReference
    //
    //  Description:  parses the reference string from user,
    //                Gets new input form user if invalid.
    //
    //  Parameters:   String pageReferenceString
    //
    //  Returns:      ArrayList<Integer>
    //
    //**************************************************************
    public ArrayList<Integer> parseReference(String pageReferenceString) {
    	int numRequests = 0;
        boolean validation = true;
        ArrayList<Integer> pages = null;

        // loop to parse user reference string
        do {
        	numRequests = 0;
            try {
            	validation = true;
                pages = Arrays.stream(pageReferenceString.split("\\s+"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toCollection(ArrayList::new));

                // Additional check for each element in the list
                for (int element : pages) {
                	numRequests++;
                    if (element > 4999 || element < 0) {
                        throw new NumberFormatException();
                    }
                }
                // If user input is invalid prompt to re enter 
            } catch (Exception e) {
                validation = false;
                printOutput("Your cylinder request was invalid");
                printOutput("Please try again, must be space-separated integers (0-4999 only)");
                pageReferenceString = userChoice();
            }
        } while (!validation);
        pages.add(0, numRequests);
        return pages;
    }
    
    //**************************************************************
    //
    //  Method:       userChoice
    //
    //  Description:  gets input from user, closes scanner when program exits 
    //
    //  Parameters:   N/A
    //
    //  Returns:      String file
    //
    //**************************************************************	
    public String userChoice() {
    	String userChoice;
    	// Use Scanner to receive user input
    	Scanner userInput = new Scanner(System.in);
    	// Store user choice
    	userChoice = userInput.nextLine();
    	
    	// close scanner when program exits.
    	if (userChoice.equalsIgnoreCase("0")) {
    		userInput.close();
    		}
    	return userChoice;
    	}
    
	//***************************************************************
	//
	//  Method:       printOutput (Non Static)
	// 
	//  Description:  handles printing output
	//
	//  Parameters:   String output
	//
	//  Returns:      N/A
	//
	//***************************************************************
	public void printOutput(String output) {
		//Print the output to the terminal
		System.out.print("\n");
		System.out.println(output);
	}//End printOutput
    
    //***************************************************************
    //
    //  Method:       developerInfo (Non Static)
    // 
    //  Description:  The developer information method of the program
    //
    //  Parameters:   None
    //
    //  Returns:      N/A 
    //
    //**************************************************************
    public void developerInfo(){
       System.out.println("Name:    Marshal Pfluger");
       System.out.println("Course:  COSC 4302 Operating Systems");
       System.out.println("Project: Nine\n\n");
    } // End of the developerInfo method
}