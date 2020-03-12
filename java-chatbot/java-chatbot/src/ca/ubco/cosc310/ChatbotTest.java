package ca.ubco.cosc310;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

class ChatbotTest {
	
	@Test
	void testPrint() {
		ChatbotMain.print("Test");
		return;
	}
	
	@Test
	void testCleanInput() {
		String expected = "aaa ${dog}";
		ChatbotMain.dogName = "doggo";
	
		String gotten = ChatbotMain.cleanInput("AAA doggo  ");
		assertEquals(expected, gotten);
	}
	
	@Test
	void testGetAction() {
		String[] testArray = {"Test"};
		ChatbotMain.actions = new ArrayList<String>(Arrays.asList(testArray));
		ChatbotMain.inputs = new LinkedHashMap<String, String>();
		ChatbotMain.inputs.put("test", "actions");
		
		assertEquals("Test", ChatbotMain.getAction("test"));
	}
	
	@Test
	void testLoadJSON() {
		String expected = "tester";
		
		ArrayList<String> jsonArray = ChatbotMain.loadJson("test", "test");
		
		assertEquals(expected, jsonArray.get(0));
		
	}
	
	@Test
	void testLoadInputs() {
		HashMap<String, String> expected = new LinkedHashMap<String, String>();
		expected.put("testergreet", "Greetings");
		expected.put("testeraction", "Actions");
		expected.put("testergoodbye", "Goodbyes");
		
		HashMap<String, String> gotten = ChatbotMain.loadInputs("test");
		
		assertEquals(expected, gotten);
	}

}
