package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.io.IOException;
import java.util.List;

import static org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;

/**
 * Abstract token filter for processing korean tokens.
 */
public abstract class OpenKoreanTextTokenFilter extends TokenFilter implements KoreanTokenPrepareable {

    private final CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAttribute = addAttribute(OffsetAttribute.class);
    private final TypeAttribute typeAttribute = addAttribute(TypeAttribute.class);
    private KoreanToken currentToken;

    protected int tokenIndex = 0;
    protected List<KoreanToken> preparedTokens = null;

    public OpenKoreanTextTokenFilter(TokenStream input) {
        super(input);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        clearAttributes();

        if(input instanceof KoreanTokenPrepareable) {
            if(preparedTokens == null) {
                this.preparedTokens = JavaConverters.seqAsJavaList(prepareKoreanTokens());
            }

            if (this.preparedTokens == null || this.preparedTokens.isEmpty() || tokenIndex >= this.preparedTokens.size()) {
                return false;
            }

            setAttributes(this.preparedTokens.get(tokenIndex++));
            return true;
        } else {
            return input.incrementToken();
        }
    }

    @Override
    public Seq<KoreanToken> prepareKoreanTokens() throws IOException {
        return perform(((KoreanTokenPrepareable) input).prepareKoreanTokens());
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        initializeState();
    }

    @Override
    public KoreanToken getCurrentToken() {
        return this.currentToken;
    }

    protected abstract Seq<KoreanToken> perform(Seq<KoreanToken> tokens);

    private void setAttributes(KoreanToken token) {
        charTermAttribute.append(token.text());
        offsetAttribute.setOffset(token.offset(), token.offset() + token.length());
        typeAttribute.setType(token.pos().toString());
        this.currentToken = token;
    }

    private void initializeState() {
        this.tokenIndex = 0;
        this.preparedTokens = null;
        this.currentToken = null;
    }
}
