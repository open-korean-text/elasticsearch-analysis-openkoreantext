package org.elasticsesarch.plugin.analysis.openkoreantext;

import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.plugin.analysis.openkoreantext.AnalysisOpenKoreanTextPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.PluginInfo;
import org.elasticsearch.test.ESIntegTestCase;
import org.junit.Assert;

import java.util.Collection;
import java.util.Collections;

public class AnalysisOpenKoreanTextPluginTest extends ESIntegTestCase {
    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return Collections.singleton(AnalysisOpenKoreanTextPlugin.class);
    }

    public void testPluginIsLoaded() {
        NodesInfoResponse response = client().admin().cluster().prepareNodesInfo().setPlugins(true).get();
        for (NodeInfo node : response.getNodes()) {
            boolean founded = false;
            for (PluginInfo pluginInfo : node.getPlugins().getPluginInfos()) {
                if (pluginInfo.getName().equals(AnalysisOpenKoreanTextPlugin.class.getName())) {
                    founded = true;
                }
            }
            Assert.assertTrue(founded);
        }
    }
}