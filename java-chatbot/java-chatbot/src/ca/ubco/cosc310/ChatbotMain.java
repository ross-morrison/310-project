package ca.ubco.cosc310;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

//test commit

/*
 * Created 2/25/2020
 * @authors Ross Morrison, Brandon Gaucher, Eric Shanks, Ben Fitzharris 
 */
public class ChatbotMain {

	/*
	 * Responses are used when the user inputs something like hey/hello
	 */
	public ArrayList<String> greetings;

	/*
	 * Random events that can happen ie. chase squirrel
	 */
	public ArrayList<String> random;

	/*
	 * Responses to actions from user. ie. *throws ball*
	 */
	public ArrayList<String> actions;

	/*
	 * Responses are used when the user inputs something like bye/goodbye
	 */
	public ArrayList<String> goodbyes;
	
	/*
	 * This is used to store the input and its associated POS tags
	 */
	public ArrayList<Word> parsedInput;
	

	/*
	 * Each input text is defined to a certain grouping, ie. *throw ball* is linked
	 * to the actions list for *runs and grabs ball*
	 */
	public HashMap<String, String> inputs;
	
	public String dogName = "";
	public String personName = "";
	
	public boolean running = true;
	public Random rand;
	
	public String input;
	
	private String taggedInput = "";
	private MaxentTagger posTag;
	
	public ChatbotGUI gui;
	public Spellcheck spellchecker;

	public static void main(String[] args) {
		new ChatbotMain();
	}
	
	public ChatbotMain() {

		//Main scanner input
		rand = new Random();
		
		gui = new ChatbotGUI(this);
		
		posTag = new MaxentTagger("english-left3words-distsim.tagger");
		
		spellchecker = new Spellcheck("wordList.txt");

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
		
		//Initial prompt to start
		prompt("Try saying hello, or say what your name is!");
		String previous = "";
		
		boolean setDogNameAsNext = false;
		boolean realNameCheck = false;


		boolean networked = false;
		if(networked) {
			String ip = "";
			int port = 1234;
			try {
				ChatbotSocket socket = new ChatbotSocket(ip, port);
				boolean running = true;
				
				//initial input from other bot
				String input = socket.send("Try saying hello, or say what your name is!");
				
				while(running) {
					String nextText = "";
					
					//clean input
					if(input == null) {
						System.out.println("Error reading input...");
						input = "error";
					}
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
					//Re prompt other bot with new text
					input = socket.send(nextText);
					System.out.println("local: " + nextText);
					System.out.println("remote: " + input);
				}
			}catch (IOException e) {
				System.out.println("Connection was lost or corrupted.");
				e.printStackTrace();
			}
		}else {
			synchronized (this) {
				while(running) {
					//Text to ask in next print
					String nextText = "";
					
					//Hard coded for now but sets dog name and the person name
					//TODO: Remove special characters
					while(input == null || input == previous) {
						try {
							System.out.println("waiting");
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					
					
					if(setDogNameAsNext) {
						dogName = input;
						System.out.println("set dog name: " + dogName);
						
						setDogNameAsNext = false;
						input = "what is your name";
						continue;
					}
					
					if(realNameCheck) {
						parseInput(input);
						if(!input.startsWith("my name is")) {
							if(NNPCheck(parsedInput.get(0))) {
								personName = parsedInput.get(0).getContent();
								realNameCheck = false;
								input = "my name is";
								continue;
							}
							prompt("Sorry but I don't think that's a real name, What is your real name? ");
							input = null;
							realNameCheck = true;
							continue;
						}
					}
					
					if(input.startsWith("my name is") && personName == "") {
						parseInput(input);
						if(!NNPCheck(parsedInput.get(3))) {
							System.out.println(parsedInput.get(3).getIdentifier());
							prompt("Sorry but I don't think that's a real name, What is your real name? ");
							input = null;
							realNameCheck = true;
							continue;
						}
						personName = input.substring(11,input.length());
						input = input.substring(0, 10);
					}else if(input.startsWith("what is your name") && dogName == "") {
						prompt("I don't have one! Please give me one:");
						setDogNameAsNext = true;
						input = null;
						continue;
					}
					
					
					parseInput(input);
					input = cleanInput(input);
					
					System.out.println("Input before: " + input);
					input = spellchecker.spellcheck(input);
					System.out.println("Input after: " + input);
					
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
					previous = input;
					prompt(nextText);
				}
			}
		}
		
		
		//Get a random goodbye to say
		gui.addBotText(goodbyes.get(rand.nextInt(goodbyes.size())));
		
	}
	
	/*
	 * Test case constructor
	 */
	public ChatbotMain(boolean test) {
		//Main scanner input
		rand = new Random();
		spellchecker = new Spellcheck("wordList.txt");
		random = new ArrayList<String>();
		actions = new ArrayList<String>();
		goodbyes = new ArrayList<String>();
		inputs = new LinkedHashMap<String, String>();
		parsedInput = new ArrayList<Word>();
	}
	
	// https://crunchify.com/how-to-read-json-object-from-file-in-java/
	/*
	 * Loads the specified JSON file into a temporary array and returns it.
	 */
	public ArrayList<String> loadJson(String filename, String section) {
		
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
	public HashMap<String, String> loadInputs(String filename) {
		
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
	public String cleanInput(String text) {
		if(dogName != "") {
			text = text.replace(dogName, "${dog}");
		}
		return text.toLowerCase().trim();
	}
	
	/*
	 * Prints the specified text then waits for input
	 */
	public String prompt(String text) {
		if(personName != "") text = text.replace("${person}", personName);
		if(dogName != "") text = text.replace("${dog}", dogName);
		gui.addBotText(text);
		System.out.println("prompt");
		return gui.nextMessage;
	}
	

	/*
	 * Loops through the actions input list and finds the associated action from actions array
	 */
	public String getAction(String input) {
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
	public boolean NNPCheck(Word word) {
		if(word.getIdentifier().equalsIgnoreCase("NNP"))
			return true;
		else {
			return false;
		}
	}
	
	/*
	 * Method parse's input into parseInput as Word objects
	 */
	
	public void parseInput(String input) {
		parsedInput.clear();
		
		String[] tempStringArray = new String[20];

		taggedInput = posTag.tagTokenizedString(input);
		
		tempStringArray = taggedInput.split(" ");
		
		for(String words : tempStringArray) {
			String[] temp = new String[2];
			temp = words.split("_");
			parsedInput.add(new Word(temp[0], temp[1]));	
		}
		
	}

}
