import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

public class MIRoot {

    private static Deque<Class<?>> classesQueue;

    public MIRoot() {
        if (classesQueue == null) {
            classesQueue = new ArrayDeque<>();
        }
    }

    public void callNextMethod(String methodName, Object... arguments) {

        if (this.getClass().getAnnotations().length == 0 ||
                this.getClass().getAnnotation(Extends.class) == null) {
            return;
        }

        Class<?>[] parents = this.getClass().getAnnotation(Extends.class).parents();
        Collections.addAll(classesQueue, parents);

        while (!classesQueue.isEmpty() && !invokeNextMethod(methodName, arguments)) {
        }

        classesQueue = new ArrayDeque<>();
    }

    private boolean invokeNextMethod(String methodName, Object... arguments) {
        Class<?>[] argumentTypes = new Class[arguments.length];
        Class<?> parent = classesQueue.pop();

        Method method;
        try {
            method = parent.getMethod(methodName, argumentTypes);
        } catch (NoSuchMethodException e) {
            return false;
        }

        try {
            Object o;
            Constructor<?> constructor;
            constructor = parent.getDeclaredConstructor();
            o = constructor.newInstance();
            method.invoke(o, arguments);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException();
        }

        return true;
    }


}
