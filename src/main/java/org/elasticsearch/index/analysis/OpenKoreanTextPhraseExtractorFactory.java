package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.OpenKoreanTextPhraseExtractor;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

/**
 * A ES token filter factory for {@link OpenKoreanTextPhraseExtractor}.
 */
public class OpenKoreanTextPhraseExtractorFactory extends AbstractTokenFilterFactory {

    public OpenKoreanTextPhraseExtractorFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new OpenKoreanTextPhraseExtractor(tokenStream);
    }
}
