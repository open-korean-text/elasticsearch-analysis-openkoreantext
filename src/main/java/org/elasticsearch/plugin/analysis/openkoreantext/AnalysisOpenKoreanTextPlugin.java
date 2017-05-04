package org.elasticsearch.plugin.analysis.openkoreantext;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.analysis.*;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

public class AnalysisOpenKoreanTextPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        return singletonMap("openkoreantext-analyzer", OpenKoreanTextAnalyzerProvider::new);
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<CharFilterFactory>> getCharFilters() {
        return singletonMap("openkoreantext-normalizer", OpenKoreanTextNormalizerFactory::new);
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> tokenizerFactories = new HashMap<>();
        tokenizerFactories.put("openkoreantext-tokenizer", OpenKoreanTextTokenizerFactory::new);
        return tokenizerFactories;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> tokenFilters = new HashMap<>();
        tokenFilters.put("openkoreantext-stemmer", OpenKoreanTextStemmerFactory::new);
        tokenFilters.put("openkoreantext-redundant-filter", OpenKoreanTextRedundantFilterFactory::new);
        tokenFilters.put("openkoreantext-phrase-extractor", OpenKoreanTextPhraseExtractorFactory::new);

        return tokenFilters;
    }
}
