package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;

import java.io.Reader;
import java.util.Arrays;
import java.util.List;

/**
 * A default korean analyzer.
 */
public class OpenKoreanTextAnalyzer extends StopwordAnalyzerBase {

    private final static CharArraySet STOP_WORD_SET;
    // Max token length is from https://ko.wikipedia.org/wiki/%EA%B8%B4_%ED%95%9C%EA%B5%AD%EC%96%B4_%EB%82%B1%EB%A7%90
    private final static int MAX_TOKEN_LENGTH = 13;

    static {
        List<String> stopWords = Arrays.asList(
                "a", "an", "and", "are", "as", "at", "be", "but", "by",
                "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the",
                "their", "then", "there", "these", "they", "this", "to", "was", "will", "with");

        STOP_WORD_SET = CharArraySet.unmodifiableSet(new CharArraySet(stopWords.size(), false));
    }

    public OpenKoreanTextAnalyzer() {
        super(STOP_WORD_SET);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new OpenKoreanTextTokenizer();

        TokenStream tokenStream = new OpenKoreanTextStemmer(tokenizer);
        tokenStream = new OpenKoreanTextRedundantFilter(tokenStream);
        tokenStream = new ClassicFilter(tokenStream);
        tokenStream = new LengthFilter(tokenStream, 0, MAX_TOKEN_LENGTH);
        tokenStream = new LowerCaseFilter(tokenStream);

        return new TokenStreamComponents(tokenizer, tokenStream);
    }

    @Override
    protected Reader initReader(String fieldName, Reader reader) {
        return new OpenKoreanTextNormalizer(super.initReader(fieldName, reader));
    }
}
