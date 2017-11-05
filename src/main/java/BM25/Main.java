package BM25;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by alex on 2017/7/6.
 */
public class Main {
    /**
     *
     * Main Function BM25 Test.
     *
     * @param args argument
     * @throws IOException IOException
     *
     */
    public static void main(final String[] args) throws IOException {
        System.out.println("資料載入中..");
        BM25 calculator = new BM25();
        System.out.println("請輸入一句類似 Demo 中的句子");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String question = scanner.nextLine();
            System.out.print("Data中最接近的句子:");
            System.out.println(calculator.rankBM25(question));
        }
    }
}
