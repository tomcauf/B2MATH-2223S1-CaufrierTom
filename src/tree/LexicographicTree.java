package tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LexicographicTree {
	static class Node {
		boolean isEndOfWord;
		Node child;
		Node sibling;
		char value;

		Node(char value) {
			this.value = value;
			this.isEndOfWord = false;
		}
	}

	private Node root;
	/*
	 * CONSTRUCTORS
	 */

	/**
	 * Constructor : creates an empty lexicographic tree.
	 */
	public LexicographicTree() {
		root = new Node('\0');
	}

	/**
	 * Constructor : creates a lexicographic tree populated with words
	 * 
	 * @param filename A text file containing the words to be inserted in the tree
	 */
	public LexicographicTree(String filename) {
		this();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String word = line.trim();
				if (!word.isEmpty()) {
					insertWord(word);
				}
			}
		} catch (IOException e) {
			System.err.println("Error while reading the file: " + e.getMessage());
		}
	}

	/*
	 * PUBLIC METHODS
	 */

	/**
	 * Returns the number of words present in the lexicographic tree.
	 * 
	 * @return The number of words present in the lexicographic tree
	 */
	public int size() {
		return sizeRecursive(root);
	}

	/**
	 * Inserts a word in the lexicographic tree if not already present.
	 * 
	 * @param word A word
	 */
	public void insertWord(String word) {
		if (word != null && word.trim().length() > 0) {
			insertWordRecursive(root, word);
		}
	}

	/**
	 * Determines if a word is present in the lexicographic tree.
	 * 
	 * @param word A word
	 * @return True if the word is present, false otherwise
	 */
	public boolean containsWord(String word) {
		return word != null && !word.isEmpty() && containsWordRecursive(root, word);
	}

	/**
	 * Determines if a prefix is present in the lexicographic tree.
	 * 
	 * @param prefix A prefix
	 * @return True if the prefix is present, false otherwise
	 */
	public boolean containsPrefix(String prefix) {
		if (prefix == null || prefix.isEmpty()) {
			return false;
		}
		return containsPrefixRecursive(root, prefix);
	}

	/**
	 * Returns an alphabetic list of all words starting with the supplied prefix.
	 * If 'prefix' is an empty string, all words are returned.
	 * 
	 * @param prefix Expected prefix
	 * @return The list of words starting with the supplied prefix
	 */
	public List<String> getWords(String prefix) {
		List<String> words = new ArrayList<>();
		if (prefix == null || prefix.trim().isEmpty()) {
			getWordsRecursive(root, "", words);
		} else {
			Node prefixNode = findPrefixNode(root, prefix);
			if (prefixNode != null) {
				getWordsRecursive(prefixNode, prefix, words);
			}
		}
		return words;
	}

	/**
	 * Returns an alphabetic list of all words of a given length.
	 * If 'length' is lower than or equal to zero, an empty list is returned.
	 * 
	 * @param length Expected word length
	 * @return The list of words with the given length
	 */
	public List<String> getWordsOfLength(int length) {
		List<String> words = new ArrayList<>();
		getWordsOfLengthRecursive(root, "", length, words);
		return words;
	}

	/*
	 * PRIVATE METHODS
	 */

	private int sizeRecursive(Node node) {
		if (node == null) {
			return 0;
		}

		int size = node.isEndOfWord ? 1 : 0;

		for (Node child = node.child; child != null; child = child.sibling) {
			size += sizeRecursive(child);
		}

		return size;
	}

	private void insertWordRecursive(Node node, String word) {
		if (word.isEmpty()) {
			node.isEndOfWord = true;
			return;
		}
		char c = word.charAt(0);
		String remainingWord = word.substring(1);

		Node child = node.child;
		Node previousChild = null;
		while (child != null && child.value < c) {
			previousChild = child;
			child = child.sibling;
		}

		if (child != null && child.value == c) {
			insertWordRecursive(child, remainingWord);
		} else {
			Node newNode = new Node(c);
			newNode.sibling = child;

			if (previousChild != null) {
				previousChild.sibling = newNode;
			} else {
				node.child = newNode;
			}

			insertWordRecursive(newNode, remainingWord);
		}
	}

	private boolean containsWordRecursive(Node node, String word) {
		if (word.isEmpty()) {
			return node.isEndOfWord;
		}

		char c = word.charAt(0);
		String remainingWord = word.substring(1);

		for (Node child = node.child; child != null; child = child.sibling) {
			if (child.value == c) {
				return containsWordRecursive(child, remainingWord);
			} else if (child.value > c) {
				break;
			}
		}
		return false;
	}

	private boolean containsPrefixRecursive(Node node, String prefix) {
		if (prefix.isEmpty()) {
			return true;
		}

		char c = prefix.charAt(0);
		String remainingPrefix = prefix.substring(1);

		for (Node child = node.child; child != null; child = child.sibling) {
			if (child.value == c) {
				return containsPrefixRecursive(child, remainingPrefix);
			} else if (child.value > c) {
				break;
			}
		}
		return false;
	}

	private void getWordsRecursive(Node node, String prefix, List<String> words) {
		if (node == null) {
			return;
		}

		if (node.isEndOfWord) {
			words.add(prefix);
		}

		for (Node child = node.child; child != null; child = child.sibling) {
			getWordsRecursive(child, prefix + child.value, words);
		}
	}

	private Node findPrefixNode(Node node, String prefix) {
		if (prefix.isEmpty()) {
			return node;
		}

		char firstChar = prefix.charAt(0);
		String remainingPrefix = prefix.substring(1);

		for (Node child = node.child; child != null; child = child.sibling) {
			if (child.value == firstChar) {
				return findPrefixNode(child, remainingPrefix);
			} else if (child.value > firstChar) {
				break;
			}
		}

		return null;
	}

	private void getWordsOfLengthRecursive(Node node, String prefix, int length, List<String> words) {
		if (node == null) {
			return;
		}

		if (length == 0 && node.isEndOfWord) {
			words.add(prefix);
		} else if (length > 0) {
			for (Node child = node.child; child != null; child = child.sibling) {
				getWordsOfLengthRecursive(child, prefix + child.value, length - 1, words);
			}
		}
	}

	/*
	 * TEST FUNCTIONS
	 */

	private static String numberToWordBreadthFirst(long number) {
		String word = "";
		int radix = 13;
		do {
			word = (char) ('a' + (int) (number % radix)) + word;
			number = number / radix;
		} while (number != 0);
		return word;
	}

	private static void testDictionaryPerformance(String filename) {
		long startTime;
		int repeatCount = 20;

		// Create tree from list of words
		startTime = System.currentTimeMillis();
		System.out.println("Loading dictionary...");
		LexicographicTree dico = null;
		for (int i = 0; i < repeatCount; i++) {
			dico = new LexicographicTree(filename);
		}
		System.out.println("Load time : " + (System.currentTimeMillis() - startTime) / 1000.0);
		System.out.println("Number of words : " + dico.size());
		System.out.println();

		// Search existing words in dictionary
		startTime = System.currentTimeMillis();
		System.out.println("Searching existing words in dictionary...");
		File file = new File(filename);
		for (int i = 0; i < repeatCount; i++) {
			Scanner input;
			try {
				input = new Scanner(file);
				while (input.hasNextLine()) {
					String word = input.nextLine();
					boolean found = dico.containsWord(word);
					if (!found) {
						System.out.println(word + " / " + word.length() + " -> " + found);
					}
				}
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Search time : " + (System.currentTimeMillis() - startTime) / 1000.0);
		System.out.println();

		// Search non-existing words in dictionary
		startTime = System.currentTimeMillis();
		System.out.println("Searching non-existing words in dictionary...");
		for (int i = 0; i < repeatCount; i++) {
			Scanner input;
			try {
				input = new Scanner(file);
				while (input.hasNextLine()) {
					String word = input.nextLine() + "xx";
					boolean found = dico.containsWord(word);
					if (found) {
						System.out.println(word + " / " + word.length() + " -> " + found);
					}
				}
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Search time : " + (System.currentTimeMillis() - startTime) / 1000.0);
		System.out.println();

		// Search words of increasing length in dictionary
		startTime = System.currentTimeMillis();
		System.out.println("Searching for words of increasing length...");
		for (int i = 0; i < 4; i++) {
			int total = 0;
			for (int n = 0; n <= 28; n++) {
				int count = dico.getWordsOfLength(n).size();
				total += count;
			}
			if (dico.size() != total) {
				System.out.printf("Total mismatch : dict size = %d / search total = %d\n", dico.size(), total);
			}
		}
		System.out.println("Search time : " + (System.currentTimeMillis() - startTime) / 1000.0);
		System.out.println();
	}

	private static void testDictionarySize() {
		final int MB = 1024 * 1024;
		System.out.print(Runtime.getRuntime().totalMemory() / MB + " / ");
		System.out.println(Runtime.getRuntime().maxMemory() / MB);

		LexicographicTree dico = new LexicographicTree();
		long count = 0;
		while (true) {
			dico.insertWord(numberToWordBreadthFirst(count));
			count++;
			if (count % MB == 0) {
				System.out.println(count / MB + "M -> " + Runtime.getRuntime().freeMemory() / MB);
			}
		}
	}

	/*
	 * MAIN PROGRAM
	 */

	public static void main(String[] args) {
		// CTT : test de performance insertion/recherche
		testDictionaryPerformance("mots/dictionnaire_FR_sans_accents.txt");

		// CST : test de taille maximale si VM -Xms2048m -Xmx2048m
		testDictionarySize();
	}
}
