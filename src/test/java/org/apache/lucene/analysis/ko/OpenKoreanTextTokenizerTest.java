package org.apache.lucene.analysis.ko;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class OpenKoreanTextTokenizerTest {

    @Test
    public void testTokenizer() throws IOException {
        String testQuery = "한국어를 처리하는 예시입니다ㅋㅋ";

        OpenKoreanTextTokenizer tokenizer = new OpenKoreanTextTokenizer();
        tokenizer.setReader(new StringReader(testQuery));

        List<String> types = new ArrayList<>();

        String[] expectedCharTerms = new String[]{"한국어", "를", " ", "처리하는", " ", "예시", "입니", "다", "ㅋㅋ"};
        String[] expectedTypes = new String[]{"Noun", "Josa", "Space", "Verb", "Space", "Noun", "Adjective", "Eomi", "KoreanParticle"};
        int[] expectedStartOffsets = new int[]{0, 3, 4, 5, 9, 10, 12, 14, 15};
        int[] expectedEndOffsets = new int[]{3, 4, 5, 9, 10, 12, 14, 15, 17};

        tokenizer.reset();
        TokenStreamAssertions.assertTokenStream(tokenizer, expectedCharTerms, expectedTypes, expectedStartOffsets, expectedEndOffsets);
        tokenizer.end();
    }
}