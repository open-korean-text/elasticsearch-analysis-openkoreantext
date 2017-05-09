package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.CharFilter;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class OpenKoreanTextNormalizerTest {
    @Test
    public void testNormalizerCharFilter() throws Exception {
        String query = "한국어를 처리하는 예시입니닼ㅋ. 오픈코리안텍스틓ㅎㅎㅎㅎㅎㅎㅎ";
        String expected = "한국어를 처리하는 예시입니다ㅋ. 오픈코리안텍스트ㅎㅎㅎ";

        CharFilter inputReader = new OpenKoreanTextNormalizer(new StringReader(query));

        char[] tempBuff = new char[10];
        StringBuilder actual = new StringBuilder();

        while (true) {
            int length = inputReader.read(tempBuff);
            if (length == -1) break;
            actual.append(tempBuff, 0, length);
        }

        Assert.assertEquals(expected, actual.toString());
    }
}