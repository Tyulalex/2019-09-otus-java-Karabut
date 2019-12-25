package json.object.writer.test_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassCollectionsData {

    private int[] intArray;
    private String[] stringArray;
    private List<String> stringList;
    private Map<String, Integer> map;
}
