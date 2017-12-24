package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.TokenStream;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.Arrays;

import static org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;

/**
 * Stems Adjectives and Verbs tokens.
 */
public final class OpenKoreanTextStemmer extends OpenKoreanTextTokenFilter {

    public OpenKoreanTextStemmer(TokenStream input) {
        super(input);
    }

    @Override
    protected Seq<KoreanToken> perform(Seq<KoreanToken> tokens) {
        KoreanToken[] performed = new KoreanToken[tokens.length()];

        int i = 0;
        Iterator<KoreanToken> tokenIterator =  tokens.iterator();

        while (tokenIterator.hasNext()) {
            KoreanToken token = tokenIterator.next();
            performed[i++] = token.stem().nonEmpty() ? stem(token) : token;
        }

        return JavaConverters.asScalaBuffer(Arrays.asList(performed)).toSeq();
    }

    private KoreanToken stem(KoreanToken token) {
        return new KoreanToken(token.stem().get(), token.pos(), token.offset(), token.length(), scala.Option.apply(null), false);
    }
}
