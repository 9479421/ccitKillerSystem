package vip.wqby.ccitserver.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

public class TuJianUtils {
    public static String discernCaptcha(String base64Data) {
        try {
            String username = "9479421";
            String password = "BAye6666";
            String remark = "输出计算结果";
            Map<String, String> data = new HashMap<>();
            data.put("username", username);
            data.put("password", password);
            data.put("typeid", "3");
            data.put("remark", remark);
            data.put("image", base64Data);
            String resultString = Jsoup.connect("http://api.ttshitu.com/predict")
                    .requestBody(JSON.toJSONString(data))
                    .header("Content-Type", "application/json")
                    .ignoreContentType(true).timeout(120000).post().text()
                    .replace("\\\"", "\"")
                    .replace("\"{", "{")
                    .replace("}\"", "}");

            System.out.println("resultString"+resultString);
            JSONObject jsonObject = JSONObject.parseObject(resultString);
            if (jsonObject.getBoolean("success")) {
                String result = jsonObject.getJSONObject("data").getString("result");
                return result;
            } else {
                System.out.println("识别失败原因为:" + jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
