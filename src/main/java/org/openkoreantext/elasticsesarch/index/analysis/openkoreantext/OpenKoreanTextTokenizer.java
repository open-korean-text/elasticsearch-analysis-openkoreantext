package org.openkoreantext.elasticsesarch.index.analysis.openkoreantext;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.openkoreantext.processor.KoreanPosJava;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessor;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor.KoreanPhrase;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;
import scala.collection.JavaConversions;
import scala.collection.Seq;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class OpenKoreanTextTokenizer extends Tokenizer {
    /** whether input stream was read as a string. */
    private boolean isInputRead = false;

    /** current index of token buffers. */
    private int tokenIndex = 0;

    /** token buffers. */
    List<KoreanTokenJava> tokenBuffer = null;

    /** whether to normalize text before tokenization. */
    private boolean enableNormalize = true;

    /** whether to stem text before tokenization. */
    private boolean enableStemmer = true;

    /** whtere to enable phrase parsing. */
    private boolean enablePhrase = false;

    private CharTermAttribute charTermAttribute = null;
    private OffsetAttribute offsetAttribute = null;
    private TypeAttribute typeAttribute = null;

    public OpenKoreanTextTokenizer() {
        this(true, true, false);
    }

    public OpenKoreanTextTokenizer(boolean enableNormalize, boolean enableStemmer, boolean enablePhrase) {
        super(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);

        this.enableNormalize = enableNormalize;
        this.enableStemmer = enableStemmer;
        this.enablePhrase = enablePhrase;

        initAttributes();
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.analysis.TokenStream#incrementToken()
     */
    @Override
    public final boolean incrementToken() throws IOException {
        clearAttributes();

        if (this.isInputRead == false) {
            System.out.println("inc: " + this.enableNormalize);
            System.out.println("inc: " + this.enableStemmer);


            this.isInputRead = true;
            CharSequence text = readText();
            Seq<KoreanToken> tokens = OpenKoreanTextProcessor.tokenize(text);

            if ( this.enableStemmer ) {
                tokens  = OpenKoreanTextProcessor.stem(tokens);
            }

            if ( this.enablePhrase ) {
                Seq<KoreanPhrase> phraseSeq = OpenKoreanTextProcessor.extractPhrases(tokens, true, true);

                this.tokenBuffer = new LinkedList<KoreanTokenJava>();

                for(KoreanPhrase phrase : JavaConversions.seqAsJavaList(phraseSeq)) {
                    this.tokenBuffer.add(new KoreanTokenJava(phrase.text(), KoreanPosJava.valueOf(phrase.pos().toString()), phrase.offset(), phrase.length(), false));
                }
            } else {
                this.tokenBuffer = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);
            }
        }

        if (this.tokenBuffer == null || this.tokenBuffer.isEmpty() || tokenIndex >= this.tokenBuffer.size()) {
            return false;
        }

        setAttributes(this.tokenBuffer.get(tokenIndex++));

        return true;
    }

    /**
     * Add attributes
     *
     */
    private void initAttributes() {
        this.charTermAttribute = addAttribute(CharTermAttribute.class);
        this.offsetAttribute = addAttribute(OffsetAttribute.class);
        this.typeAttribute = addAttribute(TypeAttribute.class);
    }

    /**
     * Set attributes
     *
     * @param token
     */
    private void setAttributes(KoreanTokenJava token) {
        charTermAttribute.append(token.getText());
        offsetAttribute.setOffset(token.getOffset(), token.getOffset() + token.getLength());
        typeAttribute.setType(token.getPos().toString());

    }

    /**
     * Read string from input reader.
     *
     * @return
     * @throws IOException
     */
    private CharSequence readText() throws IOException {
        StringBuilder text = new StringBuilder();
        char[] tmp = new char[1024];
        int len = -1;
        while ((len = input.read(tmp)) != -1) {
            text.append(new String(tmp, 0, len));
        }

        if ( this.enableNormalize ) {
            return OpenKoreanTextProcessor.normalize(text.toString());
        } else {
            return text.toString();
        }

    }

    /**
     * Initailze states.
     */
    private void initializeState() {
        this.isInputRead = false;
        this.tokenIndex = 0;
        this.tokenBuffer = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.lucene.analysis.Tokenizer#close()
     */
    @Override
    public void close() throws IOException {
        super.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.lucene.analysis.Tokenizer#reset()
     */
    @Override
    public void reset() throws IOException {
        super.reset();

        initializeState();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.lucene.analysis.TokenStream#end()
     */
    @Override
    public void end() throws IOException {
        super.end();
    }
}
