import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class MIRoot {
    /**
     * responsible for the order of traversing classes
     */
    private static Deque<Class<?>> classesQueue;

    /**
     * Constructor
     * creates a new classesQueue if it's null
     */
    public MIRoot() {
        if (classesQueue == null) {
            classesQueue = new ArrayDeque<>();
        }
    }

    /**
     * @param arguments Arguments of the function for which call-next-method is called
     * @return the result of the call-next-method
     * @throws NoSuchMethodException if there is no such method for classes higher in the hierarchy
     * @throws MIHierarchyException  if there is no parent class to call for next method
     */
    public Object callNextMethod(Object... arguments) throws NoSuchMethodException, MIHierarchyException {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Class<?>[] parents = getParents(this.getClass());
        if (parents == null) {
            if (classesQueue.isEmpty()) {
                throw new MIHierarchyException("No parent class to call for next method.");
            }
        } else {
            addSuperClassesToQueue(parents);
        }

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

    /**
     * @param parents Classes to be added to classesQueue
     */
    private void addSuperClassesToQueue(Class<?>[] parents) {
        for (Class<?> parent : parents) {
            if (!classesQueue.contains(parent)) {
                classesQueue.add(parent);
            }
        }
    }

    /**
     * @param c
     * @return the parents of class c
     */
    private Class<?>[] getParents(Class<?> c) {
        if (c.getAnnotations().length == 0 ||
                c.getAnnotation(Extends.class) == null ||
                c.getAnnotation(Extends.class).parents() == null ||
                c.getAnnotation(Extends.class).parents().length == 0) {
            return null;
        }
        return c.getAnnotation(Extends.class).parents();
    }

    /**
     * @param arguments
     * @return an array with types for each argument
     */
    private Class<?>[] getArgumentTypes(Object... arguments) {
        Class<?>[] argumentTypes = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            argumentTypes[i] = arguments[i].getClass();
        }
        return argumentTypes;
    }

    /**
     * @param methodName Method name
     * @param arguments  Arguments with which this method is called
     * @return true if the head element of the classesQueue has this method, otherwise false
     * if the head element of the queue does not have this method, removes it from the queue
     */
    private boolean hasNextMethod(String methodName, Object... arguments) {
        if (classesQueue.isEmpty()) {
            return false;
        }

        Class<?> parent = classesQueue.peek();

        Class<?>[] argumentTypes = getArgumentTypes(arguments);

        try {
            parent.getDeclaredMethod(methodName, argumentTypes);
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

    /**
     * @param methodName Method name
     * @param arguments  Arguments with which this method is called
     * @return the result of executing this method
     * here the head element of the classesQueue is the nearest parent that has this method
     */
    private Object invokeNextMethod(String methodName, Object... arguments) {
        Class<?>[] argumentTypes = getArgumentTypes(arguments);
        Class<?> parent = classesQueue.pop();

        Method method;
        try {
            method = parent.getDeclaredMethod(methodName, argumentTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }

        try {
            Constructor<?> constructor = parent.getDeclaredConstructor();
            Object o = constructor.newInstance();
            return method.invoke(o, arguments);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException();
        }
    }

}
