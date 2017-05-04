package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class OpenKoreanTextRedundantFilterTest {
    @Test
    public void testBasicUsage() throws IOException {
        String query = "그리고 이 것은 예시, 또는 예로써, 한국어를 처리하기 입니다";
        String[] expectedCharTerms = new String[]{"한국어", "를", " ", "처리", "하다", " ", "예시", "이다", "ㅋㅋ"};
        String[] expectedTypes = new String[]{"Noun", "Josa", "Space", "Noun", "Verb", "Space", "Noun", "Adjective", "KoreanParticle"};
        int[] expectedStartOffsets = new int[]{0, 3, 4, 5, 7, 9, 10, 12, 15};
        int[] expectedEndOffsets = new int[]{3, 4, 5, 7, 9, 10, 12, 15, 17};

        Tokenizer tokenizer = new OpenKoreanTextTokenizer();
        tokenizer.setReader(new StringReader(query));

        TokenFilter tokenFilter = new OpenKoreanTextStemmer(tokenizer);
        tokenFilter = new OpenKoreanTextRedundantFilter(tokenFilter);

        tokenFilter.reset();
        while (tokenFilter.incrementToken()) {
            System.out.println("=----=");
            System.out.println(tokenFilter.getAttribute(CharTermAttribute.class).toString());
            System.out.println(tokenFilter.getAttribute(TypeAttribute.class).type());
        }
        tokenFilter.end();
    }
}