package cryptanalysis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import tree.LexicographicTree;

public class DictionaryBasedAnalysis {

	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DICTIONARY = "mots/dictionnaire_FR_sans_accents.txt";

	private static final String CRYPTOGRAM_FILE = "txt/Plus fort que Sherlock Holmes (cryptogram).txt";
	private static final String DECODING_ALPHABET = "VNSTBIQLWOZUEJMRYGCPDKHXAF"; // Sherlock

	private String[] words;
	private final LexicographicTree dict;

	/*
	 * CONSTRUCTOR
	 */
	public DictionaryBasedAnalysis(String cryptogram, LexicographicTree dict) {
		this.dict = dict;
		this.words = Arrays.stream(cryptogram.toUpperCase().split("[^A-Z]+"))
				.filter(word -> word.length() >= 3)
				.sorted(Comparator.comparingInt(String::length).reversed())
				.distinct()
				.collect(Collectors.toCollection(LinkedHashSet::new))
				.toArray(new String[0]);
	}

	/*
	 * PUBLIC METHODS
	 */

	/**
	 * Performs a dictionary-based analysis of the cryptogram and returns an
	 * approximated decoding alphabet.
	 * 
	 * @param alphabet The decoding alphabet from which the analysis starts
	 * @return The decoding alphabet at the end of the analysis process
	 */
	public String guessApproximatedAlphabet(String alphabet) {
		if (alphabet == null) {
			throw new IllegalArgumentException("The alphabet must not be null");
		}
		alphabet = alphabet.toUpperCase();
		if (alphabet.length() < 26 || alphabet.length() > 26) {
			throw new IllegalArgumentException("The alphabet must contain exactly 26 letters");
		}
		if (!alphabet.matches("[A-Z]+")) {
			throw new IllegalArgumentException("The alphabet must contain only letters from A to Z");
		}
		int currentLength = 0;
		int nbWordsFound = getScore(alphabet);
		List<String> wordsWithRepetition = getWordsWithRepetition(alphabet);
		List<String> wordAlreadyChecked = new ArrayList<>();
		List<String> currentWordsOfLength = new ArrayList<>();
		System.out.printf("+------------------------------------------------------------------------+\n");
		System.out.printf("| %-70s |\n", "Starting dictionary-based analysis...");
		System.out.printf("+------------------------------------------------------------------------+\n");
		System.out.printf("| %-70s |\n", "Initial alphabet: " + alphabet);
		System.out.printf("| %-70s |\n", "Initial words length: " + words.length);
		for (String word : wordsWithRepetition) {
			if (currentLength > word.length() || currentLength == 0) {
				currentLength = word.length();
				currentWordsOfLength = dict.getWordsOfLength(currentLength);
				System.out.printf("+------------------------------------------------------------------------+\n");
				System.out.printf("| %-70s |\n", ">> Current length: " + currentLength);
			}
			if (dict.containsWord(applySubstitution(word, alphabet).toLowerCase())) {
				continue;
			}
			String compatibleWord = getCompatibleWord(word, currentWordsOfLength);
			if (compatibleWord != null && !wordAlreadyChecked.contains(compatibleWord)) {
				wordAlreadyChecked.add(compatibleWord);
				String newAlphabet = updateAlphabet(applySubstitution(word, alphabet), compatibleWord, alphabet);
				int newNbWordsFound = getScore(newAlphabet);
				if (newNbWordsFound > nbWordsFound) {
					nbWordsFound = newNbWordsFound;
					alphabet = newAlphabet;
					System.out.printf("| %-70s |\n", ">> New alphabet: " + alphabet);
					System.out.printf("| %-70s |\n", "=> Score decoded: words = " + words.length + " | valid = "
							+ nbWordsFound + " | invalid = " + (words.length - nbWordsFound));
				}
			}
		}
		System.out.printf("+------------------------------------------------------------------------+\n");
		return alphabet.toUpperCase();
	}

	public static String getCompatibleWord(String motChiffre, List<String> mots) {
		for (String mot : mots) {
			if (mot.length() == motChiffre.length()) {
				boolean compatible = true;
				Map<Character, Character> mapping = new HashMap<>();
				for (int i = 0; i < mot.length(); i++) {
					char lettreMot = mot.charAt(i);
					char lettreMotChiffre = motChiffre.charAt(i);

					if (mapping.containsKey(lettreMot)) {
						if (mapping.get(lettreMot) != lettreMotChiffre) {
							compatible = false;
							break;
						}
					} else {
						if (mapping.containsValue(lettreMotChiffre)) {
							compatible = false;
							break;
						}
						mapping.put(lettreMot, lettreMotChiffre);
					}
				}
				if (compatible) {
					return mot.toUpperCase();
				}
			}
		}
		return null;
	}

	public static String updateAlphabet(String crypt, String word, String alphabet) {
		Map<Character, Character> mapping = new HashMap<>();
		for (int i = 0; i < alphabet.length(); i++) {
			char lettreAlphabet = alphabet.charAt(i);
			int index = crypt.indexOf(lettreAlphabet);
			if (index == -1) {
				mapping.put(lettreAlphabet, lettreAlphabet);
			} else {
				mapping.put(lettreAlphabet, word.charAt(index));
			}
		}

		for (Map.Entry<Character, Character> entry : mapping.entrySet()) {
			char lettreAlphabet = entry.getKey();
			char lettreMot = entry.getValue();
			if (lettreAlphabet == lettreMot) {
				updateLetter(mapping, lettreAlphabet);
			}
		}

		String newAlphabet = "";
		for (int i = 0; i < alphabet.length(); i++) {
			char lettreAlphabet = alphabet.charAt(i);
			newAlphabet += mapping.get(lettreAlphabet);
		}
		return newAlphabet;
	}

