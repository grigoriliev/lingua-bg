package eu.ideya.lingua.bg.core.util;

import java.text.BreakIterator;
import java.text.Normalizer;
import java.util.Optional;
import java.util.OptionalInt;

public final class StressUtils {
	private static final int ACUTE_ACCENT = 0x0301;
	private static final int GRAVE_ACCENT = 0x0300;

	/** Precomposed Bulgarian stressed letters */
	private static final int UPPER_PRECOMPOSED = 0x040D; // Ѝ
	private static final int LOWER_PRECOMPOSED = 0x045D; // ѝ

	private StressUtils() {}

	/**
	 * Detects if a word contains any stress mark.
	 */
	public static boolean containsStress(String word) {
		return normalize(word).codePoints().anyMatch(StressUtils::isAccent);
	}

	/** Counts stress marks. */
	public static int stressCount(String word) {
		return (int) normalize(word).codePoints().filter(StressUtils::isAccent).count();
	}

	/**
	 * Finds the index of the first stressed letter in the specified word.
	 */
	public static OptionalInt findStressedLetterIndex(String word) {
		if (word == null || word.isEmpty()) return OptionalInt.empty();

		// If short form of a third-person singular feminine possessive pronoun.
		// (not a letter with stress)
		if ("ѝ".equalsIgnoreCase(word)) {
			return OptionalInt.of(0);
		}

		// Use BreakIterator to iterate over grapheme clusters
		final BreakIterator it = BreakIterator.getCharacterInstance();
		it.setText(word);

		int start = it.first();
		while (start != BreakIterator.DONE) {
			int end = it.next();
			if (end == BreakIterator.DONE) break;

			final String grapheme = word.substring(start, end);

			if (containsStress(grapheme)) {
				return OptionalInt.of(start);
			}

			start = end;
		}

		return OptionalInt.empty();
	}

	/**
	 * Finds the first stressed letter in the specified word.
	 */
	public static Optional<String> findStressedLetter(String word) {
		return Optional.of(findStressedLetterIndex(word).orElse(-1))
			.filter(idx -> idx != -1)
			.map(idx -> word.substring(idx, idx + 1));
	}

	public static boolean hasExactlyOneStress(String word) {
		return stressCount(word) == 1;
	}

	public static boolean isAccent(int codePoint) {
		return codePoint == ACUTE_ACCENT || codePoint == GRAVE_ACCENT ||
			codePoint == UPPER_PRECOMPOSED || codePoint == LOWER_PRECOMPOSED;
	}

	public static boolean containsBulgarianPrecomposedStress(String s) {
		return s.codePoints().anyMatch(
			cp -> cp == LOWER_PRECOMPOSED || cp == UPPER_PRECOMPOSED
		);
	}

	private static String normalize(String text) {
		return Normalizer.normalize(text, Normalizer.Form.NFD);
	}
}
