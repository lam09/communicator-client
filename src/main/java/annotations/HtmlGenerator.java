package annotations;

import java.lang.reflect.Method;

public class HtmlGenerator {

    public void generate()
    {
        Class<?> clazz=DocumentClass.class;

        boolean isHtmlUL = clazz.isAnnotationPresent(AnnHtmlUL.class);

        StringBuilder sb = new StringBuilder();
        if(isHtmlUL)
        {
            AnnHtmlUL annUL = clazz.getAnnotation(AnnHtmlUL.class);
            sb.append("<H3>"+clazz.getName()+"</H3>");
            sb.append("\n");

            String border = annUL.border();
            sb.append("<UL style='border:"+border+">");
            sb.append("\n");

            Method[] methods = clazz.getMethods();
            for(Method method:methods)
            {
                if(method.isAnnotationPresent(AnnHtmlUI.class)){
                    AnnHtmlUI annUI = method.getAnnotation(AnnHtmlUI.class);
                    String background = annUI.background();
                    String color=annUI.color();
                    sb.append("<LI style='margin:5px;padding:5px;background:" + background + ";color:" + color + "'>");
                    sb.append("\n");
                    sb.append(method.getName());
                    sb.append("\n");
                    sb.append("</LI>");
                    sb.append("\n");
                }
            }
            sb.append("</UL>");
        }
        writeToFile(clazz.getSimpleName() + ".html", sb);
    }
    // Ghi các thông tin ra màn hình Console (Hoặc file).
    private static void writeToFile(String fileName, StringBuilder sb) {
        System.out.println(sb);
    }

    public static void main(String[]args)
    {
        HtmlGenerator generator = new HtmlGenerator();
        generator.generate();
    }
}
