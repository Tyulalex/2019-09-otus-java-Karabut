package json.object.writer;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import json.object.writer.test_data.ClassCollectionsData;
import json.object.writer.test_data.ClassCollectionsOfObjectsData;
import json.object.writer.test_data.ClassPrimitiveData;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import javax.json.JsonException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class JsonObjectWriterTest {

    private static final String PRIMITIVE_DATA_JSON = "class-primitive-data.json";
    private static final String COLLECTION_DATA_JSON = "class-collections-data.json";
    private static final String NULLABLE_DATA = "class-nullable-data.json";
    private static final String OBJECTS_COLLECTION_DATA_JSON = "class-collections-of-objects-data.json";
    private JsonObjectWriter gson;

    @BeforeEach
    private void setUp() {
        gson = new JsonObjectWriter();
    }

    @Test
    public void testObjectToJsonPrimitiveData() throws IOException, JSONException, IllegalAccessException {
        String expectedJson = expectedResource(PRIMITIVE_DATA_JSON);
        var primitiveObject = new ClassPrimitiveData(1, "Text",
                true, 4.5, 5.6F, 'e', 123L, (byte) 1, (short) 23);
        String actualJson = gson.toJson(primitiveObject);
        System.out.println(actualJson);
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }


    @Test
    public void testObjectToJsonCollectionsAndArrays() throws IOException, JsonException, JSONException, IllegalAccessException {
        String expectedJson = expectedResource(COLLECTION_DATA_JSON);
        var objectToJson = new ClassCollectionsData();
        objectToJson.setIntArray(new int[]{3, 5, 6});
        objectToJson.setMap(Map.of("key1", 1, "key2", 2));
        objectToJson.setStringArray(new String[]{});
        objectToJson.setStringList(List.of("1", "test", "qwerty"));
        String actualJson = gson.toJson(objectToJson);
        System.out.println(actualJson);
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    public void testNullableValues() throws IOException, JsonException, JSONException, IllegalAccessException {
        String expectedJson = expectedResource(NULLABLE_DATA);
        var objectToJson = new ClassCollectionsData();
        String actualJson = gson.toJson(objectToJson);
        System.out.println(actualJson);
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    public void testCollectionsAndArrayOfObjects() throws IOException, JSONException, IllegalAccessException {
        String expectedJson = expectedResource(OBJECTS_COLLECTION_DATA_JSON);
        var obj = new ClassCollectionsOfObjectsData();
        ClassPrimitiveData[] classPrimitiveDataArr = new ClassPrimitiveData[3];
        Arrays.fill(classPrimitiveDataArr, createClassPrimitiveDataDefaultValues());
        List<ClassPrimitiveData> classPrimitiveDataList = List.of(
                createClassPrimitiveDataDefaultValues(),
                createClassPrimitiveDataDefaultValues()
        );
        obj.setClassPrimitiveDataArray(classPrimitiveDataArr);
        obj.setClassPrimitiveDataList(classPrimitiveDataList);
        String actualJson = gson.toJson(obj);
        System.out.println(actualJson);
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    private String expectedResource(String resourcePath) throws IOException {
        URL url = Resources.getResource(resourcePath);
        return Resources.toString(url, Charsets.UTF_8);
    }

    private ClassPrimitiveData createClassPrimitiveDataDefaultValues() {
        return new ClassPrimitiveData(1, "Text",
                true, 4.5, 5.6F, 'e', 123L, (byte) 1, (short) 23);
    }

}