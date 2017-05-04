package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.openkoreantext.processor.OpenKoreanTextProcessor;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.io.IOException;
import java.util.List;

public class OpenKoreanTextTokenizer extends Tokenizer {
    private static final int READER_BUFFER_SIZE = 1024;

    private Seq<KoreanToken> tokens = null;

    private List<KoreanToken> tokensForInc = null;

    private final CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);

    private final OffsetAttribute offsetAttribute = addAttribute(OffsetAttribute.class);

    private final TypeAttribute typeAttribute = addAttribute(TypeAttribute.class);

    private int tokenIndex = 0;

    public OpenKoreanTextTokenizer() {
        super(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        clearAttributes();

        if (this.tokensForInc == null) {
            prepareTokens();
            this.tokensForInc = JavaConverters.seqAsJavaList(tokens);
        }

        if (this.tokensForInc == null || this.tokensForInc.isEmpty() || tokenIndex >= this.tokensForInc.size()) {
            return false;
        }

        setAttributes(this.tokensForInc.get(tokenIndex++));
        return true;
    }

    public void prepareTokens() throws IOException {
        CharSequence text = readText();
        this.tokens = OpenKoreanTextProcessor.tokenize(text);
    }

    public Seq<KoreanToken> getTokens(){
        return this.tokens;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        initializeState();
    }

    private CharSequence readText() throws IOException {
        StringBuilder text = new StringBuilder();
        char[] tmp = new char[READER_BUFFER_SIZE];
        int len = -1;
        while ((len = input.read(tmp)) != -1) {
            text.append(new String(tmp, 0, len));
        }
        return text.toString();
    }

    private void setAttributes(KoreanToken token) {
        charTermAttribute.append(token.text());
        offsetAttribute.setOffset(token.offset(), token.offset() + token.length());
        typeAttribute.setType(token.pos().toString());
    }

    private void initializeState() {
        this.tokenIndex = 0;
        this.tokens = null;
        this.tokensForInc = null;
    }
}
