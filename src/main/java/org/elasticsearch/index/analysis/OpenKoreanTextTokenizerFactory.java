package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.apache.lucene.analysis.ko.OpenKoreanTextTokenizer;

public class OpenKoreanTextTokenizerFactory extends AbstractTokenizerFactory {

    public OpenKoreanTextTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
    }

    @Override
    public Tokenizer create() {
        return new OpenKoreanTextTokenizer();
    }
}
