package jrails;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Model {
    private static final Map<Class<?>, List<Object>> DATABASE = new HashMap<>();
    private static final Map<Class<?>, AtomicInteger> ID_GENERATORS = new HashMap<>();
    private int id = 0;

    public void save() {
        Class<?> clazz = this.getClass();

        // Initialize database and ID generator for this class if needed
        DATABASE.putIfAbsent(clazz, new ArrayList<>());
        ID_GENERATORS.putIfAbsent(clazz, new AtomicInteger(1));

        List<Object> records = DATABASE.get(clazz);

        if (id == 0) {
            // Assign a new ID and save
            id = ID_GENERATORS.get(clazz).getAndIncrement();
            records.add(this);
        } else {
            // Update existing record
            boolean updated = false;
            for (int i = 0; i < records.size(); i++) {
                Model record = (Model) records.get(i);
                if (record.id() == this.id) {
                    records.set(i, this);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                throw new IllegalStateException("Attempted to update a non-existing record");
            }
        }

        // Save to disk
        saveToFile(clazz, records);
    }

    public int id() {
        return id;
    }

    public static <T> T find(Class<T> clazz, int id) {
        List<Object> records = DATABASE.getOrDefault(clazz, Collections.emptyList());
        for (Object record : records) {
            Model model = (Model) record;
            if (model.id() == id) {
                return clazz.cast(record);
            }
        }
        return null;
    }

    public static <T> List<T> all(Class<T> clazz) {
        List<Object> records = DATABASE.getOrDefault(clazz, Collections.emptyList());
        List<T> result = new ArrayList<>();
        for (Object record : records) {
            result.add(clazz.cast(record));
        }
        return result;
    }

    public void destroy() {
        Class<?> clazz = this.getClass();
        List<Object> records = DATABASE.getOrDefault(clazz, new ArrayList<>());

        if (id == 0) {
            throw new IllegalStateException("Cannot destroy a record that was never saved");
        }

        records.removeIf(record -> ((Model) record).id() == this.id);
        saveToFile(clazz, records);
    }

    public static void reset() {
        DATABASE.clear();
        ID_GENERATORS.clear();
    }

    private static void saveToFile(Class<?> clazz, List<Object> records) {
        String filename = clazz.getSimpleName() + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Object record : records) {
                Model model = (Model) record;
                List<String> values = new ArrayList<>();
                for (Field field : clazz.getFields()) {
                    if (field.isAnnotationPresent(Column.class)) {
                        Object value = field.get(model);
                        values.add(value == null ? "null" : escape(value.toString()));
                    }
                }
                writer.write(model.id + "," + String.join(",", values));
                writer.newLine();
            }
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("Error saving to file", e);
        }
    }

    private static String escape(String value) {
        return value.replace(",", "\\,");
    }
}
