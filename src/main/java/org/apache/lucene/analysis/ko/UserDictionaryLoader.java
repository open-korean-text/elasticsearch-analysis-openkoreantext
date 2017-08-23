package org.apache.lucene.analysis.ko;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.index.analysis.OpenKoreanTextTokenizerFactory;
import org.openkoreantext.processor.OpenKoreanTextProcessor;
import scala.collection.JavaConverters;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loader to add user custom dictionaries
 * dictionaries must be located in {PLUGIN_PATH}/dic/
 */
public class UserDictionaryLoader {

    private static Logger logger = ESLoggerFactory.getLogger(UserDictionaryLoader.class);

    private final static Map<String, Boolean> loadedDictionaryFiles = new HashMap<>();

    private static final String DEFAULT_DIC_SUFFIX = "dic/";
    private static File[] dicFiles;

    static {
        String currentPath = OpenKoreanTextTokenizerFactory.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        dicFiles = new File[]{};

        try {
            File dicDirectory = new File(new File(currentPath).getParent() + "/" + DEFAULT_DIC_SUFFIX);
            if(dicDirectory.isDirectory()) {
                dicFiles = dicDirectory.listFiles();
            }
        } catch (AccessControlException e) {
            logger.error("Can not load dictionary files", e);
        }
    }

    public static void loadDefaultUserDictionaries() {
        for(File file : dicFiles) {
            Boolean loaded = loadedDictionaryFiles.get(file.getPath());
            if(loaded == null ||loaded == false) {
                try {
                    addUserDictionary(file);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
                loadedDictionaryFiles.put(file.getPath(), true);
            }
        }
    }

    public static void addUserDictionary(List<String> words) {
        OpenKoreanTextProcessor.addNounsToDictionary(JavaConverters.asScalaBuffer(words).toSeq());
    }

    public static void addUserDictionary(File file) throws IOException {
        addUserDictionary(new BufferedReader(new FileReader(file)));
    }

    public static void addUserDictionary(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        addUserDictionary(new BufferedReader(new InputStreamReader(connection.getInputStream())));
    }

    private static void addUserDictionary(BufferedReader bufferedReader) throws IOException {
        List<String> words = new ArrayList<>();
        String word;
        while ((word = bufferedReader.readLine()) != null) {
            if(Strings.isEmpty(word)) continue;
            words.add(word);
        }
        addUserDictionary(words);
    }
}
