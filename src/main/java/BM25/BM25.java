package BM25;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import BM25.POS.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * The BM25 weighting scheme, often called Okapi weighting.
 * 計算文件與文件之間的相關性
 * @version 1.0 2017年4月27日
 * @author ALEX-CHUN-YU
 */
public class BM25 {
    /**
     * CKIP POS.
     */
    private static ArrayList<Tuple<String, String>> sentenceSegResult = new ArrayList<Tuple<String, String>>();

    /**
     * Storage question.
     */
    private static Map<ArrayList<String>, String> corpusHashmap = new HashMap<ArrayList<String>, String>();

    /**
     * Corpus.
     */
    private static ArrayList<ArrayList<String>> documentList = new ArrayList<ArrayList<String>>();

    /**
     * Test Document.
     */
    private static ArrayList<String> allTermsInCorpus = new ArrayList<String>();

    /**
     * Free parameter, usually chosen as k1 = 1.2. range: 1.2 ~ 2.0 越少飽和速率越快!
     */
    private double k1;

    /**
     * Free parameter, usually chosen as b = 0.75. range: 0 ~ 1.0 越接近1,文檔的長度歸約化到全部文檔的平均長度上越大
     */
    private double b;

    /**
     * Default constructor with k1 = 1.2, b = 0.75.
     * @throws IOException IOException
     */
    public BM25() throws IOException {
        this(1.2, 0.75);
    }

    /**
     * Constructor 主要負責將 Data 載入以方便後續計算.
     *
     * @param k1 is a positive tuning parameter that calibrates
     * the document term frequency scaling. A k1 value of 0 corresponds to a
     * binary model (no term frequency), and a large value corresponds to using
     * raw term frequency.
     * @param b b is another tuning parameter which determines
     * the scaling by document length: b = 1 corresponds to fully scaling the
     * term weight by the document length, while b = 0 corresponds to no length
     * normalization.
     * @throws IOException IOException */
    public BM25(final double k1, final double b) throws IOException {
        if (k1 < 0) {
            throw new IllegalArgumentException("Negative k1 = " + k1);
        }
        if (b < 0 || b > 1) {
            throw new IllegalArgumentException("Invalid b = " + b);
        }
        this.k1 = k1;
        this.b = b;
        POS posCKIP = new POS();
        //Produce Dictionary
        File file = new File(".");
        String path = file.getCanonicalPath();
        // Set File Name
        String fileSeparator = System.getProperty("file.separator");
        String fileName = path + fileSeparator + "src//main//resources//TestFile.txt";
        InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
        BufferedReader read = new BufferedReader(isr);
        String sentence;
        ArrayList<String> document;
        // Load BM25 Need Data
        while ((sentence = read.readLine()) != null) {
            //Each Sentence As A Doc
            document = new ArrayList<String>();
            //Result Of POS
            sentenceSegResult = posCKIP.seg(sentence);
            //Each Sentence Result Of POS
            for (Tuple<String, String> segment : sentenceSegResult) {
                document.add(segment.getWord());
            }
            // Calculate IDF Corpus
            documentList.add(document);
            // Calculate TF Corpus
            allTermsInCorpus.addAll(document);
            // 儲存資料匹配的數據
            corpusHashmap.put(document, sentence);
        }
        // Close Reader
        isr.close();
        read.close();
    }

    /**
     * 此 TF 有加入 BM25 之兩個參數進去 k1(飽和程度，例如10個term為上限) and b(文件與文件建立於相同條件上).
     * @param tfDocument list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     */
    public double tf(final ArrayList<String> tfDocument, final String term) {
        // 文件中(句子)指定 term 的出現頻率
        double freq = 0;
        //平均每個文件的 term 數量
        double avgDocSize = (allTermsInCorpus.size() / documentList.size());
        for (String word : tfDocument) {
            if (term.equalsIgnoreCase(word)) {
                freq++;
            }
        }
        //BM25 TF演算法
        return (freq * (k1 + 1)) / (freq + k1 * (1 - b + b * (tfDocument.size()) / avgDocSize));
    }

    /**
     * @param corpus list of list of strings represents the DataSet
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     */
    public double idf(final ArrayList<ArrayList<String>> corpus, final String term) {
        double count = 0;
        for (ArrayList<String> idfDoc : corpus) {
            for (String word : idfDoc) {
                if (term.equalsIgnoreCase(word)) {
                    count++;
                    break;
                }
            }
        }
        //count + 1 avoid infinity
        return Math.log((corpus.size() - count + 0.5) / (count + 0.5));
    }

    /**
     * Rank The Score.
     * @param tfDoc Each Document TF
     * @param corpus Corpus
     * @param termList terms of question
     * @return the TF-IDF of term
     */
    public double score(final ArrayList<String> tfDoc, final ArrayList<String> termList,
                        final ArrayList<ArrayList<String>> corpus) {
        double sumScore = 0.0;
        for (String term : termList) {
            sumScore += tf(tfDoc, term) * idf(corpus, term);
        }
        return sumScore;
    }

    /**
     * Rank Answer By BM25.
     * @param question is our question
     */
    public String rankBM25(final String question) {
        ArrayList<String> maxScoreDocument = new ArrayList<String>();
        ArrayList<String> segmentList = new ArrayList<String>();
        POS posCKIP = new POS();
        sentenceSegResult = posCKIP.seg(question);
        for (Tuple<String, String> segment : sentenceSegResult) {
            segmentList.add(segment.getWord());
        }
        double maxScore = 0;
        double score;
        // document 為每個文件(句子)的 term list
        for (ArrayList<String> document : documentList) {
            // 計算每個文件(句子)與輸入的句子之分數
            score = this.score(document, segmentList, documentList);
            if (score > maxScore) {
                maxScore = score;
                maxScoreDocument = document;
            }
        }
        // 取出最佳匹配的數據
        return corpusHashmap.get(maxScoreDocument);
    }
}
