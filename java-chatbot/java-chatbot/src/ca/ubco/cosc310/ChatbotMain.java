package ca.ubco.cosc310;

import java.util.ArrayList;
import java.util.HashMap;


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
	 * Each input text is defined to a certain grouping, ie. *throw ball* is linked to the actions list and looks for one with "ball" in it
	 */
	public HashMap<String, String> inputs;

	public static void main(String[] args) {
		System.out.println("Enter your dogs name: ");
		
		System.out.println("That's a great name! ${dog-name}");
		
		System.out.println("*${dog-name} sniffs you*");
		
		System.out.println("*${dog-name} closes its eyes in disgust*");
		
		System.out.println("Change");
		
	}

}
