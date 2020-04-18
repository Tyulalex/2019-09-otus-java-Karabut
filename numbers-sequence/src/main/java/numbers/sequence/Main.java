package numbers.sequence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    private static final Integer MAX_PRINT_NUMBER = 10;
    private static final List<String> THREAD_NAMES = List.of("PrinterThread1", "PrinterThread2");
    private static Map<String, Integer> threadToPrintNumberForward = new HashMap<>();
    private static Map<String, Integer> threadToPrintNumberBackward = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        Thread one = new Thread(main::printNumbers);
        one.setName(THREAD_NAMES.get(0));
        Thread two = new Thread(main::printNumbers);
        two.setName(THREAD_NAMES.get(1));
        threadToPrintNumberForward.put(one.getName(), 0);
        threadToPrintNumberForward.put(two.getName(), 0);
        threadToPrintNumberBackward.put(one.getName(), MAX_PRINT_NUMBER);
        threadToPrintNumberBackward.put(two.getName(), MAX_PRINT_NUMBER);
        one.start();
        two.start();
        one.join();
        two.join();
    }

    public synchronized void printNumbers() {
        String currentThreadName = Thread.currentThread().getName();
        String otherThreadName = getOtherThreadName(currentThreadName);
        for (int i = 1; i <= MAX_PRINT_NUMBER; i++) {
            doPrintAndNotify(currentThreadName, i, threadToPrintNumberForward);
            while (threadToPrintNumberForward.get(otherThreadName) < i) {
                try {
                    wait();
                } catch (InterruptedException ex) {

                }
            }
        }
        for (int j = MAX_PRINT_NUMBER - 1; j > 0; j--) {
            doPrintAndNotify(currentThreadName, j, threadToPrintNumberBackward);
            while (threadToPrintNumberBackward.get(otherThreadName) > j) {
                try {
                    wait();
                } catch (InterruptedException ex) {

                }
            }
        }
    }

    private String getOtherThreadName(String currentThreadName) {
        Optional<String> optionalOtherThreadName = THREAD_NAMES.stream()
                .filter(i -> !i.equals(currentThreadName)).findFirst();
        return optionalOtherThreadName.orElseThrow();
    }

    private void doPrintAndNotify(String currentThreadName, int number, Map<String, Integer> sharedNotifyMap) {
        System.out.println(currentThreadName + " " + number);
        sharedNotifyMap.put(currentThreadName, number);
        notify();
    }
}
