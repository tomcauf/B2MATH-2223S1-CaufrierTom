package boggle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import tree.LexicographicTree;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BoggleTest {
	private static final Set<String> EXPECTED_WORDS = new TreeSet<>(Arrays.asList(new String[] {"ces", "cesse", "cessent", "cresson", "ego", "encre",
			"encres", "engonce", "engoncer", "engonces", "esse", "gens", "gent", "gesse", "gnose", "gosse", "nes", "net", "nos", "once",
			"onces", "ose", "osent", "pre", "pres", "presse", "pressent", "ressent", "sec", "secs", "sen", "sent", "set", "son",
			"songe", "songent", "sons", "tenson", "tensons", "tes"}));
	private static final String GRID_LETTERS = "rhreypcswnsntego";
	private static LexicographicTree dictionary = null;
	private static Boggle boggle4X4;

	@BeforeAll
	public static void initTestDictionary() {
		System.out.print("Loading dictionary...");
		dictionary = new LexicographicTree("mots/dictionnaire_FR_sans_accents.txt");
		System.out.println(" done.");
		boggle4X4 = new Boggle(4, GRID_LETTERS, dictionary);
	}
	
	@Test
	void wikipediaExample() {
		Boggle b = new Boggle(4, GRID_LETTERS, dictionary);
		assertNotNull(b);
		assertEquals(GRID_LETTERS, b.letters());
		System.out.println(b.toString());
		assertTrue(b.contains("songent"));
		assertFalse(b.contains("sono"));
		assertEquals(EXPECTED_WORDS, b.solve());
	}
	@Test
	void containsWords() {
		LexicographicTree dict = new LexicographicTree();
		dict.insertWord("aux");
		dict.insertWord("aura");
		dict.insertWord("dure");
		Boggle b = new Boggle(3, "asxduavre", dict);

		assertTrue(b.contains("aux"));
		assertTrue(b.contains("aura"));
		assertTrue(b.contains("dure"));
		
		assertFalse(b.contains("auxe"));
		assertFalse(b.contains("aurar"));
		assertFalse(b.contains("ada"));
		assertFalse(b.contains("ase"));
		assertFalse(b.contains("dures"));
		assertFalse(b.contains("asas"));
	}
	
	@Test
	void solve2x2_1() {
		LexicographicTree dict = new LexicographicTree();
		dict.insertWord("sega");
		dict.insertWord("sage");
		Boggle b = new Boggle(2, "sgea", dict);

		assertTrue(b.contains("sage"));
		assertTrue(b.contains("sega"));
		assertEquals(new TreeSet<String>(Arrays.asList("sega", "sage")), b.solve());
	}
	
	@Test
	void solve2x2_2() {
		LexicographicTree dict = new LexicographicTree();
		dict.insertWord("art");
		dict.insertWord("rate");
		Boggle b = new Boggle(2, "arte", dict);

		assertTrue(b.contains("art"));
		assertTrue(b.contains("rate"));
		
		assertFalse(b.contains("rar"));
		
		assertEquals(new TreeSet<String>(Arrays.asList("art", "rate")), b.solve());
		
	}
	
	@Test
	void solve4x4English() {
		List<String> expected = Arrays.asList("ees", "eess", "ere", "eres", "erg", "ert", "erusse", "erusser", "erustes", "esn", "ess", "est", "esu", "ets", "eue", "eues", "eur", "eure", "eus", "eusse", "eut", "eutes", "eutm", "fee", "feer", "fees", "fer", "feret", "ferets", "fert", "feru", "ferue", "ferues", "ferus", "fes", "fessu", "fessue", "fessy", "fet", "fetu", "fetus", "feu", "feue", "feues", "feur", "feure", "feurer", "feures", "feus", "feutre", "feutrer", "feutres", "fre", "free", "frere", "freres", "fressure", "fret", "frets", "freusse", "freusser", "fse", "fss", "gre", "gree", "greer", "grees", "gref", "gres", "gress", "gressy", "gru", "grue", "gruee", "gruees", "gruer", "grues", "grusse", "grust", "grute", "grutee", "gruter", "grutes", "gtr", "gtt", "gue", "guee", "gueer", "guees", "guer", "guere", "gueres", "gueret", "guerets", "gues", "guess", "guet", "guets", "guett", "gur", "gus", "guse", "guses", "guss", "gusse", "guster", "gut", "guts", "gutte", "guy", "mss", "mst", "mts", "mtu", "mys", "myste", "mystere", "mystes", "myt", "myure", "myures", "nsf", "nsu", "ree", "reer", "rees", "reest", "reet", "ref", "refre", "refs", "rer", "rerue", "rerues", "res", "ressu", "ressue", "ressuer", "ressut", "rest", "resu", "resue", "resuer", "resure", "resut", "ret", "rets", "retu", "retue", "retuer", "retus", "retut", "rety", "reu", "reus", "reuse", "reuser", "reuses", "reuss", "reusse", "reut", "reute", "reuter", "rtg", "rtt", "rue", "ruee", "ruees", "ruer", "rues", "rug", "rus", "ruse", "rusee", "ruser", "ruses", "russ", "russe", "russy", "rust", "rut", "rute", "ruts", "rutter", "ruy", "see", "ser", "sere", "serf", "serfs", "sert", "ses", "sestu", "sesue", "set", "sets", "seu", "seur", "seurer", "smts", "sns", "sse", "ssf", "sss", "ssss", "sst", "ssu", "ste", "stere", "sterer", "stert", "stes", "stm", "sts", "stuer", "sue", "suee", "suees", "suer", "sues", "suet", "suets", "sur", "sure", "sures", "suret", "surets", "sut", "sutes", "suttee", "tee", "tef", "tefs", "ter", "terf", "terfs", "tergu", "tes", "tessure", "teu", "teug", "tms", "tre", "tref", "trefe", "trefes", "trefs", "tres", "tressue", "tressuer", "trest", "trests", "trets", "treu", "trg", "tru", "truss", "trust", "truste", "trustee", "truster", "trustes", "trusts", "trut", "trute", "truter", "trutes", "tse", "tsf", "tsm", "tss", "tsss", "tsu", "tte", "ttm", "ttr", "tts", "ttu", "tue", "tuee", "tuees", "tuer", "tues", "tug", "tur", "tus", "tuss", "tusse", "tust", "tuste", "tustee", "tuster", "tustes", "tusts", "tut", "tute", "tuter", "tutes", "tuy", "tuysse", "tuyssee", "tuysser", "tuysses", "uee", "uess", "ure", "uree", "urees", "ures", "urt", "use", "usee", "user", "uses", "usn", "uss", "usse", "ussy", "uster", "ute", "utes", "utm", "utr", "uts", "utt", "ymt", "yss", "ytres", "yue", "yues", "yug", "yur", "yuste", "yut");
		
		LexicographicTree dict = new LexicographicTree();
		for (String string : expected) {
			dict.insertWord(string);
		}
		Boggle b = new Boggle(4, "mssnytsstuefgrer", dict);
		
		for (String string : expected) {
			assertTrue(b.contains(string));
		}
		
		assertEquals(new TreeSet<String>(expected), b.solve());
		
	}
	
	@Test
	void incorrectFields() {
		LexicographicTree dict = new LexicographicTree();
		assertThrows(IllegalArgumentException.class, () -> {new Boggle(3, "arte", dict);});
		assertThrows(IllegalArgumentException.class, () -> {new Boggle(0, "arte", dict);});
		assertThrows(IllegalArgumentException.class, () -> {new Boggle(0, dict);});
		assertDoesNotThrow(() -> {new Boggle(1, "a", dict);});
		new Boggle(1, dict);
		assertDoesNotThrow(() -> {new Boggle(1, dict);});
	}
	@Test
	void createBoggleWithMoreLetter(){
		String newDict = "rhreypcswnsntegosjdmqlmfdjnksdmljfghkhgcjhvcjfcfcfgsdhgsd";
		assertThrows(IllegalArgumentException.class, () -> new Boggle(4, newDict, dictionary));
	}

	@Test
	void createBoggleSpecifyingLettersWithIncorrectSize(){
		String letter = "rhreypcswnsntegomlkkdnqmlksqnmlnqdsmlnqsdlmndqlmdnlqs";
		assertThrows(IllegalArgumentException.class, () -> new Boggle(-3,letter, dictionary));
		assertThrows(IllegalArgumentException.class, () -> new Boggle(0,letter, dictionary));
	}

	@Test
	void createBoggleNoSpecifyingLettersWithIncorrectSize(){
		assertThrows(IllegalArgumentException.class, () -> new Boggle(-3, dictionary));
		assertThrows(IllegalArgumentException.class, () -> new Boggle(0, dictionary));
	}

	@Test
	void createBoggleWithNullLetters(){
		assertThrows(IllegalArgumentException.class, () -> new Boggle(4, null, dictionary));
	}

	@Test
	void createBoggleWithEmptyLetters(){
		assertThrows(IllegalArgumentException.class, () -> new Boggle(4, "", dictionary));
	}

	@Test
	void createBoggleWithNullDictionary(){
		assertThrows(IllegalArgumentException.class, () -> new Boggle(4, GRID_LETTERS, null));
		assertThrows(IllegalArgumentException.class, () -> new Boggle(4, null));
	}

	@Test
	void createBoggleWithEmptyDictionary(){
		Boggle boogleWithEmptyDict = new Boggle(4, GRID_LETTERS, new LexicographicTree());
		assertEquals(0, boogleWithEmptyDict.solve().size());
	}

	@Test
	void createBoggleWithUnauthorizedLetter(){
		assertThrows(IllegalArgumentException.class, () -> new Boggle(4, "rhreypcswnsnteégomlkkdnqmlksqnmlnqdsmlànqsdlmndq==lmdnlqs", dictionary));
	}

	@Test
	public void getLetterFromBoggleGrid(){
		String letters = boggle4X4.letters();
		String expectedLetters = "rhreypcswnsntego";
		assertEquals(expectedLetters, letters);
	}

	@Test
	public void containsUnexpectedWord(){
		assertFalse(boggle4X4.contains("hello"));
	}

	@Test
	public void containsEmptyWord(){
		assertFalse(boggle4X4.contains(""));
	}

	@Test
	public void containsNullWord(){
		assertFalse(boggle4X4.contains(null));
	}

	@Test
	public void containsWordWithSpaces(){
		assertFalse(boggle4X4.contains("sons fort"));
	}

	@Test
	public void containsWordWithSpecialCharacters(){
		assertFalse(boggle4X4.contains("sons!"));
		assertFalse(boggle4X4.contains("céder"));
		assertFalse(boggle4X4.contains("trône"));
	}

	@Test
	public void containsWordWithNumbers(){
		assertFalse(boggle4X4.contains("sons1"));
	}

	@Test
	public void containsWordWithUpperCase(){
		assertTrue(boggle4X4.contains("SONS"));
	}

	@Test
	public void containsWordWithLowerCase(){
		assertTrue(boggle4X4.contains("sons"));
	}

	@Test
	public void containsWordWithMixedCase(){
		assertTrue(boggle4X4.contains("sOnS"));
	}

	@Test
	public void containsWordWithNonAdjacentLetters(){
		assertFalse(boggle4X4.contains("sont"));
	}

	@Test
	public void containsWordWithAdjacentLetters(){
		assertTrue(boggle4X4.contains("cesse"));
		assertTrue(boggle4X4.contains("ces"));
		for(String word :EXPECTED_WORDS){
			assertTrue(boggle4X4.contains(word));
		}
	}

	@Test
	public void containsWordByUsingMultipleTimeASameVertex(){
		assertFalse(boggle4X4.contains("ses"));
	}

	//Solve

	@Test
	void solveBoggle4x4(){
		Set<String> words = boggle4X4.solve();
		assertEquals(EXPECTED_WORDS.size(), words.size());
		assertTrue(words.containsAll(EXPECTED_WORDS));
		assertTrue(EXPECTED_WORDS.containsAll(words));
	}

	@Test
	void solveBoggleNonexistentWordInDictionary() {
		Set<String> words = boggle4X4.solve();
		assertFalse(words.contains("hello"));
		assertFalse(words.contains("Anticonstitutionnellement"));
	}

	@Test
	void solveBoggleDoesNotContainsWordWithLengthLowerThan3(){
		Set<String> words = boggle4X4.solve();
		assertFalse(words.contains("ci"));
	}

	@Test
	void solveBoggleWithDifferentWaysToWriteAWord(){
		String letter = "ssssessss";
		Boggle boggle = new Boggle(3, letter, dictionary);
		Set<String> words = boggle.solve();
		assertTrue(words.contains("ses"));
		assertEquals(1, words.size());
	}

	@Test
	void solveGridWithSize1(){
		String grid = "a";
		Boggle boggle = new Boggle(1, grid, dictionary);
		Set<String> words = boggle.solve();
		assertEquals(words.size(),0);
		assertFalse(boggle.contains("a"));
	}
	@Test
	void toStringBoggle4x4(){
		String expectedString ="";
		expectedString += "+---+---+---+---+\n"
				+ "| r | h | r | e |\n"
				+ "+---+---+---+---+\n"
				+ "| y | p | c | s |\n"
				+ "+---+---+---+---+\n"
				+ "| w | n | s | n |\n"
				+ "+---+---+---+---+\n"
				+ "| t | e | g | o |\n"
				+ "+---+---+---+---+\n";
		assertEquals(expectedString, boggle4X4.toString());
	}
}
