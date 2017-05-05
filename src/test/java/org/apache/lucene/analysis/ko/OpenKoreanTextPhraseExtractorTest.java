package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class OpenKoreanTextPhraseExtractorTest {

    @Test
    @Ignore
    public void testBasicUsage() throws IOException {
        String query = "한국어를 처리하는 예시입니다ㅋㅋ #한국어";

        Tokenizer tokenizer = new OpenKoreanTextTokenizer();
        tokenizer.setReader(new StringReader(query));

        TokenFilter tokenFilter = new OpenKoreanTextPhraseExtractor(tokenizer);
        tokenFilter.reset();
        // Check test
        tokenFilter.end();
    }
}