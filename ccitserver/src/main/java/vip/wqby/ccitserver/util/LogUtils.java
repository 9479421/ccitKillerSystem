package vip.wqby.ccitserver.util;

public class LogUtils {
    public static void info(String ...str){
        for (String s :str){
            System.out.print(s+"|");
        }
        System.out.println();
    }
}
