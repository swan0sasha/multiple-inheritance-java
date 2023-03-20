public class TargetCode {

    public static class A extends MIRoot {
        public void print() {
            System.out.println("\nPrint from A");
        }
    }

    @Extends(parents = A.class)
    public static class B extends MIRoot {
        public void print() {
            System.out.println("\nPrint from B");
            callNextMethod("print");
        }
    }

    @Extends(parents = A.class)
    public static class C extends MIRoot {
        public void print() {
            System.out.println("\nPrint from C");
        }
    }

    public static class E extends MIRoot {
    }

    @Extends(parents = {B.class, C.class})
    public static class D extends MIRoot {
        public void print() {
            System.out.println("\nPrint from D");
            callNextMethod("print");
        }

        public void simplePrint() {
            System.out.println("\nPrint from D");
        }
    }

    public static void main(String[] args) {
        D d = new D();
        d.print();
    }
}
