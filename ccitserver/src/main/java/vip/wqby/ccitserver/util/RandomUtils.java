package vip.wqby.ccitserver.util;

import java.util.Random;

public class RandomUtils {
    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String randomCardId(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i=0 ;i < 18 ; i++){
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }
}
