import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MIRoot {

    public void callNextMethod(String methodName, Object... arguments) {
        if (this.getClass().getAnnotations().length == 0 ||
                this.getClass().getAnnotation(Extends.class) == null) {
            return;
        }

        Class<?>[] parents = this.getClass().getAnnotation(Extends.class).parents();
        Class<?>[] argumentTypes = new Class[arguments.length];

        for (Class<?> parent : parents) {
            Method method;
            try {
                method = parent.getMethod(methodName, argumentTypes);
            } catch (NoSuchMethodException e) {
                continue;
            }
            Object o = null;
            Constructor<?> constructor;
            try {
                 constructor = parent.getDeclaredConstructor();
            }  catch (NoSuchMethodException ex) {
                System.out.println("No constructor");
                throw new RuntimeException(ex);
            }
            try {
                o = constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            try {
                method.invoke(o, arguments);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
