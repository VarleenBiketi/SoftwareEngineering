package jrails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ModelTest {
    @Before
    public void setUp() {
        Model.reset(); // Clear database before each test
    }

    @After
    public void tearDown() {
        Model.reset(); // Clear database after each test
    }

    @Test
    public void testSaveAndFind() {
        TestModel m = new TestModel();
        m.name = "Test";
        m.value = 42;
        m.save();

        TestModel retrieved = Model.find(TestModel.class, m.id());
        assertNotNull(retrieved);
        assertEquals("Test", retrieved.name);
        assertEquals(42, retrieved.value);
    }

    @Test
    public void testAll() {
        TestModel m1 = new TestModel();
        m1.name = "First";
        m1.value = 1;
        m1.save();

        TestModel m2 = new TestModel();
        m2.name = "Second";
        m2.value = 2;
        m2.save();

        List<TestModel> allModels = Model.all(TestModel.class);
        assertEquals(2, allModels.size());
    }

    @Test
    public void testDestroy() {
        TestModel m = new TestModel();
        m.name = "DestroyMe";
        m.value = 100;
        m.save();

        m.destroy();
        assertNull(Model.find(TestModel.class, m.id()));
    }
}

class TestModel extends Model {
    @Column public String name;
    @Column public int value;
}
