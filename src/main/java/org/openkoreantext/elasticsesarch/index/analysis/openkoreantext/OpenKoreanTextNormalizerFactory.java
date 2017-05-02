package org.openkoreantext.elasticsesarch.index.analysis.openkoreantext;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractCharFilterFactory;
import org.elasticsearch.index.analysis.MultiTermAwareComponent;

import java.io.Reader;

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
}
