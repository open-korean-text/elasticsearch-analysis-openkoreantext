package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.TokenStream;
import org.openkoreantext.processor.OpenKoreanTextProcessor;
import scala.collection.Seq;

import static org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;

public final class OpenKoreanTextStemmer extends OpenKoreanTextTokenFilter {

    public OpenKoreanTextStemmer(TokenStream input) {
        super(input);
    }

    @Override
    protected Seq<KoreanToken> perform(Seq<KoreanToken> tokens) {
        return OpenKoreanTextProcessor.stem(tokens);
    }
}
