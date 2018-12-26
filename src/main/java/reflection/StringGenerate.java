package reflection;

import java.util.ArrayList;

public class StringGenerate {
    ArrayList<String> list;
  /*  public StringGenerate(String s1,String s2)
    {
        list = new ArrayList<String>();
        list.add(s1);
        list.add(s2);
    }
    public StringGenerate(String s1,String s2,String s3)
    {
        list = new ArrayList<String>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
    }*/
 /*   public StringGenerate(String s1,String s2,String s3,String s4)
    {
        list = new ArrayList<String>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);
    }*/
    public StringGenerate(String... params){
        list = new ArrayList<String>();
        for(String s:params)
        list.add(s);
    }

    public void print()
    {
        for(String s:list)
            System.out.print("  " + s);
    }
    public void println(String text){
        System.out.println(text);

    }
}
