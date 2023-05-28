package tree;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/* ---------------------------------------------------------------- */

/*
 * Constructor
 */
public class LexicographicTreeTest {
	private static final String[] WORDS = new String[] {"a-cote", "aide", "as", "au","aujourd'hui", "aux",
			"bu", "bus", "but", "cote", "et", "ete" };
	private static final String DICTIONNAIRE = "mots/dictionnaire_FR_sans_accents.txt";
	private static final LexicographicTree DICT = new LexicographicTree();

	@BeforeAll
	public static void initTestDictionary() {
		for (int i = 0; i < WORDS.length; i++) {
			DICT.insertWord(WORDS[i]);
		}
	}

	@AfterAll
	public static void clearTest() {
		removeFile("test.txt");
	}

	@Test
	void constructor_EmptyDictionary() {
		LexicographicTree dict = new LexicographicTree();
		assertNotNull(dict);
		assertEquals(0, dict.size());
	}

	@Test
	void constructor_DictionaryFromFile() {
		LexicographicTree dict = new LexicographicTree(DICTIONNAIRE);
		assertNotNull(dict);
		assertEquals(327956, dict.size());
	}

	@Test
	void constructor_DictionaryFromBadFile() {
		LexicographicTree dict = new LexicographicTree("mots/pasUnFichier.txt");
		assertNotNull(dict);
		assertEquals(0, dict.size());
	}

	@Test
	void constructor_DictionaryEmpty() {
		createFile("test.txt");
		LexicographicTree dict = new LexicographicTree("mots/test.txt");
		assertNotNull(dict);
		assertEquals(0, dict.size());
	}

	@Test
	void insertWord_General() {
		LexicographicTree dict = new LexicographicTree();
		for (int i = 0; i < WORDS.length; i++) {
			dict.insertWord(WORDS[i]);
			assertEquals(i + 1, dict.size(), "Mot " + WORDS[i] + " non inséré");
			dict.insertWord(WORDS[i]);
			assertEquals(i + 1, dict.size(), "Mot " + WORDS[i] + " en double");
		}
	}

	@Test
	void insertBadWord() {
		LexicographicTree dict = new LexicographicTree();
		dict.insertWord("");
		assertEquals(0, dict.size());
		dict.insertWord(" ");
		assertEquals(0, dict.size());
		dict.insertWord(null);
		assertEquals(0, dict.size());
	}

	@Test
	void testInsertWordWithHyphenAndApostrophe() {

		LexicographicTree tree = new LexicographicTree();
		tree.insertWord("aujourd'hui");
		tree.insertWord("tire-bouchon");
		assertTrue(tree.containsWord("aujourd'hui"));
		assertTrue(tree.containsWord("tire-bouchon"));
		assertEquals(2, tree.size());
	}

	@Test
	void containsWord_General() {
		for (String word : WORDS) {
			assertTrue(DICT.containsWord(word), "Mot " + word + " non trouvé");
		}
		for (String word : new String[] { "", "aid", "ai", "aides", "mot", "e" }) {
			assertFalse(DICT.containsWord(word), "Mot " + word + " inexistant trouvé");
		}
	}

	@Test
	void containsWord_BadValue() {
		assertFalse(DICT.containsWord(""));
		assertFalse(DICT.containsWord(null));
	}
	
	@Test
	void containsWords() {
		LexicographicTree dict = new LexicographicTree();
		dict.insertWord("aux");
		dict.insertWord("aura");
		dict.insertWord("bus");
		dict.insertWord("au");
		dict.insertWord("as");
		dict.insertWord("dure");

		assertTrue(dict.containsWord("aux"));
		assertTrue(dict.containsWord("aura"));
		assertTrue(dict.containsWord("au"));
		assertTrue(dict.containsWord("as"));
		assertTrue(dict.containsWord("dure"));
		
		assertFalse(dict.containsWord("auxe"));
		assertFalse(dict.containsWord("aurar"));
		assertFalse(dict.containsWord("ada"));
		assertFalse(dict.containsWord("ase"));
		assertFalse(dict.containsWord("dures"));
		assertFalse(dict.containsWord("asas"));
	}

