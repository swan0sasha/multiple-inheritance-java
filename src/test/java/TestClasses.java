public class TestClasses {

    /*

           G
           |
           |
    A      H
    |   /  |
    |  /   |
    B      C
    \      /
     \    /
       D

     */

    public static class A extends MIRoot {
        public String name() {
            return "A";
        }

        public String grandparentName() {
            return "A";
        }
    }

    @Extends(parents = {})
    public static class G extends MIRoot {
        public String name() {
            return "G";
        }
    }

    @Extends(parents = G.class)
    public static class H extends MIRoot {
        public String name() throws NoSuchMethodException, MultipleInheritanceException {
            return "H".concat((String) callNextMethod("name"));
        }
    }

    @Extends(parents = {A.class, H.class})
    public static class B extends MIRoot {
        public String name() throws NoSuchMethodException, MultipleInheritanceException {
            return "B".concat((String) callNextMethod("name"));
        }
    }

    @Extends(parents = H.class)
    public static class C extends MIRoot {
        public String name() {
            return "C";
        }

        public String string(String s) {
            return "C".concat(s);
        }
    }

    @Extends(parents = {B.class, C.class})
    public static class D extends MIRoot {
        public String name() throws NoSuchMethodException, MultipleInheritanceException {
            return "D".concat((String) callNextMethod("name"));
        }
    }

}
