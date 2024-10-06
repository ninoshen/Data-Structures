package ngordnet.proj2b_testing;

import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.main.HyponymsHandler;
import ngordnet.main.wordNetHelper;
import ngordnet.ngrams.NGramMap;


public class AutograderBuddy {
    /**
     * Returns a HyponymHandler
     */

    public static NgordnetQueryHandler getHyponymHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {
        NGramMap ngm = new NGramMap(wordFile, countFile);
        wordNetHelper helper = new wordNetHelper(synsetFile, hyponymFile);
        return new HyponymsHandler(ngm, helper);
    }
}
