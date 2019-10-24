package diyarraylisttest;

import diyrarraylist.DIYArrayList;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestDIYArrayList {

    private static final String[] STRING_ARRAY_LONG = {"a", "b", "c", "d", "e", "f", "g", "q", "r",
            "t", "e", "t", "e", "w", "e", "3", "d", "g", "d", "g", "e", "b", "e"};

    private static final String[] STRING_ARRAY_SHORT = {"t", "5"};

    @Test
    public void testAddFirstElementToTheEnd() {
        DIYArrayList<Integer> diyArrayList = new DIYArrayList<>();
        assertEquals(0, diyArrayList.size());
        assertTrue(diyArrayList.add(12));
        assertEquals(1, diyArrayList.size());
        assertEquals(diyArrayList.get(0), Integer.valueOf(12));
    }

    @Test
    public void testAddNotFirstElementToTheEnd() {
        DIYArrayList<Integer> diyArrayList = new DIYArrayList<>();
        diyArrayList.add(12);
        assertTrue(diyArrayList.add(54));
        assertEquals(2, diyArrayList.size());
    }

    @Test
    public void testAddAllCollectionsMethod() {
        List<String> diyArrayList = new DIYArrayList<>(STRING_ARRAY_LONG);
        assertTrue(Collections.addAll(diyArrayList, "5", "4", "3"));
        int newSize = diyArrayList.size();
        assertEquals(STRING_ARRAY_LONG.length + 3, newSize);
        assertEquals(diyArrayList.get(newSize - 1), "3");
        assertEquals(diyArrayList.get(newSize - 2), "4");
        assertEquals(diyArrayList.get(newSize - 3), "5");
    }


    @Test
    public void testCopyCollectionsMethodSuccessDestSizeEqualSrc() {
        List<Integer> src = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        List<Integer> dest = givenDiyListOfIntegersValues(0, 100);
        Collections.copy(dest, src);
        assertEquals(src.size(), dest.size());
        assertEquals(src.get(0), dest.get(0));
        assertEquals(src.get(src.size() - 1), dest.get(dest.size() - 1));
    }

    @Test
    public void testCopyCollectionStringSuccessDestSizeMoreThanSrc() {
        List<String> src = new DIYArrayList<>(STRING_ARRAY_SHORT);
        List<String> dest = new DIYArrayList<>(STRING_ARRAY_LONG);
        int initialSize = dest.size();
        Collections.copy(dest, src);
        assertEquals(STRING_ARRAY_SHORT[0], dest.get(0));
        assertEquals(STRING_ARRAY_SHORT[1], dest.get(1));
        assertEquals(initialSize, dest.size());
    }

    @Test
    public void testCopyCollectionStringExceptionDestSizeLessThanSrc() {
        List<String> src = new DIYArrayList<>(STRING_ARRAY_LONG);
        List<String> dest = new DIYArrayList<>(STRING_ARRAY_SHORT);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            Collections.copy(dest, src);
        });
    }


    @Test
    public void testSortCollectionsSuccess() {
        List<Integer> diyList = givenDiyListOfIntegersValues(0, 100);
        Collections.sort(diyList, Collections.reverseOrder());
        assertEquals(Integer.valueOf(99), diyList.get(0));
        assertEquals(Integer.valueOf(0), diyList.get(diyList.size() - 1));
    }


    @Test
    public void testUnsupportedOperationExceptionThrown() {
        List<String> list = new DIYArrayList<>();
        assertThrows(UnsupportedOperationException.class, list::iterator);
    }

    @Test
    public void testConstructorWithArray() {
        String[] array = new String[]{"1", "2"};
        List diyList = new DIYArrayList<>(array);
        assertEquals("1", diyList.get(0));
        array[0] = "3";
        assertEquals("1", diyList.get(0));
    }


    private List<Integer> givenDiyListOfIntegersValues(int startRange, int endRange) {
        List<Integer> src = IntStream.range(startRange, endRange).boxed().collect(Collectors.toList());
        List<Integer> diyList = new DIYArrayList<>();
        for (int i = 0; i < src.size(); i++) {
            diyList.add(i);
        }
        return diyList;
    }
}
