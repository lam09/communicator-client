package reflection;



import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectMain {

    public static void main(String[]args)
    {
        Class<?>clazz = StringGenerate.class;
        String[] params = {"Monday","Tuesday","Wenddays","Thursday"};
        Class[] paramClasses = new Class[params.length];
        for(int i=0;i<paramClasses.length;i++) paramClasses[i] = params[i].getClass();
        try {
            Constructor<?> constructor = clazz.getConstructor(params.getClass());

            StringGenerate generate= (StringGenerate) constructor.newInstance((Object) params); // (Object) is important
          //  generate = new StringGenerate("Monday","Tuesday","Wenddays","Thursday");
            Method printMethod=clazz.getMethod("println", String.class);
            printMethod.invoke(generate,"Hello world");

            generate.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
