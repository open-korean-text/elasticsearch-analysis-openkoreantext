package org.apache.lucene.analysis.ko;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.*;
import org.elasticsearch.plugin.analysis.openkoreantext.AnalysisOpenKoreanTextPlugin;
import org.elasticsearch.test.ESTestCase;

import java.io.IOException;

import static org.hamcrest.Matchers.instanceOf;

public class OpenKoreanTextAnalyzerTest extends ESTestCase {
    public void testDefaultComponentsLoading() throws IOException {
        TestAnalysis analysis =  createTestAnalysis(new Index("test", "_na_"), Settings.EMPTY, new AnalysisOpenKoreanTextPlugin());

        CharFilterFactory charFilterFactory = analysis.charFilter.get("openkoreantext-normalizer");
        assertNotNull(charFilterFactory);
        assertThat(charFilterFactory, instanceOf(OpenKoreanTextNormalizerFactory.class));

        TokenizerFactory tokenizerFactory = analysis.tokenizer.get("openkoreantext-tokenizer");
        assertNotNull(tokenizerFactory);
        assertThat(tokenizerFactory, instanceOf(OpenKoreanTextTokenizerFactory.class));

        TokenFilterFactory tokenFilterFactory = analysis.tokenFilter.get("openkoreantext-stemmer");
        assertNotNull(tokenFilterFactory);
        assertThat(tokenFilterFactory, instanceOf(OpenKoreanTextStemmerFactory.class));

        tokenFilterFactory = analysis.tokenFilter.get("openkoreantext-redundant-filter");
        assertNotNull(tokenFilterFactory);
        assertThat(tokenFilterFactory, instanceOf(OpenKoreanTextRedundantFilterFactory.class));
    }
}