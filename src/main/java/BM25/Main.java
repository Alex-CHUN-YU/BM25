package BM25;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by alex on 2017/7/6.
 */
public class Main {

    /**
     *
     * Main Function Test BM25.
     *
     * @param args argument
     * @throws IOException IOException
     *
     */
    public static void main(final String[] args) throws IOException {
        BM25 calculator = new BM25();
        System.out.println("請輸入一句Demo中的句子");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String sentence = scanner.nextLine();
            System.out.print("Data中最接近的話:");
            System.out.println(calculator.rankBM25(sentence));
        }
    }
}
