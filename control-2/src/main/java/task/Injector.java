package task;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Injector {

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws
            ImplementationNotFoundException,
            InjectionCycleException, ClassNotFoundException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException {
        Class cl;

        HashMap<Class, Object> objects = new HashMap<Class, Object>();
//        if(implementationClassNames.contains(rootClassName)) {
//            throw new InjectionCycleException();
//        }

        cl = Class.forName(rootClassName); // no except guarantee

        Constructor cons = cl.getConstructors()[0];
        Class[] params = cons.getParameterTypes();

        ArrayList<Object> args = new ArrayList<Object>();
        for (Class param : params) {
            if (objects.containsKey(param)) {
                args.add(objects.get(param));
            }
            else {
                String name = param.getCanonicalName();
//                Object impl = getImpl(name, implementationClassNames);
                objects.put(param, initialize(name, implementationClassNames));
                args.add(objects.get(param));
            }
        }

        if(args.size() == 0) {
            return cons.newInstance();
        }

        return cons.newInstance(args.toArray());
    }

    private static Object getImpl(String name, List<String> implementationClassNames) throws
            ImplementationNotFoundException {
        Class cl;
        try {
            cl = Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new ImplementationNotFoundException();
        }
        return null;
    }
}