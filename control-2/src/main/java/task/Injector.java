package task;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@SuppressWarnings("WeakerAccess")
public class Injector {

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws
            Exception {
        Class cl = Class.forName(rootClassName);
        return initClass(cl, implementationClassNames, new HashSet<Object>());
    }

    private static Object initClass(Class cl, List<String> implNames, HashSet<Object> forInit) throws
            InjectionCycleException, IllegalAccessException, AmbiguousImplementationException,
            ImplementationNotFoundException, InvocationTargetException, InstantiationException {
        Class[] paramsTypes = cl.getConstructors()[0].getParameterTypes();

        if(forInit.contains(cl)) {
            throw new InjectionCycleException();
        }

        ArrayList<Object> args = new ArrayList<Object>();

        for(Class param : paramsTypes) {
            args.add(implNames.contains(param.getCanonicalName())
                    ? initClass(param, implNames, forInit)
                    : findUniqueImplementation(param, implNames, forInit));
        }

        Object obj;
        Constructor cons = cl.getConstructors()[0];
        if(args.size() == 0) {
            obj = cons.newInstance();
        }
        else{
            obj = cons.newInstance(args.toArray());
        }

        forInit.add(obj);
        return obj;
    }

    private static Object findUniqueImplementation(Class<?> cl, List<String> implNames, HashSet<Object> initialized)
            throws ImplementationNotFoundException, AmbiguousImplementationException,
            InjectionCycleException, InvocationTargetException, IllegalAccessException,
            InstantiationException {
        Class<?> result = null;
        for (String dependency : implNames) {
            Class der;
            try {
                der = Class.forName(dependency);
            } catch (ClassNotFoundException e) {
                throw new ImplementationNotFoundException();
            }
            if (cl.isAssignableFrom(der)) {
                if (result != null) {
                    throw new AmbiguousImplementationException();
                }

                result = der;
            }
        }

        if (result == null) {
            throw new ImplementationNotFoundException();
        }

        initialized.remove(cl.getCanonicalName());
        return initClass(result, implNames, initialized);
    }
}