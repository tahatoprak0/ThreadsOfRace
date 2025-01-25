import java.util.ArrayList;
import java.util.Collections;

public class NumberSplitter {
    // Ortak erişim için synchronized listeler
    private static final ArrayList<Integer> evenNumbers = new ArrayList<>();
    private static final ArrayList<Integer> oddNumbers = new ArrayList<>();

    public static void main(String[] args) {
        // 1'den 10000'e kadar sayıları içeren ArrayList oluşturuluyor
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            numbers.add(i);
        }

        // Listeleri parçalamak için bölümler oluşturuluyor
        ArrayList<ArrayList<Integer>> partitions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            partitions.add(new ArrayList<>(numbers.subList(i * 25, (i + 1) * 25)));
        }

        // 4 farklı thread oluşturuluyor
        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            threads[i] = new Thread(new NumberProcessor(partitions.get(i)));
            threads[i].start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Sonuçları ekrana yazdır
        Collections.sort(evenNumbers);
        Collections.sort(oddNumbers);
        System.out.println("Çift Sayılar: " + evenNumbers);
        System.out.println("Tek Sayılar: " + oddNumbers);
    }

    // Sayıları işleyen Runnable sınıfı
    static class NumberProcessor implements Runnable {
        private final ArrayList<Integer> list;

        public NumberProcessor(ArrayList<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (int number : list) {
                if (number % 2 == 0) {
                    synchronized (evenNumbers) {
                        evenNumbers.add(number);
                    }
                } else {
                    synchronized (oddNumbers) {
                        oddNumbers.add(number);
                    }
                }
            }
        }
    }
}
