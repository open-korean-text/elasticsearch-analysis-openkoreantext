package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ko.OpenKoreanTextTokenizer;
import org.apache.lucene.analysis.ko.UserDictionaryLoader;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

/**
 * A ES tokenizer factory for {@link OpenKoreanTextTokenizer}.
 */
public class OpenKoreanTextTokenizerFactory extends AbstractTokenizerFactory {

    public OpenKoreanTextTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
        UserDictionaryLoader.loadDefaultUserDictionaries();
    }

    @Override
    public Tokenizer create() {
        return new OpenKoreanTextTokenizer();
    }
}
