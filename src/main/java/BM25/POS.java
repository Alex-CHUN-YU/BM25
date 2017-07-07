package BM25;

import java.util.ArrayList;

import tw.cheyingwu.ckip.CKIP;
import tw.cheyingwu.ckip.Term;

/**
 * CKIP Part of Speech.
 *
 * @version 1.0 2017年4月27日
 * @author NCKU WMMKS LAB
 *
 */
public class POS {

    /**
     * CKIP serverIP.
     */
    private static String serverIP = "140.109.19.104";

    /**
     * CKIP serverPort.
     */
    private static String serverPort = "1501";

    /**
     * CKIP userName.
     */
    private static String userName = "duke@mt.com.tw"; // "kenmonmouth";

    /**
     * CKIP User Password.
     */
    private static String password = "Duke9876!"; // "6cd25e45";

    /**
     * CKIP User connection.
     */
    private CKIP connection;

    /**
     * CKIP connect.
     */
    public POS() {
        connect(serverIP, Integer.parseInt(serverPort), userName, password);
    }

    /**
     * CKIP connect.
     * @param serverIPTemp CKIP serverIP
     * @param serverPortTemp CKIP serverPort
     * @param userNameTemp CKIP userName
     * @param passwordTemp User Password
     */
    private void connect(final String serverIPTemp, final int serverPortTemp,
                         final String userNameTemp, final String passwordTemp) {
        connection = new CKIP(serverIPTemp, serverPortTemp, userNameTemp, passwordTemp);
    }

    /**
     * CKIP connect.
     *
     * @param sentence
     *            Segment
     * @return object
     */
    public ArrayList<Tuple<String, String>> seg(final String sentence) {
        ArrayList<Tuple<String, String>> wsResult = new ArrayList<Tuple<String, String>>();
        connection.setRawText(sentence);
        connection.send();
        for (Term t : connection.getTerm()) {
            Tuple<String, String> pair = new Tuple<String, String>(t.getTerm(), t.getTag());
            wsResult.add(pair);
        }
        return wsResult;
    }

    /**
     * Tuple Format.
     *
     * @param <X> declare X
     * @param <Y> declare Y
     *
     */
    public static class Tuple<X, Y> {
        //X segmentWord.
        private X word;
        //Y segmentWord.
        private Y getPos;

        /**
         * get x segment.
         * @return word
         */
        public X getWord() {
            return word;
        }

        /**
         * get y segment.
         * @return POS
         */
        public Y getPos() {
            return getPos;
        }

        /**
         * segmentPOS.
         * @param x is X declare
         * @param y is Y declare
         */
        public Tuple(final X x, final Y y) {
            this.word = x;
            this.getPos = y;
        }
    }
    
}
