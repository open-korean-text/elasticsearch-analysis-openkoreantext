package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.Tokenizer;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class OpenKoreanTextStemmerTest  {

    @Test
    public void testBasicUsage() throws IOException {
        String query = "한국어를 처리하는 예시입니다ㅋㅋ";
        String[] expectedCharTerms = new String[]{"한국어", "를", " ", "처리", "하다", " ", "예시", "이다", "ㅋㅋ"};
        String[] expectedTypes = new String[]{"Noun", "Josa", "Space", "Noun", "Verb", "Space", "Noun", "Adjective", "KoreanParticle"};
        int[] expectedStartOffsets = new int[]{0, 3, 4, 5, 7, 9, 10, 12, 15};
        int[] expectedEndOffsets = new int[]{3, 4, 5, 7, 9, 10, 12, 15, 17};

        Tokenizer tokenizer = new OpenKoreanTextTokenizer();
        tokenizer.setReader(new StringReader(query));

        OpenKoreanTextTokenFilter tokenFilter = new OpenKoreanTextStemmer(tokenizer);

        TokenStreamAssertions.assertTokenStream(tokenFilter, expectedCharTerms, expectedTypes, expectedStartOffsets, expectedEndOffsets);
    }
}