	/**
	 * Applies an alphabet-specified substitution to a text.
	 * 
	 * @param text     A text
	 * @param alphabet A substitution alphabet
	 * @return The substituted text
	 */
	public static String applySubstitution(String text, String alphabet) {
		if (alphabet == null || text == null) {
			throw new IllegalArgumentException("The alphabet and the text must not be null");
		}
		if (alphabet.length() < 26 || alphabet.length() > 26) {
			throw new IllegalArgumentException("The alphabet must contain exactly 26 letters");
		}
		if (hasRepetition(alphabet)) {
			throw new IllegalArgumentException("The alphabet must not contain any letter more than once");
		}
		String result = "";
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			int index = LETTERS.indexOf(c);
			result += (index == -1) ? c : alphabet.charAt(index);
		}
		return result;
	}

	/*
	 * PRIVATE METHODS
	 */
	/**
	 * Compares two substitution alphabets.
	 * 
	 * @param a First substitution alphabet
	 * @param b Second substitution alphabet
	 * @return A string where differing positions are indicated with an 'x'
	 */
	private static String compareAlphabets(String a, String b) {
		String result = "";
		for (int i = 0; i < a.length(); i++) {
			result += (a.charAt(i) == b.charAt(i)) ? " " : "x";
		}
		return result;
	}

	/**
	 * Load the text file pointed to by pathname into a String.
	 * 
	 * @param pathname A path to text file.
	 * @param encoding Character set used by the text file.
	 * @return A String containing the text in the file.
	 * @throws IOException
	 */
	private static String readFile(String pathname, Charset encoding) {
		String data = "";
		try {
			data = Files.readString(Paths.get(pathname), encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private static void updateLetter(Map<Character, Character> mapping, char lettreAlphabet) {
		char target = findTarget(mapping, lettreAlphabet, lettreAlphabet);
		if (target != lettreAlphabet) {
			mapping.put(lettreAlphabet, target);
		}
	}

	private static char findTarget(Map<Character, Character> mapping, char initial, char current) {
		for (Map.Entry<Character, Character> entry : mapping.entrySet()) {
			char lettreAlphabet2 = entry.getKey();
			char lettreMot2 = entry.getValue();
			if (current == lettreMot2 && lettreAlphabet2 != initial) {
				return findTarget(mapping, initial, lettreAlphabet2);
			}
		}
		return current;
	}

	private List<String> getWordsWithRepetition(String alphabet) {
		List<String> result = new ArrayList<>();
		for (String word : words) {
			if (hasRepetition(word)) {
				result.add(word);
			}
		}
		return result;
	}

	private static boolean hasRepetition(String string) {
		Set<Character> set = new HashSet<>();
		for (int i = 0; i < string.length(); i++) {
			if (set.contains(string.charAt(i))) {
				return true;
			} else {
				set.add(string.charAt(i));
			}
		}
		return false;
	}

	private int getScore(String alphabet) {
		int nbWordsFound = 0;
		for (String word : words) {
			if (dict.containsWord(applySubstitution(word, alphabet).toLowerCase())) {
				nbWordsFound++;
			}
		}

		return nbWordsFound;
	}
	private String randomAlphabet() {
		List<Character> alphabet = new ArrayList<>();
		for (int i = 0; i < LETTERS.length(); i++) {
			alphabet.add(LETTERS.charAt(i));
		}
		Collections.shuffle(alphabet);
		String result = "";
		for (int i = 0; i < alphabet.size(); i++) {
			result += alphabet.get(i);
		}
		return result;
	}

	/*
	 * MAIN PROGRAM
	 */

	public static void main(String[] args) {
		/*
		 * Load dictionary
		 */
		System.out.print("Loading dictionary... ");
		LexicographicTree dict = new LexicographicTree(DICTIONARY);
		System.out.println("done.");
		System.out.println();

		/*
		 * Load cryptogram
		 */
		String cryptogram = readFile(CRYPTOGRAM_FILE, StandardCharsets.UTF_8);
		// System.out.println("*** CRYPTOGRAM ***\n" + cryptogram.substring(0, 100));
		// System.out.println();

		/*
		 * Decode cryptogram
		 */
		long startTime = System.currentTimeMillis();
		DictionaryBasedAnalysis dba = new DictionaryBasedAnalysis(cryptogram, dict);
		String startAlphabet = LETTERS;
		//String startAlphabet = dba.randomAlphabet();
		String finalAlphabet = dba.guessApproximatedAlphabet(startAlphabet);
		System.out.printf("+------------------------------------------------------------------------+\n");
		System.out.printf("| %-70s |\n", "Search time : " + (System.currentTimeMillis() - startTime) / 1000.0);
		System.out.printf("+------------------------------------------------------------------------+\n");
		// Display final results
		System.out.println();
		System.out.println("Decoding     alphabet : " + DECODING_ALPHABET);
		System.out.println("Approximated alphabet : " + finalAlphabet);
		System.out.println("Remaining differences : " + compareAlphabets(DECODING_ALPHABET, finalAlphabet));
		System.out.println();

		// Display decoded text
		System.out.println("*** DECODED TEXT ***\n" + applySubstitution(cryptogram, finalAlphabet).substring(0, 200));
		System.out.println();
	}
}
