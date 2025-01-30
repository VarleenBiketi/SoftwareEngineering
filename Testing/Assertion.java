// Assertion.java
public class Assertion {
    static ObjectAssertion assertThat(Object o) {
        return new ObjectAssertion(o);
    }

    static StringAssertion assertThat(String s) {
        return new StringAssertion(s);
    }

    static BooleanAssertion assertThat(boolean b) {
        return new BooleanAssertion(b);
    }

    static IntAssertion assertThat(int i) {
        return new IntAssertion(i);
    }
}

// ObjectAssertion.java
class ObjectAssertion {
    private final Object actual;

    public ObjectAssertion(Object actual) {
        this.actual = actual;
    }

    public ObjectAssertion isNotNull() {
        if (actual == null) {
            throw new IllegalArgumentException("Expected object to be not null");
        }
        return this;
    }

    public ObjectAssertion isNull() {
        if (actual != null) {
            throw new IllegalArgumentException("Expected object to be null");
        }
        return this;
    }

    public ObjectAssertion isEqualTo(Object o2) {
        if (!actual.equals(o2)) {
            throw new IllegalArgumentException("Expected objects to be equal");
        }
        return this;
    }

    public ObjectAssertion isNotEqualTo(Object o2) {
        if (actual.equals(o2)) {
            throw new IllegalArgumentException("Expected objects to be not equal");
        }
        return this;
    }

    public ObjectAssertion isInstanceOf(Class<?> c) {
        if (!c.isInstance(actual)) {
            throw new IllegalArgumentException("Expected object to be an instance of " + c.getName());
        }
        return this;
    }
}

// StringAssertion.java
class StringAssertion {
    private final String actual;

    public StringAssertion(String actual) {
        this.actual = actual;
    }

    public StringAssertion isNotNull() {
        if (actual == null) {
            throw new IllegalArgumentException("Expected string to be not null");
        }
        return this;
    }

    public StringAssertion isNull() {
        if (actual != null) {
            throw new IllegalArgumentException("Expected string to be null");
        }
        return this;
    }

    public StringAssertion isEqualTo(Object o) {
        if (!actual.equals(o)) {
            throw new IllegalArgumentException("Expected strings to be equal");
        }
        return this;
    }

    public StringAssertion isNotEqualTo(Object o) {
        if (actual.equals(o)) {
            throw new IllegalArgumentException("Expected strings to be not equal");
        }
        return this;
    }

    public StringAssertion startsWith(String prefix) {
        if (!actual.startsWith(prefix)) {
            throw new IllegalArgumentException("Expected string to start with " + prefix);
        }
        return this;
    }

    public StringAssertion isEmpty() {
        if (!actual.isEmpty()) {
            throw new IllegalArgumentException("Expected string to be empty");
        }
        return this;
    }

    public StringAssertion contains(String substring) {
        if (!actual.contains(substring)) {
            throw new IllegalArgumentException("Expected string to contain " + substring);
        }
        return this;
    }
}

// BooleanAssertion.java
class BooleanAssertion {
    private final boolean actual;

    public BooleanAssertion(boolean actual) {
        this.actual = actual;
    }

    public BooleanAssertion isEqualTo(boolean b2) {
        if (actual != b2) {
            throw new IllegalArgumentException("Expected booleans to be equal");
        }
        return this;
    }

    public BooleanAssertion isTrue() {
        if (!actual) {
            throw new IllegalArgumentException("Expected boolean to be true");
        }
        return this;
    }

    public BooleanAssertion isFalse() {
        if (actual) {
            throw new IllegalArgumentException("Expected boolean to be false");
        }
        return this;
    }
}

// IntAssertion.java
class IntAssertion {
    private final int actual;

    public IntAssertion(int actual) {
        this.actual = actual;
    }

    public IntAssertion isEqualTo(int i2) {
        if (actual != i2) {
            throw new IllegalArgumentException("Expected integers to be equal");
        }
        return this;
    }

    public IntAssertion isLessThan(int i2) {
        if (actual >= i2) {
            throw new IllegalArgumentException("Expected " + actual + " to be less than " + i2);
        }
        return this;
    }

    public IntAssertion isGreaterThan(int i2) {
        if (actual <= i2) {
            throw new IllegalArgumentException("Expected " + actual + " to be greater than " + i2);
        }
        return this;
    }
}
