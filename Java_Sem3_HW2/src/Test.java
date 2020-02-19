import java.util.*;

public class Test {
    public static void main(String[] args) {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return t1 - integer;
            }
        };
        List<Integer> list = List.of(1, 5, 10);
        ArraySet<Integer> arraySet = new ArraySet<>(list);
        System.out.println(Collections.binarySearch(list, 11, Collections.reverseOrder(comparator)));
    }
}
