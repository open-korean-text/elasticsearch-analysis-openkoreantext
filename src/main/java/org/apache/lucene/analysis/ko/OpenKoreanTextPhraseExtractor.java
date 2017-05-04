package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.TokenStream;
import org.openkoreantext.processor.OpenKoreanTextProcessor;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.List;
import java.util.stream.Collectors;

import static org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor.KoreanPhrase;
import static org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;

public class OpenKoreanTextPhraseExtractor extends OpenKoreanTextTokenFilter {

    public OpenKoreanTextPhraseExtractor(TokenStream input) {
        super(input);
    }

    @Override
    protected Seq<KoreanToken> perform(Seq<KoreanToken> tokens) {
        Seq<KoreanPhrase> phrases = OpenKoreanTextProcessor.extractPhrases(tokens, false, true);
        return convertPhrasesToTokens(phrases);
    }

    private Seq<KoreanToken> convertPhrasesToTokens(Seq<KoreanPhrase> phrases) {
        List<KoreanToken> tokenList = JavaConverters.seqAsJavaList(phrases).stream().map(phrase -> new KoreanToken(phrase.text(), phrase.pos(), phrase.offset(), phrase.length(), false)).collect(Collectors.toList());
        return JavaConverters.asScalaBuffer(tokenList).toSeq();
    }
}
