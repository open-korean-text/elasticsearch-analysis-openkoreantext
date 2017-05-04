package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.ko.OpenKoreanTextAnalyzer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

public class OpenKoreanTextAnalyzerProvider extends AbstractIndexAnalyzerProvider<OpenKoreanTextAnalyzer> {

    private final OpenKoreanTextAnalyzer analyzer;

    public OpenKoreanTextAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
        analyzer= new OpenKoreanTextAnalyzer();
    }

    @Override
    public OpenKoreanTextAnalyzer get() {
        return this.analyzer;
    }
}
