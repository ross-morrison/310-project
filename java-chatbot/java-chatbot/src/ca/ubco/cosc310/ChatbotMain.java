package ca.ubco.cosc310;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//test commit

/*
 * Created 2/25/2020
 * @authors Ross Morrison, Brandon Gaucher, Eric Shanks, Ben Fitzharris 
 */
public class ChatbotMain {

	/*
	 * Responses are used when the user inputs something like hey/hello
	 */
	public static ArrayList<String> greetings;

	/*
	 * Random events that can happen ie. chase squirrel
	 */
	public static ArrayList<String> random;

	/*
	 * Responses to actions from user. ie. *throws ball*
	 */
	public static ArrayList<String> actions;

	/*
	 * Responses are used when the user inputs something like bye/goodbye
	 */
	public static ArrayList<String> goodbyes;

	/*
	 * Each input text is defined to a certain grouping, ie. *throw ball* is linked
	 * to the actions list for *runs and grabs ball*
	 */
	public static HashMap<String, String> inputs;
	
	public static Scanner scanman;
	
	public static String dogName = "";
	public static String personName = "";

	public static void main(String[] args) {
		
		//Main scanner input
		scanman = new Scanner(System.in);
		
		Random rand = new Random();

		random = new ArrayList<String>();
		actions = new ArrayList<String>();
		goodbyes = new ArrayList<String>();
		inputs = new LinkedHashMap<String, String>();

		// Load arrays from files
		greetings = loadJson("greetings", "Greetings");
		random = loadJson("random", "Random");
		actions = loadJson("actions", "Actions");
		goodbyes = loadJson("goodbyes", "Goodbyes");
		inputs = loadInputs("inputs");
		
		String networked = prompt("Use socket? Y / N");
		if(networked.equalsIgnoreCase("y")) {
			
		}else {
			//To loop forever until the user says goodbye
			boolean running = true;
			
			//Initial prompt to start
			String input = prompt("Try saying hello, or say what your name is!");
	
			while(running) {
				//Text to ask in next print
				String nextText = "";
				
				//Hard coded for now but sets dog name and the person name
				//TODO: Remove special characters
				if(input.startsWith("My name is")) {
					personName = input.substring(11,input.length());
					input = input.substring(0, 10);
				}else if(input.equalsIgnoreCase("what is your name?")) {
					dogName = prompt("I don't have one! Please give me one:");
				}
				
				//Lowercase and trim input
				input = cleanInput(input);
				
				//If the input is found
				if(inputs.get(input) != null) {
					
					//Get the group of the input
					String cat = inputs.get(input);
					
					if(cat == "Greetings") {
						//Get a random greeting
						nextText = greetings.get(rand.nextInt(greetings.size()));
					}else if(cat == "Actions") {
						//Get the matching action
						nextText = getAction(input);
					}else if(cat == "Goodbyes") {
						//Stop the loop
						running = false;
						break;
					}
				}else if(rand.nextInt(10) > 5) {
					//Chance of a random enounter
					nextText = random.get(rand.nextInt(random.size()));
				}else {
					//Retry input
					nextText = "I didn't understand that";
				}
				//Re prompt user with the new text
				input = prompt(nextText);
			}
			
			//Get a random goodbye to say
			print(goodbyes.get(rand.nextInt(goodbyes.size())));
		}
		
		//Close the scanner
		scanman.close();
	}

	// https://crunchify.com/how-to-read-json-object-from-file-in-java/
	/*
	 * Loads the specified JSON file into a temporary array and returns it.
	 */
	public static ArrayList<String> loadJson(String filename, String section) {
		
		ArrayList<String> tempArray = new ArrayList<String>();
		
		JSONParser parser = new JSONParser();
		
		try {
			//Load json file
			Object fileObject = parser.parse(new FileReader("./" + filename + ".json"));
			JSONObject jsonObject = (JSONObject) fileObject;
			//Get the specified section
			JSONArray jsonArray = (JSONArray) jsonObject.get(section);
			
			//Load into array
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.size(); i++) {
					tempArray.add(jsonArray.get(i).toString());
				}
			}
			
			return tempArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Loads the 3 input groups into the HashMap
	 */
	public static HashMap<String, String> loadInputs(String filename) {
		
		HashMap<String,String> tempMap = new LinkedHashMap<String, String>();
		
		//Load greetings
		ArrayList<String> greetingsInput = loadJson(filename, "Inputs:Greetings");
		for(String in : greetingsInput) {
			tempMap.put(cleanInput(in), "Greetings");
		}
		
		//Load actions
		ArrayList<String> actionsInput = loadJson(filename, "Inputs:Actions");
		for(String in : actionsInput) {
			tempMap.put(cleanInput(in), "Actions");
		}
		
		//Load goodbyes
		ArrayList<String> goodbyesInput = loadJson(filename, "Inputs:Goodbyes");
		for(String in : goodbyesInput) {
			tempMap.put(cleanInput(in), "Goodbyes");
		}
		
		return tempMap;
	}
	
	/*
	 * Trims and lowercases specified string, also replaces the dogname with ${dog} for calling out the dogs name
	 */
	public static String cleanInput(String text) {
		if(dogName != "") {
			text = text.replace(dogName, "${dog}");
		}
		return text.toLowerCase().trim();
	}
	
	/*
	 * Prints the specified text then waits for input
	 */
	public static String prompt(String text) {
		print(text);
		return scanman.nextLine();
	}
	
	/*
	 * Formats the specified text with the dog name or person name before printing
	 */
	public static void print(String text) {
		if(personName != "") text = text.replace("${person}", personName);
		if(dogName != "") text = text.replace("${dog}", dogName);
		if(text != "") System.out.println(text);
	}
	
	/*
	 * Loops through the actions input list and finds the associated action from actions array
	 */
	public static String getAction(String input) {
		ArrayList<String> actionInputs = new ArrayList<String>();
		for (Entry<String, String> entry : inputs.entrySet()) {
			if (entry.getValue().equalsIgnoreCase("actions")) {
				actionInputs.add(entry.getKey());
			}
		}
		return actions.get(actionInputs.indexOf(input));
	}

}
