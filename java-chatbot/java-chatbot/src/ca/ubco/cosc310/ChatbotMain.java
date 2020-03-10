package ca.ubco.cosc310;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


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
	 * Each input text is defined to a certain grouping, ie. *throw ball* is linked to the actions list and looks for one with "ball" in it
	 */
	public static HashMap<String, String> inputs;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		greetings = new ArrayList<String>();
		random = new ArrayList<String>();
		actions = new ArrayList<String>();
		goodbyes = new ArrayList<String>();
		inputs = new HashMap<String, String>();
		
		//Load arrays from files
		//https://crunchify.com/how-to-read-json-object-from-file-in-java/
		JSONParser parser = new JSONParser();
		try {
			Object fileObject = parser.parse(new FileReader("./greetings.json"));
			JSONObject greetingsObject = (JSONObject) fileObject;
			JSONArray greetingsList = (JSONArray) greetingsObject.get("Greetings");
			
			Iterator<String> iterator = greetingsList.iterator();
			while (iterator.hasNext()) {
				greetings.add(iterator.next());
			}
			
			fileObject = parser.parse(new FileReader("./random.json"));
			JSONObject randomObject = (JSONObject) fileObject;
			JSONArray randomList = (JSONArray) randomObject.get("Random");
			
			iterator = randomList.iterator();
			while (iterator.hasNext()) {
				random.add(iterator.next());
			}
			
			fileObject = parser.parse(new FileReader("./actions.json"));
			JSONObject actionsObject = (JSONObject) fileObject;
			JSONArray actionsList = (JSONArray) actionsObject.get("Actions");
			
			iterator = actionsList.iterator();
			while (iterator.hasNext()) {
				actions.add(iterator.next());
			}
			
			fileObject = parser.parse(new FileReader("./goodbyes.json"));
			JSONObject goodbyesObject = (JSONObject) fileObject;
			JSONArray goodbyesList = (JSONArray) goodbyesObject.get("Goodbyes");
			
			iterator = goodbyesList.iterator();
			while (iterator.hasNext()) {
				goodbyes.add(iterator.next());
			}
			
			fileObject = parser.parse(new FileReader("./inputs.json"));
			JSONObject inputsObject = (JSONObject) fileObject;
			
			JSONArray inputsGreetingsList = (JSONArray) inputsObject.get("Inputs:Greetings");
			iterator = inputsGreetingsList.iterator();
			while (iterator.hasNext()) {
				inputs.put( iterator.next(), "Greetings");
			}
			
			JSONArray inputsGoodbyesList = (JSONArray) inputsObject.get("Inputs:Goodbyes");
			iterator = inputsGoodbyesList.iterator();
			while (iterator.hasNext()) {
				inputs.put(iterator.next(), "Goodbyes");
			}
			
			JSONArray inputsActionsList = (JSONArray) inputsObject.get("Inputs:Actions");
			iterator = inputsActionsList.iterator();
			while (iterator.hasNext()) {
				inputs.put(iterator.next(), "Actions");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Prompt user input
		for(String greeting : greetings) {
			System.out.println(greeting);
		}
		
		//Loop until user says goodbye, then send goodbye message
		
		
	}

}
