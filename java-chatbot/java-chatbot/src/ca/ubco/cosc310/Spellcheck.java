package ca.ubco.cosc310;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Spellcheck {
	
	/*
	 * Dictionary arraylist
	 */
	public ArrayList<String> dictionary;

	public Spellcheck(String filename) {
		dictionary = loadWords(filename);
	}

	public  String spellcheck(String input) {
		// loop through user string check each word
		String fixedInput = "";
		for (String word : input.split("\\s+")) {
			// if word is in dictionary add it back to input
			if (word.length() > 4) {
				if (dictionary.contains(word.toLowerCase())) {
					fixedInput = fixedInput + " " + word;
				}
				// if word not in dictionary, find closest match, then add it back to input
				else if (!dictionary.contains(word.toLowerCase())) {
					String fixedWord = fixWord(word);
					fixedInput = fixedInput + " " + fixedWord;
				}
			}else {
				fixedInput = fixedInput + " " + word;
			}
		}

		return fixedInput.trim();
	}

	public String fixWord(String word) {
		String fixedWord = word;
		int allWords = dictionary.size();
		// loop through all words in the dictionary
		for (int i = 0; i < allWords; i++) {
			
			// initalize min distance to something that will not affect first run
			int minDist = 100;
			// find the min distance between misspelled word and word in dictionary
			int newMinDist = distance(word, dictionary.get(i).toLowerCase());
			if (newMinDist < minDist) {
				minDist = newMinDist;
				// set fixedword to smallest changed word found in the dictionary
				if (minDist < 2) {
					fixedWord = dictionary.get(i).toLowerCase();
				}
			}
		}
		return fixedWord;
	}

	/* from http://rosettacode.org/wiki/Levenshtein_distance */
	/* Finds the minimum distance (change in letters) between two strings */
	public int distance(String a, String b) {
		a = a.toLowerCase();
		b = b.toLowerCase();
		// i == 0
		int[] costs = new int[b.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= a.length(); i++) {
			// j == 0; nw = lev(i - 1, j)
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
						a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}
	
	/*
	 * Loads text file by line into Array
	 */
	public ArrayList<String> loadWords(String filename) {
		ArrayList<String> tempArray = new ArrayList<String>();
		
		try {
			File f = new File("./" + filename);
			FileReader fr = new FileReader(f);
			BufferedReader reader = new BufferedReader(fr);
			String curLine;
			while((curLine = reader.readLine()) != null) {
				tempArray.add(curLine);
			}
			
			reader.close();
			
			return tempArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
