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

        String[] expectedCharTerms = new String[]{"한국어", "를", " ", "처리하는", " ", "예시", "입니", "다", "ㅋㅋ"};
        String[] expectedTypes = new String[]{"Noun", "Josa", "Space", "Verb", "Space", "Noun", "Adjective", "Eomi", "KoreanParticle"};
        int[] expectedStartOffsets = new int[]{0, 3, 4, 5, 9, 10, 12, 14, 15};
        int[] expectedEndOffsets = new int[]{3, 4, 5, 9, 10, 12, 14, 15, 17};

        tokenizer.reset();
        TokenStreamAssertions.assertTokenStream(tokenizer, expectedCharTerms, expectedTypes, expectedStartOffsets, expectedEndOffsets);
        tokenizer.end();
    }

    @Test
    public void testAddNounsToDictionary() throws IOException {
        String text = "브루클린버거는 대박맛집이다";

        OpenKoreanTextTokenizer tokenizer = new OpenKoreanTextTokenizer();
        tokenizer.setReader(new StringReader(text));

        String[] before = new String[]{"브루클린", "버거", "는", " ", "대박", "맛집", "이다"};
        String[] after = new String[]{"브루클린버거", "는", " ", "대박맛집", "이다"};

        tokenizer.reset();
        TokenStreamAssertions.assertTokenStream(tokenizer, before, null, null, null);
        tokenizer.end();
        tokenizer.close();

        List<String> userDictionary = new ArrayList<>();
        userDictionary.add("브루클린버거");
        userDictionary.add("대박맛집");
        tokenizer.addUserDictionary(userDictionary);

        tokenizer.setReader(new StringReader(text));
        tokenizer.reset();
        TokenStreamAssertions.assertTokenStream(tokenizer, after, null, null, null);
        tokenizer.end();
    }

    @Test
    public void testUserDictionaryFromFile() throws IOException {
        String text = "브루클린버거는 대박맛집이다";
        String[] expected = new String[]{"브루클린버거", "는", " ", "대박맛집", "이다"};

        OpenKoreanTextTokenizer tokenizer = new OpenKoreanTextTokenizer();

        File dic = new File(getClass().getClassLoader().getResource("dictionary").getFile());
        String path = dic.getAbsolutePath();
        tokenizer.addUserDictionary(path);

        tokenizer.setReader(new StringReader(text));
        tokenizer.reset();
        TokenStreamAssertions.assertTokenStream(tokenizer, expected, null, null, null);
        tokenizer.end();
    }

    @Test
    public void testUserDictionaryFromURL() throws IOException {
        String text = "브루클린버거는 대박맛집이다";
        String[] expected = new String[]{"브루클린버거", "는", " ", "대박맛집", "이다"};

        OpenKoreanTextTokenizer tokenizer = new OpenKoreanTextTokenizer();

        URL url = new URL("https://raw.githubusercontent.com/open-korean-text/elasticsearch-analysis-openkoreantext/master/src/test/resources/dictionary");
        tokenizer.addUserDictionary(url);

        tokenizer.setReader(new StringReader(text));
        tokenizer.reset();
        TokenStreamAssertions.assertTokenStream(tokenizer, expected, null, null, null);
        tokenizer.end();
    }
}