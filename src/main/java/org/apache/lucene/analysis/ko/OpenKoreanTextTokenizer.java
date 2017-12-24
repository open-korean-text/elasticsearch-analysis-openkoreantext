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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides Korean tokenization.
 */
public class OpenKoreanTextTokenizer extends Tokenizer implements KoreanTokenPrepareable {

    private static final int READER_BUFFER_SIZE = 1024;

    private final static Set<String> stopTypes;

    static {
        stopTypes = new HashSet<>();
        stopTypes.add("Space");
    }

    private List<KoreanToken> preparedTokens = null;

    private KoreanToken currentToken = null;

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

        if (this.preparedTokens == null) {
            this.preparedTokens = JavaConverters.seqAsJavaList(prepareKoreanTokens());
        }

        if (this.preparedTokens == null || this.preparedTokens.isEmpty() || tokenIndex >= this.preparedTokens.size()) {
            return false;
        }

        setAttributes(this.preparedTokens.get(tokenIndex++));
        return true;
    }

    @Override
    public Seq<KoreanToken> prepareKoreanTokens() throws IOException {
        CharSequence text = readText();
        return OpenKoreanTextProcessor.tokenize(text);
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        initializeState();
    }

    @Override
    public KoreanToken getCurrentToken(){
        return this.currentToken;
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
        this.currentToken = token;
    }

    private void initializeState() {
        this.tokenIndex = 0;
        this.preparedTokens = null;
        this.currentToken = null;
    }
}
