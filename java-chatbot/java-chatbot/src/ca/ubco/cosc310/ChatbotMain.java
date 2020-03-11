package ca.ubco.cosc310;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//brandon

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
	 * to the actions list and looks for one with "ball" in it
	 */
	public static HashMap<String, String> inputs;

	public static void main(String[] args) {

		random = new ArrayList<String>();
		actions = new ArrayList<String>();
		goodbyes = new ArrayList<String>();
		inputs = new HashMap<String, String>();

		// Load arrays from files
		greetings = loadJson("greetings", "Greetings");
		random = loadJson("random", "Random");
		actions = loadJson("actions", "Actions");
		goodbyes = loadJson("goodbyes", "Goodbyes");
		inputs = loadInputs("inputs");


		// Prompt user input
		for (String greeting : greetings) {
			System.out.println(greeting);
		}

		// Loop until user says goodbye, then send goodbye message

	}

	// https://crunchify.com/how-to-read-json-object-from-file-in-java/
	public static ArrayList<String> loadJson(String filename, String section) {
		ArrayList<String> tempArray = new ArrayList<String>();
		JSONParser parser = new JSONParser();
		try {
			Object fileObject = parser.parse(new FileReader("./" + filename + ".json"));
			JSONObject jsonObject = (JSONObject) fileObject;
			JSONArray jsonArray = (JSONArray) jsonObject.get(section);
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
	
	public static HashMap<String, String> loadInputs(String filename) {
		
		HashMap<String,String> tempMap = new HashMap<String, String>();
		
		ArrayList<String> greetingsInput = loadJson(filename, "Inputs:Greetings");
		for(String in : greetingsInput) {
			tempMap.put(in, "Greetings");
		}
		
		ArrayList<String> actionsInput = loadJson(filename, "Inputs:Actions");
		for(String in : actionsInput) {
			tempMap.put(in, "Actions");
		}
		
		ArrayList<String> goodbyesInput = loadJson(filename, "Inputs:Goodbyes");
		for(String in : actionsInput) {
			tempMap.put(in, "Goodbyes");
		}
		
		return tempMap;

	}

}
