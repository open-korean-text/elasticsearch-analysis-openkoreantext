package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.OpenKoreanTextStemmer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

/**
 * A ES token filter factory for {@link OpenKoreanTextStemmer}.
 */
public class OpenKoreanTextStemmerFactory extends AbstractTokenFilterFactory {

    public OpenKoreanTextStemmerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new OpenKoreanTextStemmer(tokenStream);
    }
}
