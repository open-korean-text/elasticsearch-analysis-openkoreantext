package org.openkoreantext.elasticsesarch.index.analysis.openkoreantext;

import org.apache.lucene.analysis.charfilter.BaseCharFilter;
import org.openkoreantext.processor.OpenKoreanTextProcessor;

import java.io.IOException;
import java.io.Reader;

public class OpenKoreanTextNormalizer extends BaseCharFilter {
    private static final int READER_BUFFER_SIZE = 2048;

    private boolean isInputRead;
    private char[] inputText;
    private int cursor;

    public OpenKoreanTextNormalizer(Reader in) {
        super(in);
        initAttrs();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (off < 0) throw new IllegalArgumentException("off < 0");
        if (off >= cbuf.length) throw new IllegalArgumentException("off >= cbuf.length");
        if (len <= 0) throw new IllegalArgumentException("len <= 0");

        if (!this.isInputRead) {
            startAttrs();
        }

        int copyLen = this.inputText.length - cursor;
        if(copyLen < 1){
            initAttrs();
            return -1;
        }

        copyLen = copyLen > len ? len : copyLen;
        System.arraycopy(inputText, cursor, cbuf, off, copyLen);
        cursor += copyLen;
        return copyLen;
    }

    private void initAttrs(){
        this.isInputRead = false;
        this.inputText = null;
        this.cursor = -1;
    }

    private void startAttrs() throws IOException {
        this.isInputRead = true;
        this.inputText = normalizeInput().toCharArray();
        this.cursor = 0;
    }

    private String normalizeInput() throws IOException {
        StringBuilder text = new StringBuilder();
        char[] tmp = new char[READER_BUFFER_SIZE];
        int len = -1;
        while ((len = input.read(tmp)) != -1) {
            text.append(new String(tmp, 0, len));
        }
        return OpenKoreanTextProcessor.normalize(text).toString();
    }
}
