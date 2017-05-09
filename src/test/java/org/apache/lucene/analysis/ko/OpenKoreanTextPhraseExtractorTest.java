package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.Tokenizer;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class OpenKoreanTextPhraseExtractorTest {

    @Test
    public void testBasicUsage() throws IOException {
        String query = "한국어를 처리하는 예시입니다ㅋㅋ #한국어";

        String[] expectedCharTerms = new String[]{"한국어", "처리", "처리하는 예시", "예시", "#한국어"};
        String[] expectedTypes = new String[]{"Noun", "Noun", "Noun", "Noun", "Hashtag"};
        int[] expectedStartOffsets = new int[]{0, 5, 5, 10, 18};
        int[] expectedEndOffsets = new int[]{3, 7, 12, 12, 22};

        Tokenizer tokenizer = new OpenKoreanTextTokenizer();
        tokenizer.setReader(new StringReader(query));

        OpenKoreanTextTokenFilter tokenFilter = new OpenKoreanTextPhraseExtractor(tokenizer);
        TokenStreamAssertions.assertTokenStream(tokenFilter, expectedCharTerms, expectedTypes, expectedStartOffsets, expectedEndOffsets);
    }
}