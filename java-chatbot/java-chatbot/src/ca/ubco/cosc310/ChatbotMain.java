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

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


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
	 * This is used to store the input and its associated POS tags
	 */
	public static ArrayList<Word> parsedInput;

	/*
	 * Each input text is defined to a certain grouping, ie. *throw ball* is linked
	 * to the actions list for *runs and grabs ball*
	 */
	public static HashMap<String, String> inputs;
	
	public static Scanner scanman;
	
	public static String dogName = "";
	public static String personName = "";
	private static String taggedInput = "";

	public static void main(String[] args) {
		
		//Main scanner input
		scanman = new Scanner(System.in);
		//POS tagger initialize
		//MaxentTagger posTag = new MaxentTagger("english-left3words-distsim.tagger");
		
		Random rand = new Random();

		random = new ArrayList<String>();
		actions = new ArrayList<String>();
		goodbyes = new ArrayList<String>();
		inputs = new LinkedHashMap<String, String>();
		parsedInput = new ArrayList<Word>();

		// Load arrays from files
		greetings = loadJson("greetings", "Greetings");
		random = loadJson("random", "Random");
		actions = loadJson("actions", "Actions");
		goodbyes = loadJson("goodbyes", "Goodbyes");
		inputs = loadInputs("inputs");

		

		//To loop forever until the user says goodbye
		boolean running = true;
		
		//Initial prompt to start
		String input = prompt("Try saying hello, or say what your name is!");

		while(running) {
			//Text to ask in next print
			String nextText = "";
			
			
			//Hard coded for now but sets dog name and the person name
			//TODO: Remove special characters
			
			//parse and POS tag input
			parseInput(input);
			
			//Chatbot rules for response
			
			
			//logic for inputting name
			if(input.startsWith("My name is")) {
				//error handling in case a singular pronoun is not entered
				while(!NNPCheck(parsedInput.get(3))) {
					input = prompt("Sorry but I don't think that's a real name, What is your real name? ");
					parseInput(input);
					if(!input.startsWith("My name is")) {
						parsedInput.add(new Word());
						parsedInput.add(new Word());
						parsedInput.add(new Word());
						parsedInput.set(3, parsedInput.get(0) );
						continue;
					}
				}
				personName = parsedInput.get(3).getContent();
				input = "My name is ";
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
				if(parsedInput.get(0).getIdentifier().equalsIgnoreCase("UH")) {
					nextText = "What's wrong?";
				}else if(parsedInput.get(parsedInput.size() - 1).getIdentifier().equalsIgnoreCase("$")) {
					nextText = "I don't like money, but I'll fetch the ball for treats!";
				}
				else {
					nextText = "I didn't understand that";
					
				}
				
			}
			//Re prompt user with the new text
			input = prompt(nextText);
			
			//empty parseInput to be clear for next input
			parsedInput.clear();
		}
		
		//Get a random goodbye to say
		print(goodbyes.get(rand.nextInt(goodbyes.size())));
		
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
	
	/*
	 * Method to check if the desired word is a proper noun singular
	 */
	public static boolean NNPCheck(Word word) {
		if(word.getIdentifier().equalsIgnoreCase("NNP"))
			return true;
		else {
			return false;
		}
	}
	
	/*
	 * Method parse's input into parseInput as Word objects
	 */
	
	public static void parseInput(String input) {
		parsedInput.clear();
		
		MaxentTagger posTag = new MaxentTagger("english-left3words-distsim.tagger");
		
		String[] tempStringArray = new String[20];

		taggedInput = posTag.tagTokenizedString(input);
		//System.out.println(taggedInput);
		tempStringArray = taggedInput.split(" ");
		
		for(String words : tempStringArray) {
			String[] temp = new String[2];
			temp = words.split("_");
			parsedInput.add(new Word(temp[0], temp[1]));	
		}
		
	}

}
