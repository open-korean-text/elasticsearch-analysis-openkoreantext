package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.elasticsearch.common.Strings;
import org.openkoreantext.processor.OpenKoreanTextProcessor;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OpenKoreanTextTokenizer extends Tokenizer implements KoreanTokenPrepareable {

    private static final int READER_BUFFER_SIZE = 1024;

    private final static Set<String> stopTypes;

    static {
        stopTypes = new HashSet<>();
        stopTypes.add("Space");
    }

    private List<KoreanToken> preparedTokens = null;

    private final CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);

    private final OffsetAttribute offsetAttribute = addAttribute(OffsetAttribute.class);

    private final TypeAttribute typeAttribute = addAttribute(TypeAttribute.class);

    private int tokenIndex = 0;

    public OpenKoreanTextTokenizer() {
        super(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
        List<String> words = new ArrayList<>();
        OpenKoreanTextProcessor.addNounsToDictionary(JavaConverters.asScalaBuffer(words).toSeq());
    }

    public void addUserDictionary(List<String> words) {
        OpenKoreanTextProcessor.addNounsToDictionary(JavaConverters.asScalaBuffer(words).toSeq());
    }

    public void addUserDictionary(String path) throws IOException {
        File file = new File(path);
        addUserDictionary(new BufferedReader(new FileReader(file)));
    }

    public void addUserDictionary(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        addUserDictionary(new BufferedReader(new InputStreamReader(connection.getInputStream())));
    }

    public void addUserDictionary(BufferedReader bufferedReader) throws IOException {
        List<String> words = new ArrayList<>();
        String word;
        while ((word = bufferedReader.readLine()) != null) {
            if(Strings.isEmpty(word)) continue;
            words.add(word);
        }
        addUserDictionary(words);
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
        this.preparedTokens = null;
    }
}
