package diy.orm.jdbc;

import diy.orm.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
public class JdbcTemplateImpl<T> implements JdbcTemplate {

    private final static String SQL_INSERT_PATTERN = "INSERT INTO \"%s\" (%s) VALUES (%s);";

    private final static String SQL_SELECT_PATTERN = "SELECT * FROM \"%s\" WHERE %s = ?;";

    private final static String SQL_UPDATE_PATTERN = "UPDATE \"%s\" SET %s WHERE %s = ?;";

    private final DbExecutor dbExecutor;
    private final RowMapper<T> rowMapper;

    public JdbcTemplateImpl(DbExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
        this.rowMapper = new RowMapper<>();
    }

    @Override
    public void create(Connection connection, Object objectData) throws SQLException, IllegalAccessException {
        checkObject(objectData);
        String sql = builtInsertSql(objectData);

        try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setColumnsValues(objectData, pst, getFieldsWithoutIdField(objectData.getClass().getDeclaredFields()));

            long id = dbExecutor.insert(connection, pst);
            IdFieldDto<Long> idFieldDto = getFieldWithIdAnnotation(objectData);
            idFieldDto.getField().set(objectData, id);

        } catch (SQLException | IllegalAccessException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public void update(Connection connection, Object objectData) throws SQLException, IllegalAccessException {
        checkObject(objectData);
        String sql = buildUpdateSql(objectData);

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            Field[] fieldsWithNoID = getFieldsWithoutIdField(objectData.getClass().getDeclaredFields());
            IdFieldDto<Long> idFieldDto = getFieldWithIdAnnotation(objectData);
            setColumnsValues(objectData, pst, fieldsWithNoID);
            pst.setLong(fieldsWithNoID.length + 1, Long.valueOf(idFieldDto.getValue()));
            dbExecutor.update(connection, pst);
        } catch (SQLException | IllegalAccessException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public void createOrUpdate(Connection connection, Object objectData) throws ReflectiveOperationException, SQLException {
        IdFieldDto<Long> idFieldDto = getFieldWithIdAnnotation(objectData);
        Optional<T> objLoaded = Optional.empty();
        try {
            if (idFieldDto.getValue() != null) {
                objLoaded = this.load(connection, (long) idFieldDto.getValue(), objectData.getClass());
            }
            if (objLoaded.isPresent()) {
                this.update(connection, objectData);
            } else {
                this.create(connection, objectData);
            }

        } catch (SQLException | ReflectiveOperationException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public Optional<T> load(Connection connection, long id, Class clazz) throws SQLException, ReflectiveOperationException {
        String sql = buildSelectSql(clazz);
        Object obj = null;

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setLong(1, id);
            ResultSet resultSet = dbExecutor.select(connection, pst);
            while (resultSet.next()) {
                obj = rowMapper.mapRow(resultSet, clazz);
            }

            return Optional.ofNullable((T) obj);

        } catch (SQLException | ReflectiveOperationException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }


    private String buildSelectSql(Class clazz) throws IllegalAccessException {
        String tableName = getTableName(clazz);
        String primaryKeyName = getFieldWithIdAnnotation(clazz).getName();

        return String.format(SQL_SELECT_PATTERN, tableName, primaryKeyName);
    }


    private String builtInsertSql(Object object) {
        String tableName = getTableName(object.getClass());
        String[] columnNames = getNonPrimaryKeyColumns(object.getClass().getDeclaredFields());
        String columns = String.join(", ", columnNames);
        String[] valuesSign = new String[columnNames.length];
        Arrays.fill(valuesSign, "?");
        String values = String.join(", ", valuesSign);
        String insertSql = String.format(SQL_INSERT_PATTERN, tableName, columns, values);
        log.info("INSERT SQL: {}", insertSql);

        return insertSql;
    }

    private String[] getNonPrimaryKeyColumns(Field[] fields) {
        Field[] fieldsWithoutIdField = getFieldsWithoutIdField(fields);

        return Arrays.stream(fieldsWithoutIdField).map(Field::getName).toArray(String[]::new);
    }

    private String buildUpdateSql(Object object) throws IllegalAccessException {
        String tableName = getTableName(object.getClass());
        String[] columnNames = getNonPrimaryKeyColumns(object.getClass().getDeclaredFields());
        String[] patterns = Arrays.stream(columnNames).map(s -> String.format("%s = ?", s)).toArray(String[]::new);
        String columns = String.join(", ", patterns);
        String primaryKeyName = getFieldWithIdAnnotation(object).getName();
        String updateSql = String.format(SQL_UPDATE_PATTERN, tableName, columns, primaryKeyName);
        log.info("UPDATE SQL: {}", updateSql);

        return updateSql;
    }

    private IdFieldDto<Long> getFieldWithIdAnnotation(Object object) throws IllegalAccessException {
        Field field = getFieldWithIdAnnotation(object.getClass().getDeclaredFields())
                .orElseThrow(() -> new JdbcTemplateException("object has no ID annotations"));

        return new IdFieldDto(field, field.getName(), field.get(object));
    }

    private IdFieldDto<Long> getFieldWithIdAnnotation(Class clazz) throws IllegalAccessException {
        Field field = getFieldWithIdAnnotation(clazz.getDeclaredFields())
                .orElseThrow(() -> new JdbcTemplateException("object has no ID annotations"));

        return new IdFieldDto(field, field.getName(), null);
    }

    private Optional<Field> getFieldWithIdAnnotation(Field[] fields) {
        Field fieldId = null;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                fieldId = field;
            }
        }
        return Optional.ofNullable(fieldId);
    }

    private void checkObject(Object object) {
        if (object == null) {
            throw new NoSuchElementException("unable to build insert statement, object is null");
        }
        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length == 0) {
            throw new NoSuchElementException("Object has no fields to insert");
        }

        if (!hasIdAnnotation(fields)) {
            throw new JdbcTemplateException("Object shall have 1 field marked with id annotation");
        }
    }

    private boolean hasIdAnnotation(Field[] fields) {
        return getFieldWithIdAnnotation(fields).isPresent();
    }

    private void setColumnsValues(Object object, PreparedStatement pst, Field[] fields) throws IllegalAccessException, SQLException {
        for (int i = 1; i <= fields.length; i++) {
            Field field = fields[i - 1];
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            pst.setObject(i, fieldValue);
        }
    }

    private Field[] getFieldsWithoutIdField(Field[] fields) {
        return Arrays.stream(fields)
                .filter(s -> !s.isAnnotationPresent(Id.class))
                .toArray(Field[]::new);
    }

    private String getTableName(Class clazz) {
        return clazz.getSimpleName().toUpperCase();
    }

    @AllArgsConstructor
    @Data
    private static class IdFieldDto<T> {
        private Field field;
        private String name;
        private T value;
    }
}
