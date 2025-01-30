import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;

public class Unit {

    public static Map<String, Throwable> testClass(String name) {
        Map<String, Throwable> results = new HashMap<>();
        try {
            Class<?> clazz = Class.forName(name);
            Object instance = clazz.getDeclaredConstructor().newInstance();

            List<Method> beforeClassMethods = new ArrayList<>();
            List<Method> afterClassMethods = new ArrayList<>();
            List<Method> beforeMethods = new ArrayList<>();
            List<Method> afterMethods = new ArrayList<>();
            List<Method> testMethods = new ArrayList<>();

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(BeforeClass.class)) {
                    if (!Modifier.isStatic(method.getModifiers())) {
                        throw new Exception("BeforeClass methods must be static");
                    }
                    beforeClassMethods.add(method);
                } else if (method.isAnnotationPresent(AfterClass.class)) {
                    if (!Modifier.isStatic(method.getModifiers())) {
                        throw new Exception("AfterClass methods must be static");
                    }
                    afterClassMethods.add(method);
                } else if (method.isAnnotationPresent(Before.class)) {
                    beforeMethods.add(method);
                } else if (method.isAnnotationPresent(After.class)) {
                    afterMethods.add(method);
                } else if (method.isAnnotationPresent(Test.class)) {
                    testMethods.add(method);
                }
            }

            Collections.sort(beforeClassMethods, Comparator.comparing(Method::getName));
            Collections.sort(afterClassMethods, Comparator.comparing(Method::getName));
            Collections.sort(beforeMethods, Comparator.comparing(Method::getName));
            Collections.sort(afterMethods, Comparator.comparing(Method::getName));
            Collections.sort(testMethods, Comparator.comparing(Method::getName));

            for (Method method : beforeClassMethods) method.invoke(null);

            for (Method test : testMethods) {
                for (Method before : beforeMethods) before.invoke(instance);

                try {
                    test.invoke(instance);
                    results.put(test.getName(), null);
                } catch (InvocationTargetException e) {
                    results.put(test.getName(), e.getTargetException());
                }

                for (Method after : afterMethods) after.invoke(instance);
            }

            for (Method method : afterClassMethods) method.invoke(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    private static final int MAX_COMBINATIONS = 100; // Cap on the number of generated combinations

    public static Map<String, Object[]> quickCheckClass(String name) {
        Map<String, Object[]> results = new TreeMap<>();

        try {
            Class<?> clazz = Class.forName(name);
            Object instance = clazz.getConstructor().newInstance();

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Property.class)) {
                    String methodName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Annotation[][] parameterAnnotations = method.getParameterAnnotations();

                    List<Object[]> argumentCombinations = generateArgumentCombinations(instance, parameterTypes, parameterAnnotations);
                    boolean passedAll = true;

                    for (Object[] args : argumentCombinations) {
                        try {
                            boolean result = (boolean) method.invoke(instance, args);
                            if (!result) {
                                results.put(methodName, args);
                                passedAll = false;
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            results.put(methodName, args);
                            passedAll = false;
                            break;
                        }
                    }
                    if (passedAll) {
                        results.put(methodName, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    private static List<Object[]> generateArgumentCombinations(Object instance, Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
        List<Object[]> argumentCombinations = new ArrayList<>();
        List<List<Object>> arguments = new ArrayList<>();

        for (int i = 0; i < parameterTypes.length; i++) {
            Annotation annotation = parameterAnnotations[i][0];
            Class<?> type = parameterTypes[i];

            try {
                if (type == Integer.class && annotation instanceof IntRange) {
                    IntRange intRange = (IntRange) annotation;
                    List<Object> values = new ArrayList<>();
                    for (int j = intRange.min(); j <= intRange.max(); j++) {
                        values.add(j);
                    }
                    arguments.add(values);
                } else if (type == String.class && annotation instanceof StringSet) {
                    StringSet stringSet = (StringSet) annotation;
                    arguments.add(Arrays.asList(stringSet.strings()));
                } else if (type == List.class && annotation instanceof ListLength) {
                    List<Object[]> listArgs = generateNestedLists(instance, parameterAnnotations[i], (ListLength) annotation);
                    arguments.add(Arrays.asList(listArgs));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        iterativeCartesianProduct(arguments, argumentCombinations);
        return limitCombinations(argumentCombinations);
    }

    private static List<Object[]> limitCombinations(List<Object[]> combinations) {
        if (combinations.size() > MAX_COMBINATIONS) {
            return combinations.subList(0, MAX_COMBINATIONS);
        }
        return combinations;
    }

    private static List<Object[]> generateNestedLists(Object instance, Annotation[] typeAnnotations, ListLength listLength) {
        List<Object[]> listArgs = new ArrayList<>();
        for (int length = listLength.min(); length <= listLength.max(); length++) {
            List<List<Object>> elementsList = new ArrayList<>();

            for (int i = 0; i < length; i++) {
                elementsList.add(generateElementValues(instance, typeAnnotations));
            }
            iterativeCartesianProduct(elementsList, listArgs);
        }
        return listArgs;
    }

    private static List<Object> generateElementValues(Object instance, Annotation[] annotations) {
        List<Object> values = new ArrayList<>();

        for (Annotation annotation : annotations) {
            if (annotation instanceof IntRange) {
                IntRange intRange = (IntRange) annotation;
                for (int i = intRange.min(); i <= intRange.max(); i++) {
                    values.add(i);
                }
            } else if (annotation instanceof StringSet) {
                values.addAll(Arrays.asList(((StringSet) annotation).strings()));
            }
        }

        return values;
    }

    private static void iterativeCartesianProduct(List<List<Object>> lists, List<Object[]> result) {
        int[] indices = new int[lists.size()];

        while (true) {
            List<Object> current = new ArrayList<>();
            for (int i = 0; i < lists.size(); i++) {
                current.add(lists.get(i).get(indices[i]));
            }
            result.add(current.toArray(new Object[0]));

            // Move to the next combination
            for (int i = lists.size() - 1; i >= 0; i--) {
                if (indices[i] < lists.get(i).size() - 1) {
                    indices[i]++;
                    break;
                } else {
                    if (i == 0) return; // If we are at the last index and cannot increment, we are done
                    indices[i] = 0;
                }
            }
        }
    }
}