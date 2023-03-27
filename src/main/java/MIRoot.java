import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class MIRoot {

    private static Deque<Class<?>> classesQueue;

    public MIRoot() {
        if (classesQueue == null) {
            classesQueue = new ArrayDeque<>();
        }
    }

    public Object callNextMethod(String methodName, Object... arguments) throws NoSuchMethodException, MultipleInheritanceException {
        Class<?>[] parents = getParents(this.getClass());
        if (parents == null) {
            throw new MultipleInheritanceException("No parent class to call for next method.");
        }

        addSuperClassesToQueue(parents);


        while (!classesQueue.isEmpty()) {
            if (hasNextMethod(methodName, arguments)) {
                Object res = invokeNextMethod(methodName, arguments);
                classesQueue = new ArrayDeque<>();
                return res;
            }
        }

        classesQueue = new ArrayDeque<>();
        Class<?>[] argumentTypes = getArgumentTypes(arguments);
        throw new NoSuchMethodException("No method \"" + methodName + "(" + Arrays.toString(argumentTypes) + ")\" in hierarchy!");
    }

    private void addSuperClassesToQueue(Class<?>[] parents) {
        for (Class<?> parent : parents) {
            if (!classesQueue.contains(parent)) {
                classesQueue.add(parent);
            }
        }
    }

    private Class<?>[] getParents(Class<?> c) {
        if (c.getAnnotations().length == 0 ||
                c.getAnnotation(Extends.class) == null ||
                c.getAnnotation(Extends.class).parents() == null ||
                c.getAnnotation(Extends.class).parents().length == 0) {
            return null;
        }
        return c.getAnnotation(Extends.class).parents();
    }

    private Class<?>[] getArgumentTypes(Object... arguments) {
        Class<?>[] argumentTypes = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            argumentTypes[i] = arguments[i].getClass();
        }
        return argumentTypes;
    }

    private boolean hasNextMethod(String methodName, Object... arguments) {
        if (classesQueue.isEmpty()) {
            return false;
        }

        Class<?> parent = classesQueue.peek();

        Class<?>[] argumentTypes = getArgumentTypes(arguments);

        try {
            parent.getMethod(methodName, argumentTypes);
        } catch (NoSuchMethodException e) {
            classesQueue.pop();

            Class<?>[] parents = getParents(parent);
            if (parents != null) {
                addSuperClassesToQueue(parents);
            }

            return false;
        }
        return true;
    }

    private Object invokeNextMethod(String methodName, Object... arguments) {
        Class<?>[] argumentTypes = getArgumentTypes(arguments);
        Class<?> parent = classesQueue.pop();

        Method method;
        try {
            method = parent.getMethod(methodName, argumentTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }

        try {
            Object o;
            Constructor<?> constructor;
            constructor = parent.getDeclaredConstructor();
            o = constructor.newInstance();
            return method.invoke(o, arguments);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException();
        }
    }

}
