package org.apache.lucene.analysis.ko;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OpenKoreanTextTokenizerTest {

    @Test
    public void testTokenizer() throws IOException {
        String text = "한국어를 처리하는 예시입니다ㅋㅋ";

        OpenKoreanTextTokenizer tokenizer = new OpenKoreanTextTokenizer();
        tokenizer.setReader(new StringReader(text));

        String[] expectedCharTerms = new String[]{"한국어", "를", " ", "처리", "하는", " ", "예시", "입니다", "ㅋㅋ"};
        String[] expectedTypes = new String[]{"Noun", "Josa", "Space", "Noun", "Verb", "Space", "Noun", "Adjective", "KoreanParticle"};
        int[] expectedStartOffsets = new int[]{0, 3, 4, 5, 7, 9, 10, 12, 15};
        int[] expectedEndOffsets = new int[]{3, 4, 5, 7, 9, 10, 12, 15, 17};

        TokenStreamAssertions.assertTokenStream(tokenizer, expectedCharTerms, expectedTypes, expectedStartOffsets, expectedEndOffsets);
    }

    @Test
    public void testAddNounsToDictionary() throws IOException {
        String text = "뷁충정식은 맛있다";

        OpenKoreanTextTokenizer tokenizer = new OpenKoreanTextTokenizer();
        tokenizer.setReader(new StringReader(text));

        String[] expected = new String[]{"뷁충정식", "은", " ", "맛있다"};

        List<String> userDictionary = new ArrayList<>();
        userDictionary.add("뷁충정식");

        UserDictionaryLoader.addUserDictionary(userDictionary);

        tokenizer.setReader(new StringReader(text));

        TokenStreamAssertions.assertTokenStream(tokenizer, expected, null, null, null);
    }

    @Test
    public void testUserDictionaryFromFile() throws IOException {
        String text = "퀠후푸룩커피는 맛있다";
        String[] expected = new String[]{"퀠후푸룩커피", "는", " ", "맛있다"};

        OpenKoreanTextTokenizer tokenizer = new OpenKoreanTextTokenizer();

        File dic = new File(getClass().getClassLoader().getResource("dictionary").getFile());
        String path = dic.getAbsolutePath();
        UserDictionaryLoader.addUserDictionary(new File(path));

        tokenizer.setReader(new StringReader(text));
        TokenStreamAssertions.assertTokenStream(tokenizer, expected, null, null, null);
    }

    @Test
    public void testUserDictionaryFromURL() throws IOException {
        String text = "안비빈비빔밥은 맛있다";
        String[] expected = new String[]{"안비빈비빔밥", "은", " ", "맛있다"};

        OpenKoreanTextTokenizer tokenizer = new OpenKoreanTextTokenizer();

        URL url = new URL("https://raw.githubusercontent.com/open-korean-text/elasticsearch-analysis-openkoreantext/master/src/test/resources/httpdictionary");
        UserDictionaryLoader.addUserDictionary(url);

        tokenizer.setReader(new StringReader(text));

        TokenStreamAssertions.assertTokenStream(tokenizer, expected, null, null, null);
    }
}