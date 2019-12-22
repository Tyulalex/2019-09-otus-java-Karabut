package json.object.writer.test_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ClassCollectionsOfObjectsData {

    private ClassPrimitiveData[] classPrimitiveDataArray;
    private List<ClassPrimitiveData> classPrimitiveDataList;
}
