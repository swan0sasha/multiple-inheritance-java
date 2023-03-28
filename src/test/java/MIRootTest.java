import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MIRootTest {

    @Test
    public void callNextMethod() {
        TestClasses.H h = new TestClasses.H();
        Assertions.assertEquals("HG", h.name());
    }

    @Test
    public void multipleParentMethods() {
        TestClasses.B b = new TestClasses.B();
        Assertions.assertEquals("BAHG", b.name());
    }

    @Test
    public void notAnnotatedClass() {
        TestClasses.A a = new TestClasses.A();
        Assertions.assertThrows(MIHierarchyException.class, a::name);
    }

    @Test
    public void emptyParentsArrayClass() {
        TestClasses.G g = new TestClasses.G();
        Assertions.assertThrows(MIHierarchyException.class, g::parentName);
    }

    @Test
    public void callNonexistentMethod() {
        TestClasses.D d = new TestClasses.D();
        Assertions.assertThrows(NoSuchMethodException.class, d::methodD);
    }

    @Test
    public void methodWithArguments() {
        TestClasses.D d = new TestClasses.D();
        Assertions.assertEquals("Cd", d.string("d"));
    }

    @Test
    public void callNonexistentMethodWithArguments() {
        TestClasses.D d = new TestClasses.D();
        Assertions.assertThrows(NoSuchMethodException.class, d::string);
        //method "string" in class C needs one String argument
    }

    @Test
    public void callParentMethod() throws NoSuchMethodException, MIHierarchyException {
        TestClasses.D d = new TestClasses.D();
        Assertions.assertEquals("C", d.parentName());
    }

    @Test
    public void callAncestorMethod() throws NoSuchMethodException, MIHierarchyException {
        TestClasses.D d = new TestClasses.D();
        Assertions.assertEquals("A", d.grandparentName());
    }
}
