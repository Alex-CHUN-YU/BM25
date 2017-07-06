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
 */
public class BM25 {

    /**
     * CKIP POS.
     */
    private static ArrayList<Tuple<String, String>> pos = new ArrayList<Tuple<String, String>>();

    /**
     * Storage question.
     */
    private static Map<ArrayList<String>, String> map = new HashMap<ArrayList<String>, String>();

    /**
     * Corpus.
     */
    private static ArrayList<ArrayList<String>> documentList = new ArrayList<ArrayList<String>>();

    /**
     * Doc Of Corpus.
     */
    private static ArrayList<String> doc = new ArrayList<String>();

    /**
     * Test Document.
     */
    private static ArrayList<String> demoDocument = new ArrayList<String>();

    /**
     * Free parameter, usually chosen as k1 = 2.0.
     */
    private double k1;

    /**
     * Free parameter, usually chosen as b = 0.75.
     */
    private double b;

    /**
     * Default constructor with k1 = 1.2, b = 0.75.
     * @throws IOException
     */
    public BM25() throws IOException {
        this(1.2, 0.75);
    }

    /**
     * Constructor 主要負責將data載入以方便後續計算.
     *
     * @param k1 is a positive tuning parameter that calibrates
     * the document term frequency scaling. A k1 value of 0 corresponds to a
     * binary model (no term frequency), and a large value corresponds to using
     * raw term frequency.
     * @param b b is another tuning parameter which determines
     * the scaling by document length: b = 1 corresponds to fully scaling the
     * term weight by the document length, while b = 0 corresponds to no length
     * normalization.
     * @throws IOException */
    public BM25(final double k1, final double b) throws IOException {
        if (k1 < 0) {
            throw new IllegalArgumentException("Negative k1 = " + k1);
        }
        if (b < 0 || b > 1) {
            throw new IllegalArgumentException("Invalid b = " + b);
        }
        this.k1 = k1;
        this.b = b;
        POS ws = new POS();
        //Produce Dictionary
        File file = new File(".");
        String path = file.getCanonicalPath();
        // Set File Name
        String fileSeparator = System.getProperty("file.separator");
        String fileName = path + fileSeparator + "src//main//resources//TestFile.txt";
        InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
        BufferedReader read = new BufferedReader(isr);
        String str;
        // Read File
        while ((str = read.readLine()) != null) {
            //Each Sentence As A Doc
            doc = new ArrayList<String>();
            //Result Of POS
            pos = ws.seg(str);
            //Each Sentence Result Of POS
            for (int i = 0; i < pos.size(); i++) {
                //System.out.print(pos.get(i).getWord());
                doc.add(pos.get(i).getWord());
            }
            //System.out.println();
            //Calculate IDF Corpus
            documentList.add(doc);
            //Calculate TF Corpus
            demoDocument.addAll(doc);
            map.put(doc, str);
        }
        // Close Reader
        isr.close();
        read.close();
    }

    /**
     * 此tf有加入BM25之兩個參數進去k1(飽和程度，例如10個term為上限) and b(文件與文件建立於相同條件上).
     * @param tfDocument list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     */
    public double tf(final ArrayList<String> tfDocument, final String term) {
        //文件中(句子)指定term的出現頻率
        double freq = 0;
        //平均每個文件的term數量
        double avgDocSize = (demoDocument.size() / documentList.size());
        for (String word : tfDocument) {
            if (term.equalsIgnoreCase(word)) {
                freq++;
            }
        }
        //BM25 TF演算法
        double tf = (freq * (k1 + 1)) / (freq + k1 * (1 - b + b * (tfDocument.size()) / avgDocSize));
        return tf;
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
     * rank the score.
     * @param tfDoc Each Document TF
     * @param corpus Corpus
     * @param terms terms of question
     * @return the TF-IDF of term
     */
    public double score(final ArrayList<String> tfDoc, final ArrayList<ArrayList<String>> corpus,
                        final ArrayList<String> terms) {
        double sumScore = 0.0;
        for (String term : terms) {
            sumScore += tf(tfDoc, term) * idf(corpus, term);
        }
        return sumScore;
    }

    /**
     * Rank Answer By BM25.
     * @throws IOException except
     * @param question is our question
     */
    public String rankBM25(final String question) {
        ArrayList<String> MaxScore = new ArrayList<String>();
        ArrayList<String> terms = new ArrayList<String>();
        POS questionTerm = new POS();
        pos = questionTerm.seg(question);
        for (int i = 0; i < pos.size(); i++) {
            //System.out.print("[" + pos.get(i).getWord() + pos.get(i).getPos() + "] ");
            terms.add(pos.get(i).getWord());
        }
        double score;
        double max = 0;
        //tfDoc為每個文件(句子)的term
        for (ArrayList<String> tfDoc : documentList) {
            //計算每個文件(句子)與輸入的句子之分數
            score = this.score(tfDoc, documentList, terms);
            if (score > max) {
                max = score;
                MaxScore = tfDoc;
            }
        }
        return map.get(MaxScore);
    }
}