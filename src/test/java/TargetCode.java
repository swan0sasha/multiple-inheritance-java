public class TargetCode {

    public class A extends MIRoot {
        public void print() {
            System.out.println("Print from A");
        }
    }

    @Extends(parents = A.class)
    public class B extends MIRoot {
        public void print() {
            System.out.println("Print from B");
            callNextMethod("print");
        }
    }

    @Extends(parents = A.class)
    public class C extends MIRoot {
        public void print() {
            System.out.println("Print from C");
        }
    }

    public class E extends MIRoot {
    }

    @Extends(parents = {B.class, C.class})
    public static class D extends MIRoot {
        public void print() {
            System.out.println("Print from D");
            callNextMethod("print");
        }
    }

    public static void main(String[] args) {
        D d = new D();
        d.print();
    }
}
