package steno;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Keyer {
    public static final int MAX_RANK_BEFORE_SPELLING = 2;
    private final ArpabetCompressor compressor;

    public Keyer(ArpabetCompressor compressor) {
        this.compressor = compressor;
    }

    public PerformanceStats scoreDictionaryWithoutContext() {
        final PerformanceStats performanceStats = new PerformanceStats();
        final Object[] entries = WordFrequency.DICTIONARY.entrySet().toArray();
        for (int i = 0; i < entries.length; i++) {
            if (i % 100 == 0) {
                System.out.println(String.valueOf(i / (double) entries.length));
            }
            Map.Entry<String, Double> wordAndFrequency = (Map.Entry<String, Double>) entries[i];
            final String word = wordAndFrequency.getKey();
            final int rank = predictionRankAfterEncodingAndDecoding(word, ImmutableList.of());
            performanceStats.add(rank, wordAndFrequency.getValue(), word);
        }
        return performanceStats;
    }

    public PerformanceStats scoreText(String text) {
        final PerformanceStats performanceStats = new PerformanceStats();
        final List<String> words = Arrays.asList(text.replaceAll("[^a-zA-Z \n]", "").toLowerCase().split("\\s+"));
        for (int i = 0; i < words.size(); i++) {
            final String word = words.get(i);
            final List<String> context = words.subList(Math.max(0, i - 4), i);
            final int rank = predictionRankAfterEncodingAndDecoding(word, context);
            performanceStats.add(rank, 1, word);
        }
        return performanceStats;
    }

    private int predictionRankAfterEncodingAndDecoding(String word, List<String> context) {
        final List<Arpabet> trueArpabets = Arpabet.fromWord(word);
        if (trueArpabets == null) {
            return word.length(); //TODO
        }
        final List<Enum> compressed = compressor.encode(trueArpabets);
        final Set<String> possibleWords = compressor.decode(compressed);
        final List<String> rankedWordsByLikelihood = NextWordPredictor.sortByLikelihoodDescending(possibleWords, context);
        final int rank = rankedWordsByLikelihood.indexOf(word);
        return rank >= 0 && rank <= MAX_RANK_BEFORE_SPELLING ? rank : word.length(); //TODO
    }
}
