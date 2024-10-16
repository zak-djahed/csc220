
package prog11;

import prog05.ArrayQueue;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Queue;
import java.util.*;

public class NotGPT implements SearchEngine{
    //@Override

    public HardDisk pageDisk = new HardDisk();
    public Map<String, Long> urlToIndex = new TreeMap<>();
    public HardDisk wordDisk = new HardDisk();
    public Map<String, Long> wordToIndex = new HashMap();

    public Long indexPage(String url){
        Long index = pageDisk.newFile();
        InfoFile newFile = new InfoFile(url);
        pageDisk.put(index, newFile);
        urlToIndex.put(url, index);
        System.out.println("indexing page " + index + " " + newFile.toString());

        return index;
    }
    public void collect(Browser browser, List<String> startingURLs) {
        Queue<Long> queue = new ArrayQueue<>();

        for(String url: startingURLs){
            if(!urlToIndex.containsKey(url)){
                Long index1 = indexPage(url);
                queue.add(index1);
            }
        }
        while(!queue.isEmpty()){
            Long currIndex = queue.poll();
            InfoFile pageFile = pageDisk.get(currIndex);

            if(browser.loadPage(pageFile.data)){
                List<String> urls = browser.getURLs();
                Set<String> urlsOnPage = new HashSet<String>();

                for(String url : urls){

                    if(!urlsOnPage.contains(url)){
                        urlsOnPage.add(url);
                        Long index3 = urlToIndex.get(url);
                        if(index3 == null){
                            index3 = indexPage(url);
                            queue.offer(index3);
                        }
                        pageFile.indices.add(index3);
                    }
                }
            }

            pageDisk.put(currIndex, pageFile);
            List<String> words = browser.getWords();
            Set<String> wordsOnPage = new HashSet<String>();

            for(String word : words){
                if(!wordsOnPage.contains(word)){
                    wordsOnPage.add(word);
                    Long indexWord = wordToIndex.get(word);
                    if(indexWord == null){
                        indexWord  = indexWord(word);
                    }
                    InfoFile wordFile = wordDisk.get(indexWord);
                    wordFile.indices.add(currIndex);
                    wordDisk.put(indexWord, wordFile);
                }
            }
        }

    }

    public Long indexWord (String word){
        Long index = wordDisk.newFile();
        InfoFile newInfoFile = new InfoFile(word);
        wordDisk.put(index, newInfoFile);
        wordToIndex.put(word, index);
        System.out.println("indexing word " + index + " " + newInfoFile.toString());
        return index;
    }

    void rankSlow(double defaultInfluence){
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            double influencePerIndex = file.influence/file.indices.size();

            for(long var : file.indices){
                pageDisk.get(var).influenceTemp += influencePerIndex;
            }
        }

        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.influence = file.influenceTemp + defaultInfluence;
            file.influenceTemp = 0.0;
        }
    }
    void rankFast (double defaultInfluence){
        List<Vote> votes = new ArrayList<>();
        for(Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()){
            //long index = entry.getKey();
            InfoFile file = entry.getValue();

            for(Long index  : file.indices){
                Vote vote = new Vote(index, file.influence/file.indices.size());
                votes.add(vote);
            }
        }
        Collections.sort(votes);
        Iterator<Vote> iterator = votes.iterator();
        Vote currVote = iterator.next();

        for (Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.influence = defaultInfluence;

            while(currVote != null && currVote.index == index){
                file.influence += currVote.vote;
                if(iterator.hasNext()){
                    currVote = iterator.next();
                }
                else{
                    break;
                }
            }
        }


    }
    @Override
    public void rank(boolean fast) {
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.influence = 1.0;
            file.influenceTemp = 0.0;
        }

        double count = 0.0;
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            if(file.indices.size() == 0){
                count++;
            }
        }

        double defaultInfluence = 1.0 * count / pageDisk.size();
        if(!fast){
            for(int i = 0; i < 20; i++){
                rankSlow(defaultInfluence);
            }
        }
        else if (fast){
            for(int i = 0; i < 20; i++){
                rankFast(defaultInfluence);
            }
        }
    }

    @Override
    public String[] search(List<String> searchWords, int numResults) {
        for(int i = 0; i < searchWords.size(); i++){
            if(!wordToIndex.containsKey(searchWords.get(i))){
                searchWords.remove(i);
            }
        }
        if(searchWords.isEmpty()){
            return new String[0];
        }

        Iterator<Long>[] wordPageIndexIterators = (Iterator<Long>[]) new Iterator[searchWords.size()];
        long[] currentPageIndex = new long[searchWords.size()];
        PageComparator pageComparator = new PageComparator();
        PriorityQueue<Long> bestPageIndices = new PriorityQueue<Long>(pageComparator);

        for(int i = 0; i < searchWords.size(); i++){
            String word = searchWords.get(i);
            Long index = wordToIndex.get(word);
            InfoFile wordFile = wordDisk.get(index);

            List<Long> indices = wordFile.indices;
            wordPageIndexIterators[i] = indices.iterator();
        }
        while(getNextPageIndexes(currentPageIndex, wordPageIndexIterators)){
            if(allEqual(currentPageIndex)){
                if(bestPageIndices.size() < numResults){
                    bestPageIndices.offer(currentPageIndex[0]);
                }
                else if (pageComparator.compare(currentPageIndex[0] , bestPageIndices.peek()) > 0){
                    bestPageIndices.poll();
                    bestPageIndices.offer(currentPageIndex[0]);
                }

                System.out.println(pageDisk.get(currentPageIndex[0]).data);
            }
        }
        String[] results = new String[bestPageIndices.size()];
        for(int i = results.length - 1; i >= 0; i--){
            results[i] = pageDisk.get(bestPageIndices.poll()).data;
        }

        return results;
    }

    private boolean allEqual (long[] array){
        for(int i = 1; i < array.length; i++){
            if(array[i] != array[i-1]){
                return false;
            }
        }
        return true;
    }
    private long getLargest (long[] array){
        long largest = array[0];

        for(int i = 0; i < array.length; i++){
            if(array[i] > largest){
                largest = array[i];
            }
        }
        return largest;
    }

    private boolean getNextPageIndexes (long[] currentPageIndexes, Iterator<Long>[] wordFileIterators){
        if(allEqual(currentPageIndexes)){
            for(int i = 0; i < wordFileIterators.length; i++){
                if(!wordFileIterators[i].hasNext()){
                    return false;
                }
                currentPageIndexes[i] = wordFileIterators[i].next();
            }
        }
        else{
            long largest = getLargest(currentPageIndexes);

            for(int i = 0; i < wordFileIterators.length; i++){
                if(currentPageIndexes[i] != largest){
                    if(!wordFileIterators[i].hasNext()){
                        return false;
                    }
                    currentPageIndexes[i] = wordFileIterators[i].next();
                }
            }
        }
        return true;
    }

    public class PageComparator implements Comparator<Long>{
        @Override
        public int compare (Long pageIndex1, Long pageIndex2){
            double difference = pageDisk.get(pageIndex1).influence - pageDisk.get(pageIndex2).influence;
            if(difference < 0){
                return -1;
            }
            else if(difference > 0){
                return 1;
            }
            else{
                return 0;
            }
        }
    }
    public class Vote implements Comparable<Vote>{
        Long index;
        Double vote;
        Vote (Long index, Double vote){
            this.index = index;
            this.vote = vote;
        }

        public int compareTo(Vote v){
            if(!index.equals(v.index)){
                return (int)(index - v.index);
            }
            else{
                return vote.compareTo(v.vote);
            }

        }
    }
}


