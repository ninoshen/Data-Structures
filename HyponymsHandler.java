package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private final NGramMap ngMap;
    private final wordNetHelper wnHelper;

    public HyponymsHandler(NGramMap map, wordNetHelper wordNet) {
        ngMap = map;
        wnHelper = wordNet;
    }

    @Override
    public String handle(NgordnetQuery q) {
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        List<String> words = q.words();
        if (words == null || words.size() == 0) {
            return "[]";
        }
        Set<String> hypos = wnHelper.getHyponym(words.get(0));
        for (String word : words) {
            Set<String> hyponymsForWord = wnHelper.getHyponym(word);
            if (hyponymsForWord == null) {
                return "[]";
            }
            hypos.retainAll(wnHelper.getHyponym(word)); // contain in both specified set
        }
        List<String> result = new ArrayList<>(hypos);
        Collections.sort(result);
        if (k == 0) {
            return result.toString(); // start dealing with k!=0 case
        }

        Set<Double> ranking = new HashSet<>();
        Map<Double, String> map = new HashMap<>();
        for (String word : hypos) {
            Double val = getTotal(word, startYear, endYear);
            ranking.add(val);
            map.put(val, word);
        }

        List<Double> rankList = new ArrayList<>(ranking);
        Collections.sort(rankList, Collections.reverseOrder());

        if (rankList.isEmpty()) {
            return "[]";
        }
        List<String> resultK = new ArrayList<>();
        if (k <= rankList.size() - 1) { // return k answers
            for (int i = 0; i < k; i++) {
                // rank can only be nonzero values
                Double rank = rankList.get(i);
                if (rank != null && rank != 0) {
                    resultK.add(map.get(rank));
                }
            }
        } else { // return all answers we have
            for (Double rank : rankList) {
                // make sure rank are only nonzero values
                if (rank != null && rank != 0) {
                    resultK.add(map.get(rank));
                }
            }
        }

        Collections.sort(resultK);
        if (resultK.size() == 0) {
            return "[]";
        } else {
            Collections.sort(resultK);
            return resultK.toString();
        }
    }

    // helper methods
    private Double getTotal(String word, int startYear, int endYear) {
        TimeSeries history = ngMap.countHistory(word, startYear, endYear);
        Double total = null;
        if (history != null) {
            List<Double> perYear = history.data(); // retrieves frequency counts for each year
            total = 0.0;
            for (Double data : perYear) {
                total += data;
            }
        }
        return total;
    }
}
