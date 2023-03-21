import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MIRootTest {

    @Test
    public void callNextMethod() throws NoSuchMethodException, MultipleInheritanceException {
        TestClasses.H h = new TestClasses.H();
        Assertions.assertEquals("HG", h.name());
    }

    @Test
    public void multipleParentMethods() throws NoSuchMethodException, MultipleInheritanceException {
        TestClasses.D d = new TestClasses.D();
        Assertions.assertEquals("DBC", d.name());
    }

    @Test
    public void callParentMethod() throws NoSuchMethodException, MultipleInheritanceException {
        TestClasses.D d = new TestClasses.D();
        Assertions.assertEquals("BC", d.callNextMethod("name"));
    }

    @Test
    public void callAncestorMethod() throws NoSuchMethodException, MultipleInheritanceException {
        TestClasses.D d = new TestClasses.D();
        Assertions.assertEquals("A", d.callNextMethod("grandparentName"));
    }

    @Test
    public void callNonexistentMethod() {
        TestClasses.D d = new TestClasses.D();
        Assertions.assertThrows(NoSuchMethodException.class, () -> d.callNextMethod("nonexistent"));
    }

    @Test
    public void notAnnotatedClass() {
        TestClasses.A a = new TestClasses.A();
        Assertions.assertThrows(MultipleInheritanceException.class, () -> a.callNextMethod("name"));
    }

    @Test
    public void noParentsClass() {
        TestClasses.G g = new TestClasses.G();
        Assertions.assertThrows(MultipleInheritanceException.class, () -> g.callNextMethod("name"));
    }


}
