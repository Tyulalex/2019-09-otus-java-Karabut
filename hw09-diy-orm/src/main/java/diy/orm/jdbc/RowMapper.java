package diy.orm.jdbc;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapper<T> {

    public T mapRow(ResultSet resultSet, Class clazz) throws SQLException, ReflectiveOperationException {
        T obj = (T) clazz.getConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class fieldTypeClass = field.getType();
            String fieldName = field.getName();
            if (isInteger(fieldTypeClass)) {
                field.set(obj, resultSet.getInt(fieldName));
            } else if (isString(fieldTypeClass)) {
                field.set(obj, resultSet.getString(fieldName));
            } else if (isLong(fieldTypeClass)) {
                field.set(obj, resultSet.getLong(fieldName));
            } else if (isBoolean(fieldTypeClass)) {
                field.set(obj, resultSet.getBoolean(fieldName));
            } else if (isDouble(fieldTypeClass)) {
                field.set(obj, resultSet.getDouble(fieldName));
            } else if (isFloat(fieldTypeClass)) {
                field.set(obj, resultSet.getFloat(fieldName));
            } else if (isChar(fieldTypeClass)) {
                field.set(obj, resultSet.getString(fieldName));
            } else if (isShort(fieldTypeClass)) {
                field.set(obj, resultSet.getShort(fieldName));
            } else if (isBigDecimal(fieldTypeClass)) {
                field.set(obj, resultSet.getBigDecimal(fieldName));
            } else {
                throw new JdbcTemplateException("unable to convert");
            }
        }
        return obj;
    }

    private static boolean isBoolean(Class clazz) {
        return clazz.isAssignableFrom(boolean.class) || clazz.isAssignableFrom(Boolean.class);
    }

    private static boolean isInteger(Class clazz) {
        return clazz.isAssignableFrom(int.class) || clazz.isAssignableFrom(Integer.class);
    }

    private static boolean isLong(Class clazz) {
        return clazz.isAssignableFrom(long.class) || clazz.isAssignableFrom(Long.class);
    }

    private static boolean isShort(Class clazz) {
        return clazz.isAssignableFrom(short.class) || clazz.isAssignableFrom(Short.class);
    }

    private static boolean isByte(Class clazz) {
        return clazz.isAssignableFrom(byte.class) || clazz.isAssignableFrom(Byte.class);
    }

    private static boolean isDouble(Class clazz) {
        return clazz.isAssignableFrom(double.class) || clazz.isAssignableFrom(Double.class);
    }

    private static boolean isFloat(Class clazz) {
        return clazz.isAssignableFrom(float.class) || clazz.isAssignableFrom(Float.class);
    }

    private static boolean isChar(Class clazz) {
        return clazz.isAssignableFrom(char.class) || clazz.isAssignableFrom(Character.class);
    }

    private static boolean isString(Class clazz) {
        return clazz.isAssignableFrom(String.class);
    }

    private static boolean isBigDecimal(Class clazz) {
        return clazz.isAssignableFrom(BigDecimal.class);
    }
}
