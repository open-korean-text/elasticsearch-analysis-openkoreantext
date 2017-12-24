package org.apache.lucene.analysis.ko;

import scala.collection.Seq;

import java.io.IOException;

import static org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;

/**
 * To prepare korean token sequence.
 */
public interface KoreanTokenPrepareable {
    /**
     * To prepare all tokens before token increment.
     */
    Seq<KoreanToken> prepareKoreanTokens() throws IOException;

    KoreanToken getCurrentToken();
}
