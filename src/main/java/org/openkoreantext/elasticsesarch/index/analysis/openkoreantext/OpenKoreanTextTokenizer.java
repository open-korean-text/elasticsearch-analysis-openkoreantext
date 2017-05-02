package org.openkoreantext.elasticsesarch.index.analysis.openkoreantext;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.openkoreantext.processor.OpenKoreanTextProcessor;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;
import scala.collection.JavaConversions;
import scala.collection.Seq;

import java.io.IOException;
import java.util.List;

public class OpenKoreanTextTokenizer extends Tokenizer {
    private static final int READER_BUFFER_SIZE = 2048;

    private boolean isInputRead = false;

    private int tokenIndex = 0;

    private List<KoreanToken> tokens = null;

    private CharTermAttribute charTermAttribute = null;

    private OffsetAttribute offsetAttribute = null;

    private TypeAttribute typeAttribute = null;

    public OpenKoreanTextTokenizer() {
        super(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
        initAttributes();
    }

    @Override
    public final boolean incrementToken() throws IOException {
        clearAttributes();

        if (!this.isInputRead) {
            this.isInputRead = true;
            CharSequence text = readText();
            Seq<KoreanToken> tokens = OpenKoreanTextProcessor.tokenize(text);
            this.tokens = JavaConversions.seqAsJavaList(tokens);
        }

        if (this.tokens == null || this.tokens.isEmpty() || tokenIndex >= this.tokens.size()) {
            return false;
        }
        setAttributes(this.tokens.get(tokenIndex++));
        return true;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        initializeState();
    }

    public CharTermAttribute getCharTermAttribute(){
        return this.charTermAttribute;
    }

    public OffsetAttribute getOffsetAttribute(){
        return this.offsetAttribute;
    }

    public TypeAttribute getTypeAttribute(){
        return this.typeAttribute;
    }

    private void initAttributes() {
        this.charTermAttribute = addAttribute(CharTermAttribute.class);
        this.offsetAttribute = addAttribute(OffsetAttribute.class);
        this.typeAttribute = addAttribute(TypeAttribute.class);
    }

    private void setAttributes(KoreanToken token) {
        charTermAttribute.append(token.text());
        offsetAttribute.setOffset(token.offset(), token.offset() + token.length());
        typeAttribute.setType(token.pos().toString());
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

    private void initializeState() {
        this.isInputRead = false;
        this.tokenIndex = 0;
        this.tokens = null;
    }
}
