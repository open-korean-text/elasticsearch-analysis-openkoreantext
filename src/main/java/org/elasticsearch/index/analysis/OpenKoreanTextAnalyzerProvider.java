package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.ko.OpenKoreanTextAnalyzer;
import org.apache.lucene.analysis.ko.UserDictionaryLoader;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

/**
 * A analyzer provider for openkoreantext.
 */
public class OpenKoreanTextAnalyzerProvider extends AbstractIndexAnalyzerProvider<OpenKoreanTextAnalyzer> {

    private final OpenKoreanTextAnalyzer analyzer;

    public OpenKoreanTextAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
        analyzer= new OpenKoreanTextAnalyzer();
        UserDictionaryLoader.loadDefaultUserDictionaries();
    }

    @Override
    public OpenKoreanTextAnalyzer get() {
        return this.analyzer;
    }
}
