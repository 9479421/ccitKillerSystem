package vip.wqby.ccitserver.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SingleMessage;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import vip.wqby.ccitserver.config.SpringUtil;
import vip.wqby.ccitserver.pojo.cpDailyConfig;
import vip.wqby.ccitserver.pojo.task.emailTask;
import vip.wqby.ccitserver.pojo.task.noteTask;
import vip.wqby.ccitserver.pojo.task.qqMessageTask;
import vip.wqby.ccitserver.queue.AllQueue;
import vip.wqby.ccitserver.queue.cpDailyConsumer;
import vip.wqby.ccitserver.util.http.Http;
import vip.wqby.ccitserver.util.http.HttpResponse;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class cpDailyUtils {
    Http http = new Http();
    private JSONObject json = null;
    private HttpResponse response = null;
    private String location;
    private String phone = "";
    private String userAccount = "";
    private String name = "";
    private String taskName = "";
    private String taskTime = "";

    //等待设置
    public static String w_ip = "";
    public static int w_port;

    public static String w_username = "";
    public static String w_password = "";


    {
        http.AutoCookie();
        http.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1309.0 Safari/537.17");
        //用了代理IP 容易超时 暂时用不到20秒
        http.setTimeout(20000,20000);
        //设置代理ip

        if (w_username.equals("")) {
            http.setProxy(w_ip, w_port);
        }else{
            http.setProxy(w_ip, w_port,w_username,w_password);
        }
    }

//    http://webapi.http.zhimacangku.com/getip?num=1&type=2&pro=&city=0&yys=0&port=1&time=1&ts=0&ys=0&cs=0&lb=1&sb=1&pb=4&mr=1&regions=
    //代理ip
    public static void setProxy() {
        try {
            int flag = 0;
            //读取可用的代理ip为止
            while(true) {
                if(flag++>=5){
                    break;
                }
                //忽略所有异常，以找到能用的代理ip为目的
                try{
                    //获取ip并置入静态变量
                    Http http_proxy = new Http();
                    http_proxy.open("http://webapi.http.zhimacangku.com/getip?num=1&type=2&pro=&city=0&yys=0&port=1&ts=0&ys=0&cs=0&lb=1&sb=0&pb=4&mr=1&regions=");
                    HttpResponse httpResponse_proxy = http_proxy.get();
                    JSONObject json_proxy = JSONObject.parseObject(httpResponse_proxy.getBody());
                    System.out.println(json_proxy.toJSONString());
                    String ip = json_proxy.getJSONArray("data").getJSONObject(0).getString("ip");
                    int port = Integer.valueOf(json_proxy.getJSONArray("data").getJSONObject(0).getString("port"));
                    w_ip = ip;
                    w_port = port;

                    //测试是否可用
                    Http http_test = new Http();
                    http_test.setProxy(ip,port);
                    http_test.open("https://ccit.campusphere.net/portal/index.html");
                    HttpResponse httpResponse = http_test.get();
                    int ret = httpResponse.getBody().indexOf("登录");
                    if(ret!=-1){
                        break;
                    }

                    //提取代理ip有频率限制
                    Thread.sleep(3000);
                }catch (Exception E){

                }


            }

//            System.out.println(w_ip);
//            System.out.println(w_port);
        } catch (Exception e) {
            System.out.println("获取代理ip异常");
            e.printStackTrace();
        }

    }

    public static void setProxy(String ip, int port) {
        w_ip = ip;
        w_port = port;
    }

    public static void setProxy(String ip, int port,String username,String password) {
        w_ip = ip;
        w_port = port;
        w_username = username;
        w_password = password;
    }


    //password密码加密
    public String randString(int len) {
        final char[] baseString = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678".toCharArray();
        StringBuffer result = new StringBuffer();
        Random e = new Random();
        for (int i = 0; i < len; i++) {
            result.append(baseString[e.nextInt(baseString.length)]);
        }
        return result.toString();
    }

    public String encrypt_password(String pwd, String key) {
        final int randStrlen = 64;
        final int randIvLen = 16;
        try {
            String ranStr = randString(randStrlen);
            String ivStr = randString(randIvLen);
            String data = ranStr + pwd;
            int needChr = 16 - (data.length() % 16);
            needChr = needChr == 0 ? 16 : needChr;
            for (int i = 0; i < needChr; i++) {
                data += (char) needChr;
            }
            return EncryptUtils.encryptAESNoPadding(data, key, ivStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //两个提交打卡的时候的AES加密
    //Extension
    public String encrypt_CpdailyExtension(String text) {
        try {
            return EncryptUtils.encryptDESPKCS5Padding(text, "XCE927==", new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //bodyString
    public String encrypt_BodyString(String text) {
        try {
            return EncryptUtils.encryptAESPKCS5Padding(text, "SASEoK4Pa5d4SssO", new byte[]{1, 2, 3, 4, 5, 6, 7, 8, '\t', 1, 2, 3, 4, 5, 6, 7});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //sign MD5加密参数
    public String getSign(LinkedHashMap<String, Object> map) {
        try {
            List<String> abstractKey = new LinkedList<>();
            abstractKey.add("appVersion");
            abstractKey.add("bodyString");
            abstractKey.add("deviceId");
            abstractKey.add("lat");
            abstractKey.add("lon");
            abstractKey.add("model");
            abstractKey.add("systemName");
            abstractKey.add("systemVersion");
            abstractKey.add("userId");
            LinkedHashMap<String, Object> submitData = map;
            StringBuilder buffer = new StringBuilder();
            for (String key : abstractKey) {
                buffer.append(key).append("=").append(EncryptUtils.getURLEncoderString(submitData.get(key).toString(), "UTF-8")).append("&");
            }
            buffer.append("SASEoK4Pa5d4SssO");
            String abs = buffer.toString();
            return EncryptUtils.getMd5(abs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void login(String username, String password) throws Exception {
        http.open("https://mobile.campushoy.com/v6/config/guest/tenant/info?ids=ccit.js");
        response = http.get();

        String ampUrl = "https://ccit.campusphere.net/wec-portal-mobile/client";
        int code = 0;
        while (code != 200) {
            http.open(ampUrl);
            response = http.get();
            code = response.getCode();
            if (response.getLocation() != null && !response.getLocation().equals("")) {
                ampUrl = response.getLocation();
                System.out.println("location " + ampUrl);
            }
        }
        System.out.println(ampUrl);
        http.open(ampUrl);
        response = http.get();
        Document document = Jsoup.parse(response.getBody());
        Element casLoginForm = document.getElementById("casLoginForm");
        Elements inputs = casLoginForm.getElementsByTag("input");
        String lt = inputs.get(4).attr("value");
        String dllt = inputs.get(5).attr("value");
        String execution = inputs.get(6).attr("value");
        String _eventId = inputs.get(7).attr("value");
        String rmShown = inputs.get(8).attr("value");
        String pwdDefaultEncryptSalt = inputs.get(9).attr("value");

        LogUtils.info(lt, dllt, execution, _eventId, rmShown, pwdDefaultEncryptSalt);

        //判断验证码
        String captcha = "";
        http.open("http://authserver.ccit.js.cn/authserver/needCaptcha.html?username=" + username);
        response = http.get();
        //System.out.println(response.getBody());

        if (response.getBody().indexOf("true") != -1) {
            System.out.println("需要验证码识别");
            //先获取图片Base64
            http.open("http://authserver.ccit.js.cn/authserver/captcha.html");
            response = http.get();
            captcha = TuJianUtils.discernCaptcha(Base64.getEncoder().encodeToString(response.getBytes()));
            System.out.println(captcha);
        }

        http.open(ampUrl
                + "&username=" + username
                + "&password=" + EncryptUtils.getURLEncoderString(encrypt_password(password, pwdDefaultEncryptSalt), "UTF-8")
                + "&captchaResponse=" + captcha
                + "&rememberMe=on"
                + "&lt=" + lt
                + "&dllt=" + dllt
                + "&execution=" + execution
                + "&_eventId=" + _eventId
                + "&rmShown=" + rmShown);
        //System.out.println(EncryptUtils.getURLEncoderString(encrypt_password(password, pwdDefaultEncryptSalt), "UTF-8"));
        response = http.post();
        location = response.getLocation();
        //System.out.println(location);
        //判断是否登陆成功
        if (location == null || location.indexOf("campusphere.net") == -1) {
            //登录失败
            throw new Exception("账号或密码错误");
        }
        //执行两次get得到关键cookie
        http.open(location);
        http.setHeader("Server", "CloudWAF");
        response = http.get();
        location = response.getLocation();

        http.open(location);
        response = http.get();
        System.out.println(response.getBody());

    }

    //QQ@消息的记录
    public static MessageChainBuilder messageChainBuilder = new MessageChainBuilder();

    public static void addMessage(SingleMessage singleMessage) {
        messageChainBuilder.append(singleMessage);
    }

    public static void addMessage(Message message) {
        messageChainBuilder.append(message);
    }

    public static void addMessage(CharSequence charSequence) {
        messageChainBuilder.append(charSequence);
    }


    public void executeTask(String username, String password, String qq) {


        try {
            login(username, password);
            //获取手机号和邮箱
            http.open("https://ccit.campusphere.net/personCenter/boundCellphone/cellphoneNumber");
            http.setContentType("application/json;charset=utf-8");
            response = http.post("{}");
            json = JSONObject.parseObject(response.getBody());
            phone = json.getJSONObject("datas").getString("cellphoneNumber");
            //获取邮箱(垃圾常州信息官方邮箱无法修改，该功能暂不能用)
            /*http.open("https://ccit.campusphere.net/personCenter/boundEmail/emailNumber");
            response = http.post("{}");
            json = JSONObject.parseObject(response.getBody());
            String email = json.getJSONObject("datas").getString("emailNumber");*/

            //获取个人信息
            http.open("https://ccit.campusphere.net/personCenter/user/getUserInfo");
            response = http.post("{}");
            System.out.println(response.getBody());
            json = JSONObject.parseObject(response.getBody());
            //获取姓名--孙志强先生
            String userGender = json.getJSONObject("datas").getJSONObject("userInfo").getString("userGender").equals("女") ? "小姐" : "先生";
            name = json.getJSONObject("datas").getJSONObject("userInfo").getString("userName") + userGender;
            userAccount = json.getJSONObject("datas").getJSONObject("userInfo").getString("userAccount");

            //获取未签到列表
            http.open("https://ccit.campusphere.net/wec-counselor-collector-apps/stu/collector/queryCollectorProcessingList");
            response = http.post("{}");
            //System.out.println(response.getBody());
            //获取未签到列表
            http.open("https://ccit.campusphere.net/wec-counselor-collector-apps/stu/collector/queryCollectorProcessingList");
            response = http.post("{\"pageNumber\": 1, \"pageSize\": 20}");
            json = JSONObject.parseObject(response.getBody());
            //System.out.println(response.getBody());

            cpDailyConfig cpDailyConfig = SpringUtil.getBean(cpDailyConfig.class);
            String lon = cpDailyConfig.getLon();
            String lat = cpDailyConfig.getLat();
            String address = cpDailyConfig.getAddress();
            //System.out.println("lon:" + lon + "|lat:" + lat + "|address:" + address);

            JSONArray jsonArray_list = json.getJSONObject("datas").getJSONArray("rows");
            for (int m = 0; m < jsonArray_list.size(); m++) {
                JSONObject json_task = jsonArray_list.getJSONObject(m);
                System.out.println(json_task);
                taskName = json_task.getString("subject");
                taskTime = json_task.getString("startTime");
                if (json_task.getInteger("isHandled") == 0) {
                    //处理签到
                    String wid = json_task.getString("wid");
                    //System.out.println("wid:" + wid);
                    int instanceWid = json_task.getInteger("instanceWid");
                    String formWid = json_task.getString("formWid");
                    http.open("https://ccit.campusphere.net/wec-counselor-collector-apps/stu/collector/detailCollector");
                    response = http.post("{\"collectorWid\": \"" + wid + "\", \"instanceWid\": " + instanceWid + "}");
                    //System.out.println(response.getBody());
                    JSONObject json1 = JSONObject.parseObject(response.getBody());
                    String schoolTaskWid = json1.getJSONObject("datas").getJSONObject("collector").getString("schoolTaskWid");
                    //System.out.println("schoolTaskWid:" + schoolTaskWid);

                    LinkedList<cpDailyForm> forms = cpDailyConfig.getForms();
                    //System.out.println(forms);
                    http.open("https://ccit.campusphere.net/wec-counselor-collector-apps/stu/collector/getFormFields");
                    response = http.post("{\"pageSize\": 9999, \"pageNumber\": 1, \"formWid\": \"" + formWid + "\", \"collectorWid\": \"" + wid + "\"}");
                    //System.out.println(response.getBody());
                    JSONObject json2 = JSONObject.parseObject(response.getBody(), Feature.OrderedField);
                    System.out.println(json2.toJSONString());
                    int totalSize = json2.getJSONObject("datas").getInteger("totalSize");
                    if (totalSize != forms.size()) {
                        throw new Exception("配置问题数量不匹配");
                    }

                    JSONArray jsonArray_rows = json2.getJSONObject("datas").getJSONArray("rows");
                    for (int i = 0; i < jsonArray_rows.size(); i++) {
                        JSONObject jsonObject_row = jsonArray_rows.getJSONObject(i);
                        //System.out.println("jsonArray_rows==>>" + jsonArray_rows);
                        if (forms.get(i).getTitle().equals(jsonObject_row.getString("title"))) {
                            //填充多出来的参数
                            jsonObject_row.put("show", true);
                            jsonObject_row.put("formType", "0");
                            jsonObject_row.put("sortNum", (i + 1) + "");
                            //判断题目类型
                            String filedType = jsonObject_row.getString("fieldType");
                            if (filedType.equals("1") || filedType.equals("5") || filedType.equals("6") || filedType.equals("7")) {
//                            jsonObject_row.put("value", getUnicode(forms.get(i).getValue()));//
                                jsonObject_row.put("value", forms.get(i).getValue());//
                            }
                            //单选题
                            if (filedType.equals("2")) {
                                JSONArray jsonArray_fieldItems = jsonObject_row.getJSONArray("fieldItems");
                                for (int j = 0; j < jsonArray_fieldItems.size(); j++) {
                                    if (!forms.get(i).getValue().equals(jsonArray_fieldItems.getJSONObject(j).getString("content"))) {
                                        jsonArray_fieldItems.remove(j);
                                        j--;
                                    } else {
//                                    jsonArray_fieldItems.getJSONObject(j).put("content", getUnicode(jsonArray_fieldItems.getJSONObject(j).getString("content")));
                                        String itemWid = jsonArray_fieldItems.getJSONObject(j).getString("itemWid");
                                        jsonObject_row.put("value", itemWid);
                                    }
                                }
                            }
//                        System.out.println(getUnicode(jsonObject_row.getString("title")));
//                        jsonObject_row.put("title", getUnicode(jsonObject_row.getString("title")));
                        } else {
                            throw new Exception("有配置项的标题不匹配");
                        }

                    }

                    LinkedHashMap<String, Object> allDataMap = new LinkedHashMap<>();
                    allDataMap.put("form", jsonArray_rows);
                    allDataMap.put("formWid", formWid);
//                allDataMap.put("address", getUnicode(address));
                    allDataMap.put("address", address);
                    allDataMap.put("collectWid", wid);
                    allDataMap.put("schoolTaskWid", schoolTaskWid);
                    allDataMap.put("uaIsCpadaily", true);
                    allDataMap.put("latitude", Double.valueOf(lat));
                    allDataMap.put("longitude", Double.valueOf(lon));
                    allDataMap.put("instanceWid", instanceWid);
                    //,SerializerFeature.WriteMapNullValue
                    //System.out.println("form=========" + JSONObject.toJSONString(allDataMap, SerializerFeature.WriteMapNullValue).replace("\\\\", "\\"));


                    String remarkName = "默认备注名";
                    String state = null;
                    String model = "OPPO R11 Plus";
                    String appVersion = "9.0.14";
                    String systemVersion = "4.4.4";
                    String systemName = "android";
                    String signVersion = "first_v3";
                    String calVersion = "firstv";
                    boolean getHistorySign = false;
                    int title = 0;
                    int signLevel = 1;
                    String abnormalReason = "回家";
                    String qrUuid = null;

                    String deviceId = "常州信息职业技术学院" + username;
                    deviceId = UUID.randomUUID().toString();
                    //System.out.println("deviceId:" + deviceId);
                    //deviceId = "1E1B9BAD-776D-0C62-FAFA-A3FB04C743DB";


                    LinkedHashMap<String, Object> map_extension = new LinkedHashMap<>();
                    map_extension.put("lon", lon);
                    map_extension.put("lat", lat);
                    map_extension.put("model", model);
                    map_extension.put("appVersion", appVersion);
                    map_extension.put("systemVersion", systemVersion);
                    map_extension.put("userId", username);
                    map_extension.put("systemName", systemName);
                    map_extension.put("deviceId", deviceId);


                    //System.out.println(JSONObject.toJSONString(map_extension));
                    //System.out.println("哈哈哈哈哈==="+JSONObject.toJSONString(map_extension));
                    String cpdailyExtension = encrypt_CpdailyExtension(JSONObject.toJSONString(map_extension));
                    //cpdailyExtension = encrypt_CpdailyExtension("{\"lon\": 119.960039, \"lat\": 31.682326, \"model\": \"OPPO R11 Plus\", \"appVersion\": \"9.0.14\", \"systemVersion\": \"4.4.4\", \"userId\": \"20091230427\", \"systemName\": \"android\", \"deviceId\": \"1E1B9BAD-776D-0C62-FAFA-A3FB04C743DB\"}");
                    //cpdailyExtension = "Iy86UyNyPEbTatb6UEDXOhhIcf0qdRhRsIEoIiQowHXGmDIkoDh0il9s/REFRo5Ni/o55cObo1v06xZ/qsyddcHNGak96uRB/bXI2WIg+mo6YmSY7C8mBpokQxnkrIc3suCJDKT6SU4+n+nWtmNgGJQPwnEX3oECdzOtprCO0y/KQ/n/+1WxU51FfgzRsZesS27XEy40R2Pt3SJ0bDvMHzkWlFGIW37GFUjBytY10DLvavT/XuuuNjJlKXLE+zKHvgXX26PmFTAhjFjM3033xoLB5oEXhuSL";
                    //System.out.println("cpdailyExtension:" + cpdailyExtension);
                    //System.out.println("allJSONData.toJSONString()==" + JSONObject.toJSONString(allDataMap, SerializerFeature.WriteMapNullValue).replace("\\\\", "\\"));
//                String bodyString = encrypt_BodyString(JSONObject.toJSONString(allDataMap, SerializerFeature.WriteMapNullValue).replace("\\\\", "\\"));
                    //System.out.println("bodyString:" + JSONObject.toJSONString(allDataMap, SerializerFeature.WriteMapNullValue).replace("\\\\", "\\"));

                    String bodyString = encrypt_BodyString(JSONObject.toJSON(allDataMap).toString());


                    LinkedHashMap<String, Object> map_submitData = new LinkedHashMap();
                    map_submitData.put("lon", lon);
                    map_submitData.put("version", signVersion);
                    map_submitData.put("calVersion", calVersion);
                    map_submitData.put("deviceId", deviceId);
                    map_submitData.put("userId", username);
                    map_submitData.put("systemName", systemName);
                    map_submitData.put("bodyString", bodyString);
                    map_submitData.put("lat", lat);
                    map_submitData.put("systemVersion", systemVersion);
                    map_submitData.put("appVersion", appVersion);
                    map_submitData.put("model", model);


                    //System.out.println("map_submitData------" + map_submitData);
//                Sign signUtil = new Sign(map_submitData);
//                String sign = signUtil.getSign();
                    //System.out.println("sign:" + sign);
                    map_submitData.put("sign", getSign(map_submitData));

                    //拿到提交的关键数据
                    //System.out.println(map_submitData);
                    //补全坐标等其他数据*/
                    http.open("https://ccit.campusphere.net/wec-counselor-collector-apps/stu/collector/submitForm");
                    http.setHeader("CpdailyStandAlone", "0");
                    http.setHeader("extension", "1");
                    http.setHeader("Cpdaily-Extension", cpdailyExtension);
//                    http.setHeader("Content-Type", "application/json;charset=utf-8");
                    http.setHeader("Host", "ccit.campusphere.net");
                    http.setHeader("Connection", "Keep-Alive");
                    http.setHeader("Accept-Encoding", "gzip");
                    //System.out.println(http.getHeaders());


                    response = http.post(JSONObject.toJSONString(map_submitData));
                    //解压
                    String ret = ByteUtils.gzip(response.getBytes());

                    //{"code":"0","message":"SUCCESS","wid":null,"hasWindowLocation":0,"windowLocation":"","score":null}
                    JSONObject json_ret = JSONObject.parseObject(ret);
                    System.out.println(json_ret.toJSONString());
                    String message = json_ret.getString("message");
                    if (!message.equals("SUCCESS")) {
                        throw new Exception("未知异常");
                    }
                    http.removeHeader("CpdailyStandAlone");
                    http.removeHeader("extension");
                    http.removeHeader("Cpdaily-Extension");
                    http.removeHeader("Host");
                    http.removeHeader("Connection");
                    http.removeHeader("Accept-Encoding");
                    AllQueue.noteTaskQueue.add(new noteTask(phone, "1390862", new String[]{name, userAccount, TimeUtils.getTimeStr(taskTime), "《" + taskName + "》"}));
                    AllQueue.emailTaskQueue.add(new emailTask("今日校园打卡完成", "尊敬的" + name + "，学号" + userAccount + "，您于" + TimeUtils.getTimeStr(taskTime) + "的今日校园打卡任务" + "《" + taskName + "》" + "已经执行完毕", qq + "@qq.com"));
                    //添加@列表
                    cpDailyUtils.addMessage(new At(Long.valueOf(qq)));
                    //发送私人消息
                    //miraiBotUtils.sendNormal(296897195, Long.valueOf(qq), new MessageChainBuilder().append(name+"\n").append("学号"+userAccount+"\n").append("《" + taskName + "》").append("已完成").build());
                    AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195, Long.valueOf(qq), new MessageChainBuilder().append(name + "\n").append("学号" + userAccount + "\n").append("《" + taskName + "》").append("已完成").build()));
                    //签完第一个就拜拜，下次循环再说
                    //break;

                    //直接置入已经签到完成的List 今日不再签到本账号
                    cpDailyConsumer.hasSignUserList.add(userAccount);


                } else {
                    //已经签完了，下一个
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            switch (e.getMessage()) {
                case "账号或密码错误":
                    AllQueue.removeCpDailyTask(username);
                    AllQueue.noteTaskQueue.add(new noteTask(phone, "1392614", new String[]{userAccount, "账号或密码错误"}));
                    AllQueue.emailTaskQueue.add(new emailTask("今日校园打卡失败", "您的今日校园账号" + userAccount + "遇到特殊异常，现已被踢出任务队列，请重新登录网站添加任务。 异常内容：" + "账号或密码错误", qq + "@qq.com"));
                    AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195, Long.valueOf(qq), new MessageChainBuilder().append("您的今日校园账号" + userAccount + "遇到特殊异常，现已被踢出任务队列，请重新登录网站添加任务。 异常内容：" + "账号或密码错误").build()));
                    break;
                case "配置问题数量不匹配":
                    AllQueue.removeCpDailyTask(username);
                    AllQueue.noteTaskQueue.add(new noteTask(phone, "1392614", new String[]{userAccount, "疫情打卡内容有所变动"}));
                    AllQueue.emailTaskQueue.add(new emailTask("今日校园打卡失败", "您的今日校园账号" + userAccount + "遇到特殊异常，现已被踢出任务队列，请重新登录网站添加任务。 异常内容：" + "疫情打卡内容有所变动", qq + "@qq.com"));
                    AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195, Long.valueOf(qq), new MessageChainBuilder().append("您的今日校园账号" + userAccount + "遇到特殊异常，现已被踢出任务队列，请重新登录网站添加任务。 异常内容：" + "疫情打卡内容有所变动").build()));
                    break;
                case "有配置项的标题不匹配":
                    AllQueue.removeCpDailyTask(username);
                    AllQueue.noteTaskQueue.add(new noteTask(phone, "1392614", new String[]{userAccount, "疫情打卡内容有所变动"}));
                    AllQueue.emailTaskQueue.add(new emailTask("今日校园打卡失败", "您的今日校园账号" + userAccount + "遇到特殊异常，现已被踢出任务队列，请重新登录网站添加任务。 异常内容：" + "疫情打卡内容有所变动", qq + "@qq.com"));
                    AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195, Long.valueOf(qq), new MessageChainBuilder().append("您的今日校园账号" + userAccount + "遇到特殊异常，现已被踢出任务队列，请重新登录网站添加任务。 异常内容：" + "疫情打卡内容有所变动").build()));
                    break;
                case "未知异常":
                    AllQueue.removeCpDailyTask(username);
                    AllQueue.noteTaskQueue.add(new noteTask(phone, "1392614", new String[]{userAccount, "未知异常"}));
                    AllQueue.emailTaskQueue.add(new emailTask("今日校园打卡失败", "您的今日校园账号" + userAccount + "遇到特殊异常，现已被踢出任务队列，请重新登录网站添加任务。 异常内容：" + "未知异常", qq + "@qq.com"));
                    AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195, Long.valueOf(qq), new MessageChainBuilder().append("您的今日校园账号" + userAccount + "遇到特殊异常，现已被踢出任务队列，请重新登录网站添加任务。 异常内容：" + "未知异常").build()));
                    break;
                default:
                    //有可能是因为代理ip连接超时导致异常 不用管他
//                    AllQueue.emailTaskQueue.add(new emailTask("今日校园打卡失败", "您的今日校园账号" + userAccount + "代理ip异常，您无需处理，等待自动重新打卡", qq + "@qq.com"));
//                    AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195, Long.valueOf(qq), new MessageChainBuilder().append("您的今日校园账号" + userAccount + "代理ip异常，您无需处理，等待自动重新打卡").build()));
                    break;
            }
        }
    }
}