	@Test
	void getWords_General() {
		assertEquals(WORDS.length, DICT.getWords("").size());
		assertArrayEquals(WORDS, DICT.getWords("").toArray());

		assertEquals(0, DICT.getWords("x").size());

		assertEquals(3, DICT.getWords("bu").size());
		assertArrayEquals(new String[] { "bu", "bus", "but" }, DICT.getWords("bu").toArray());
	}

	@Test
	void getWords_BadValue() {
		assertEquals(WORDS.length, DICT.getWords(null).size());
		assertEquals(WORDS.length, DICT.getWords("").size());
		assertEquals(WORDS.length, DICT.getWords(" ").size());
		assertEquals(WORDS.length, DICT.getWords("  ").size());
	}

	@Test
	void getWordsOfLength_General() {
		assertEquals(4, DICT.getWordsOfLength(3).size());
		assertArrayEquals(new String[] { "aux", "bus", "but", "ete" }, DICT.getWordsOfLength(3).toArray());
	}
	@Test
	void getWordsOfLength_Negative() {
		assertEquals(0, DICT.getWordsOfLength(-1).size());
		assertArrayEquals(new String[] { }, DICT.getWordsOfLength(-1).toArray());
	}

	@Test
	void size() {
		LexicographicTree dict = new LexicographicTree();
		assertEquals(0, dict.size());
		dict.insertWord("a");
		assertEquals(1, dict.size());
		dict.insertWord("a");
		assertEquals(1, dict.size());
		dict.insertWord("");
		assertEquals(1, dict.size());
		dict.insertWord(" ");
		assertEquals(1, dict.size());
		dict.insertWord("\0");
		assertEquals(1, dict.size());
		LexicographicTree dict2 = new LexicographicTree(DICTIONNAIRE);
		assertEquals(327956, dict2.size());
	}

	@Test
	void testSearchingForWordsOfIncreasingLength() {
		LexicographicTree dico = new LexicographicTree(DICTIONNAIRE);
		for (int i = 0; i < 4; i++) {
			int total = 0;
			for (int n = 0; n <= 28; n++) {
				int count = dico.getWordsOfLength(n).size();
				total += count;
			}
			assertEquals(dico.size(), total);
		}
	}

	@Test
	void testSearchingNonExistingWordsInDictionary() {
		int repeatCount = 20;
		File file = new File(DICTIONNAIRE);
		LexicographicTree dico = new LexicographicTree(DICTIONNAIRE);
		for (int i = 0; i < repeatCount; i++) {
			Scanner input;
			try {
				input = new Scanner(file);
				while (input.hasNextLine()) {
					String word = input.nextLine() + "xx";
					boolean found = dico.containsWord(word);
					if (found) {
						assertTrue(false, word + " / " + word.length() + " -> " + found);
					}
				}
				input.close();
			} catch (FileNotFoundException e) {
				assertTrue(false, "File not found: " + DICTIONNAIRE);
			}
		}
	}

	@Test
	void testSearchingExistingWordsInDictionary() {
		int repeatCount = 20;
		File file = new File(DICTIONNAIRE);
		LexicographicTree dico = new LexicographicTree(DICTIONNAIRE);
		for (int i = 0; i < repeatCount; i++) {
			Scanner input;
			try {
				input = new Scanner(file);
				while (input.hasNextLine()) {
					String word = input.nextLine();
					boolean found = dico.containsWord(word);
					if (!found) {
						assertTrue(false, word + " / " + word.length() + " -> " + found);
					}
				}
				input.close();
			} catch (FileNotFoundException e) {
				assertTrue(false, "File not found: " + DICTIONNAIRE);
			}
		}
	}

	private static void createFile(String string) {
		File file = new File("mots/test.txt");
		try {
			file.createNewFile();
		} catch (Exception e) {
			System.out.println("Erreur lors de la création du fichier");
		}
	}

	private static void removeFile(String string) {
		File file = new File("mots/test.txt");
		if (file.exists()) {
			file.delete();
		}
	}

}
