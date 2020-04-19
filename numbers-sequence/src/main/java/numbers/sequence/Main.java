package numbers.sequence;

import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Main {

    private static final Integer MAX_PRINT_NUMBER = 10;

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        Thread one = new Thread(main::printNumbers);
        one.setName("PrinterThread1");
        Thread two = new Thread(main::printNumbers);
        two.setName("PrinterThread2");
        one.start();
        two.start();
        one.join();
        two.join();
    }

    public synchronized void printNumbers() {
        String currentThreadName = Thread.currentThread().getName();
        Supplier<IntStream> streamSupplier = () -> IntStream
                .concat(
                        IntStream.range(1, MAX_PRINT_NUMBER + 1),
                        IntStream.range(2, MAX_PRINT_NUMBER)
                                .map(i -> MAX_PRINT_NUMBER + 1 - i)
                );
        while (true) {
            streamSupplier.get().forEach(i -> {
                System.out.println(currentThreadName + " " + i);
                notify();
                try {
                    wait();
                } catch (InterruptedException ex) {

                }
            });
        }
    }
}
