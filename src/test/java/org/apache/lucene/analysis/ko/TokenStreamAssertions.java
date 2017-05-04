package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TokenStreamAssertions {
    public static void assertTokenStream(TokenStream tokenStream, String[] expectedCharTerms, String[] expectedTypes, int[] expectedStartOffsets, int[] expectedEndOffsets) throws IOException {
        int index = 0;
        while (tokenStream.incrementToken() == true) {
            assertEquals(expectedCharTerms[index], tokenStream.getAttribute(CharTermAttribute.class).toString());
            System.out.println(tokenStream.getAttribute(CharTermAttribute.class).toString());
            assertEquals(expectedTypes[index], tokenStream.getAttribute(TypeAttribute.class).type());
            OffsetAttribute offsets = tokenStream.getAttribute(OffsetAttribute.class);
            assertEquals(expectedStartOffsets[index], offsets.startOffset());
            assertEquals(expectedEndOffsets[index], offsets.endOffset());
            index++;
        }
    }
}
