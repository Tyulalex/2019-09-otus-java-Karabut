package hellootus;

import com.google.common.collect.ImmutableList;

public class HelloOtus {

    public static void main(String[] args) {

        ImmutableList<Integer> immutableList = ImmutableList.of(1, 2, 3, 4, 5, 4, 8);
        for (Integer e : immutableList) {
            System.out.println(e);
        }
    }
}
