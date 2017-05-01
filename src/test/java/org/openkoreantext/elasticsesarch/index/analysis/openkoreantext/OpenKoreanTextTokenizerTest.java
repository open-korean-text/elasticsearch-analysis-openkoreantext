package org.openkoreantext.elasticsesarch.index.analysis.openkoreantext;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class OpenKoreanTextTokenizerTest {

    @Test
    public void testTokenizer() throws IOException {
        OpenKoreanTextTokenizer tokenizer = new OpenKoreanTextTokenizer();
        tokenizer.setReader(new StringReader("한국어를 처리하는 예시입니닼ㅋㅋㅋㅋㅋ"));

        tokenizer.reset();

        while (tokenizer.incrementToken() == true){
            System.out.println(tokenizer);
        }

//        while ( tokenizer.incrementToken() == true ) {
//
//        }
//        tokenizer.end();

    }
}