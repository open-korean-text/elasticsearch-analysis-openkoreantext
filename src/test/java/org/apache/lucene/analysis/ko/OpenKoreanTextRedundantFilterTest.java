package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.Tokenizer;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class OpenKoreanTextRedundantFilterTest {
    @Test
    public void testBasicUsage() throws IOException {
        String query = "그리고 이것은 예시, 또는 예로써, 한국어를 처리하기 입니다";
        String[] expectedCharTerms = new String[]{"예시", "예", "한국어", "처리", "하다", "이다"};
        String[] expectedTypes = new String[]{"Noun", "Modifier", "Noun", "Noun", "Verb", "Adjective"};
        int[] expectedStartOffsets = new int[]{8, 15, 20, 25, 27, 30};
        int[] expectedEndOffsets = new int[]{10, 16, 23, 27, 29, 33};

        Tokenizer tokenizer = new OpenKoreanTextTokenizer();
        tokenizer.setReader(new StringReader(query));

        OpenKoreanTextTokenFilter tokenFilter = new OpenKoreanTextStemmer(tokenizer);
        tokenFilter = new OpenKoreanTextRedundantFilter(tokenFilter);

        TokenStreamAssertions.assertTokenStream(tokenFilter, expectedCharTerms, expectedTypes, expectedStartOffsets, expectedEndOffsets);
    }
}