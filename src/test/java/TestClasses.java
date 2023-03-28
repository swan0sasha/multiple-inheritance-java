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

    public interface Root {
        String name();

        default String grandparentName() {
            return "Default grandparentName";
        }

        default String string(String s) {
            return ("Default" + s);
        }
    }

    public static class A extends MIRoot implements Root {
        public String name() {
            try {
                return "A".concat((String) this.callNextMethod());
            } catch (NoSuchMethodException | MIHierarchyException e) {
                throw new RuntimeException(e);
            }
        }

        public String grandparentName() {
            return "A";
        }
    }

    @Extends(parents = {})
    public static class G extends MIRoot implements Root {
        public String name() {
            return "G";
        }
    }

    @Extends(parents = G.class)
    public static class H extends MIRoot implements Root {
        public String name() {
            try {
                return "H".concat((String) callNextMethod());
            } catch (NoSuchMethodException | MIHierarchyException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Extends(parents = {A.class, H.class})
    public static class B extends MIRoot implements Root {
        public String name() {
            try {
                return "B".concat((String) callNextMethod());
            } catch (NoSuchMethodException | MIHierarchyException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Extends(parents = H.class)
    public static class C extends MIRoot implements Root {
        public String name() {
            return "C";
        }

        public String string(String s) {
            return "C".concat(s);
        }
    }

    @Extends(parents = {B.class, C.class})
    public static class D extends MIRoot implements Root {
        public String name() {
            try {
                return "D".concat((String) callNextMethod());
            } catch (NoSuchMethodException | MIHierarchyException e) {
                throw new RuntimeException(e);
            }
        }

        public String string(String s) {
            try {
                return (String) callNextMethod(s);
            } catch (NoSuchMethodException | MIHierarchyException e) {
                throw new RuntimeException(e);
            }
        }

        public String string() throws NoSuchMethodException, MIHierarchyException {
            return (String) callNextMethod();
        }

        public Object methodD() throws MIHierarchyException, NoSuchMethodException {
            return callNextMethod();
        }
    }
}
