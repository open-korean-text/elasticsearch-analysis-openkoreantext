package org.openkoreantext.elasticsesarch.plugin.analysis.openkoreantext;

import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.openkoreantext.elasticsesarch.index.analysis.openkoreantext.OpenKoreanTextTokenizerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnalysisOpenKoreanTextPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> tokenizerFactories = new HashMap<>();
        tokenizerFactories.put("openkoreantext-tokenizer", OpenKoreanTextTokenizerFactory::new);
        return tokenizerFactories;
    }
}
