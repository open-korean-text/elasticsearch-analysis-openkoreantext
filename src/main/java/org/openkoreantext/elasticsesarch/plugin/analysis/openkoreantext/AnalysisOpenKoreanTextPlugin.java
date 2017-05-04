package org.openkoreantext.elasticsesarch.plugin.analysis.openkoreantext;

import org.elasticsearch.index.analysis.CharFilterFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.openkoreantext.elasticsesarch.index.analysis.openkoreantext.OpenKoreanTextNormalizerFactory;
import org.openkoreantext.elasticsesarch.index.analysis.openkoreantext.OpenKoreanTextStemmerFactory;
import org.openkoreantext.elasticsesarch.index.analysis.openkoreantext.OpenKoreanTextTokenizerFactory;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

public class AnalysisOpenKoreanTextPlugin extends Plugin implements AnalysisPlugin {

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
        return tokenFilters;
    }
}
