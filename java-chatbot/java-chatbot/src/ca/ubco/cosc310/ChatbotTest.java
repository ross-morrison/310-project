package ca.ubco.cosc310;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ChatbotTest {
	
	public static ChatbotMain chatbot;
	
	private static ChatbotTestServer serverTest;
	private static Thread serverThread;
	private static Spellcheck sc;
	
	@BeforeAll
    public static void startTests() {
		chatbot = new ChatbotMain(true);
		serverThread = new Thread(){
			public void run() {
				try {
					serverTest = new ChatbotTestServer(8888);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		serverThread.start();
		sc = new Spellcheck("wordList.txt");
    }
	
	@AfterAll
    public static void endTests() {
        try {
			serverTest.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			serverThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	
	@Test
	void testSpellCheck() {
		String in = "I like petnut buuter";
		String expected = "I like peanut butter";
		String gotten = sc.spellcheck(in);
		assertEquals(expected, gotten);
	}
	
	@Test
	void testClientConnection() {
		try {
			ChatbotSocket client = new ChatbotSocket("127.0.0.1", 8888);
			String test = client.send("test");
			assertEquals("test2", test);
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
