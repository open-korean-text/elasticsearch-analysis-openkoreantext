package org.openkoreantext.elasticsesarch.index.analysis.openkoreantext;

import org.junit.Assert;
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
        Integer[] expectedStartOffsets = new Integer[]{0, 3, 4, 5, 9, 10, 12, 14, 15};
        Integer[] expectedEndOffsets = new Integer[]{3, 4, 5, 9, 10, 12, 14, 15, 17};

        List<String> actualCharTerms = new ArrayList<>();
        List<String> actualTypes = new ArrayList<>();
        List<Integer> actualStartOffsets = new ArrayList<>();
        List<Integer> actualEndOffsets = new ArrayList<>();

        tokenizer.reset();
        while (tokenizer.incrementToken() == true){
            actualCharTerms.add(tokenizer.getCharTermAttribute().toString());
            actualTypes.add(tokenizer.getTypeAttribute().type());
            actualStartOffsets.add(tokenizer.getOffsetAttribute().startOffset());
            actualEndOffsets.add(tokenizer.getOffsetAttribute().endOffset());
        }
        tokenizer.end();

        Assert.assertArrayEquals(expectedCharTerms, actualCharTerms.toArray());
        Assert.assertArrayEquals(expectedTypes, actualTypes.toArray());
        Assert.assertArrayEquals(expectedStartOffsets, actualStartOffsets.toArray());
        Assert.assertArrayEquals(expectedEndOffsets, actualEndOffsets.toArray());
    }
}