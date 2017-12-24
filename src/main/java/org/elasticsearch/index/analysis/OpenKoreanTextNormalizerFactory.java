package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.apache.lucene.analysis.ko.OpenKoreanTextNormalizer;
import org.apache.lucene.analysis.ko.OpenKoreanTextStemmer;

import java.io.Reader;

/**
 * A ES character-filter factory for {@link OpenKoreanTextNormalizer}.
 */
public class OpenKoreanTextNormalizerFactory extends AbstractCharFilterFactory implements MultiTermAwareComponent {

    public OpenKoreanTextNormalizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name);
    }

    @Override
    public Reader create(Reader reader) {
        return new OpenKoreanTextNormalizer(reader);
    }

    @Override
    public Object getMultiTermComponent() {
        return this;
    }

    public static class OpenKoreanTextStemmerFactory extends AbstractTokenFilterFactory {
        public OpenKoreanTextStemmerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
            super(indexSettings, name, settings);
        }

        @Override
        public TokenStream create(TokenStream tokenStream) {
            return new OpenKoreanTextStemmer(tokenStream);
        }
    }
}
