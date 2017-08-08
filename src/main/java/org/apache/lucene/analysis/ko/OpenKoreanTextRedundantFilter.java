package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.TokenStream;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.*;

import static org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;

/**
 * Remove redundant type and term tokens.
 */
public class OpenKoreanTextRedundantFilter extends OpenKoreanTextTokenFilter {

    private final static Set<String> redundantTypes;

    private final static Set<String> redundantTerms;

    static {
        String[] types = new String[]{"Space", "Conjunction", "Josa", "Eomi", "PreEomi", "Punctuation"};
        redundantTypes = new HashSet<>();
        for(String redundant : types) {
            redundantTypes.add(redundant);
        }

        redundantTerms = new HashSet<>();
        String[] terms = new String[]{"이", "그", "저", "요", "것", "수", "등", "들", "및", "에", "에서", "또", "또는", "또한", "꼭", "잘", "로서", "로써"};
        for(String redundant : terms) {
            redundantTerms.add(redundant);
        }
    }


    public OpenKoreanTextRedundantFilter(TokenStream input) {
        super(input);
    }

    @Override
    protected Seq<KoreanToken> perform(Seq<KoreanToken> tokens) {
        List<KoreanToken> performed = new ArrayList<>();
        for(KoreanToken token : JavaConverters.seqAsJavaList(tokens)) {
            if(redundantTypes.contains(token.pos().toString())){
               continue;
            }
            if(redundantTerms.contains(token.text())){
                continue;
            }
            performed.add(token);
        }

        return JavaConverters.asScalaBuffer(performed).toSeq();
    }
}
