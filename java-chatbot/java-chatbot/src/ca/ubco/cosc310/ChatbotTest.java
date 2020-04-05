package ca.ubco.cosc310;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ChatbotTest {
	
	public ChatbotMain chatbot;
	
	@BeforeAll
    void startTests() {
		chatbot = new ChatbotMain();
    }
	
	
	@Test
	void testCleanInput() {
		String expected = "aaa ${dog}";
		chatbot.dogName = "doggo";
	
		String gotten = chatbot.cleanInput("AAA doggo  ");
		assertEquals(expected, gotten);
	}
	
	@Test
	void testGetAction() {
		String[] testArray = {"Test"};
		chatbot.actions = new ArrayList<String>(Arrays.asList(testArray));
		chatbot.inputs = new LinkedHashMap<String, String>();
		chatbot.inputs.put("test", "actions");
		
		assertEquals("Test", chatbot.getAction("test"));
	}
	
	@Test
	void testLoadJSON() {
		String expected = "tester";
		
		ArrayList<String> jsonArray = chatbot.loadJson("test", "test");
		
		assertEquals(expected, jsonArray.get(0));
		
	}
	
	@Test
	void testLoadInputs() {
		HashMap<String, String> expected = new LinkedHashMap<String, String>();
		expected.put("testergreet", "Greetings");
		expected.put("testeraction", "Actions");
		expected.put("testergoodbye", "Goodbyes");
		
		HashMap<String, String> gotten = chatbot.loadInputs("test");
		
		assertEquals(expected, gotten);
	}

}
