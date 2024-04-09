package vip.wqby.ccitserver;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import vip.wqby.ccitserver.config.SpringUtil;
import vip.wqby.ccitserver.pojo.CommonResult;
import vip.wqby.ccitserver.pojo.task.rollCallTask;
import vip.wqby.ccitserver.util.IbsUtils;
import vip.wqby.ccitserver.util.LogUtils;
import vip.wqby.ccitserver.util.cpDailyUtils;
import vip.wqby.ccitserver.util.http.Http;
import vip.wqby.ccitserver.util.http.HttpResponse;
import vip.wqby.ccitserver.util.qndxxUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CcitserverApplicationTests {
    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        //插入数据
//        for (int i =0 ; i<800;i++){
//            Card card = new Card();
//            card.setUse(false);
//            card.setCardId(RandomUtils.randomCardId());
//            mongoTemplate.insert(card);
//        }

//
//        List<Card> all = mongoTemplate.findAll(Card.class);
//        System.out.println(all);
//
//        Query query = new Query(Criteria.where("cardId").is("dsadsa213214324khjk3j21"));
//        List<Card> cards = mongoTemplate.find(query, Card.class);
//        System.out.println(cards);
    }

    @Test
    void ibs() throws IOException {
        IbsUtils ibsUtils = new IbsUtils();
        ibsUtils.getHomeWorkList("17301491797","BAye6666");
    }

    @Test
    void ibsTask() throws Exception{
        IbsUtils ibsUtils = new IbsUtils();
        ibsUtils.doHomeWork("17301491797","BAye6666","5482798470843679048","test");
//        ibsUtils.doHomeWork("17849585568","a17849585568","5107092643256140061");
    }

//    @Test
//    void getCard() throws Exception{
//        Criteria criteria = Criteria.where("isUse").is(false);
//        Query query = Query.query(criteria);
//        Card card = mongoTemplate.findOne(query, Card.class, "card");
//        System.out.println(card);
//    }

    @Test
    void test111() throws Exception{
        Http http_proxy = new Http();
        http_proxy.open("http://webapi.http.zhimacangku.com/getip?num=1&type=2&pro=&city=0&yys=0&port=1&pack=179816&ts=0&ys=0&cs=0&lb=1&sb=0&pb=4&mr=1&regions=");
        HttpResponse httpResponse_proxy = http_proxy.get();
        System.out.println(httpResponse_proxy.getBody().toString());
        JSONObject json_proxy = JSONObject.parseObject(httpResponse_proxy.getBody());
        String ip = json_proxy.getJSONArray("data").getJSONObject(0).getString("ip");
        int port = Integer.valueOf(json_proxy.getJSONArray("data").getJSONObject(0).getString("port"));
    }

    @Test
    void cpDaily() throws Exception{
//        cpDailyUtils.setProxy();
        cpDailyUtils cpDailyUtils = new cpDailyUtils();
        cpDailyUtils.executeTask("20091230427","123456789@Wgq","321");

//        cpDailyUtils cpDailyUtils1 = new cpDailyUtils();
//        cpDailyUtils1.executeTask("20091230428","wk773473238","3213");
//        cpDailyUtils.executeTask("20091230427","123456789@Wgq","9479421");
//        cpDailyUtils.executeTask("20091230425","a17849585568");

//        try {
//            cpDailyUtils.login("20091230427","123456789@Wgq");
//        }catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("321321312");
//        }


    }

    @Test
    void timeTest() throws Exception{
        qndxxUtils qndxxUtils = new qndxxUtils();
//        qndxxUtils.testSession("qTus7pAwR4fA8RquSiyVg1NuRu3UMQbFEbcP1Gbp\n");
//        qndxxUtils.executeTask("SUCmBhpxqK0n8XoiJMMJCPIHKgO1aL23FvXG8CL2\n","9479421","17301491797");
    }


    @Test
    void parse() throws Exception{
        JSONObject json = JSONObject.parseObject("{\"Result\":0,\"Data\":[{\"ObjectID\":\"02f05419-683d-492e-851e-961f8d54de07\",\"RollCallID\":\"0bcf36a1-9e9f-4d5b-8e81-b4169b3ec298\",\"StuID\":\"5202ae44-b33c-4dac-a40a-1e9feded2a8b\",\"StuName\":\"陈剑\",\"ClassID\":\"5278244032848944213\",\"PunchTime\":\"2022-05-11 08:16:10\",\"PhoneID\":\"dd6c6203a49f41199efc381ce316b9f3\",\"PhoneBrand\":\"iOS\",\"IsPunch\":true,\"PointX\":31.6823,\"PointY\":119.96,\"PointZ\":0.0,\"PuncherID\":\"5202ae44-b33c-4dac-a40a-1e9feded2a8b\",\"PuncherName\":\"陈剑\",\"CreateTime\":\"2022-05-11 08:15:31\",\"EndTime\":\"2022-05-11 08:20:31\",\"Code\":\"0777\",\"System\":\"\",\"IsDelete\":false,\"Type\":0,\"Remark\":\"\",\"PunchDistance\":15002,\"UserObjectID\":\"20091630302\",\"IsOutdate\":true}],\"Msg\":\"成功\",\"Info\":null,\"status\":0}");
//        Double PointX = json.getDouble("PointX");
//        String PointY = json.getJSONObject("Data").getString("PointY");
//        String PointZ = json.getString("PointZ");
//        System.out.println(PointX);
//        LogUtils.info(PointY,PointZ);
        json.getJSONObject("321").getString("321");
    }

    @Test
    void rollCall() throws Exception{

        IbsUtils ibsUtils = new IbsUtils();
        MongoTemplate mongoTemplate = (MongoTemplate) SpringUtil.getBean("mongoTemplate");
        List<rollCallTask> rollCallUserList = mongoTemplate.findAll(rollCallTask.class, "ibsqdUserList");
        ibsUtils.doRollCallList(rollCallUserList);
//        ibsUtils.doRollCall("17301491797","BAye6666","5218019443118766273","g3PA9ETIAdaDky9+DmbYEZPqi7kU3phA2sd8rsGKbDi4MSOohVomKwkO2O8nx17fjL02SgeJqRDEJMngEIm9PA==","ba24f80c-b390-461e-ac7e-3329b085eb96");
//        ibsUtils.doRollCall("17301491797","BAye6666","5278244032848944213","g3PA9ETIAdaDky9+DmbYEZPqi7kU3phA2sd8rsGKbDi4MSOohVomKwkO2O8nx17fjL02SgeJqRDEJMngEIm9PA==","ba24f80c-b390-461e-ac7e-3329b085eb96");

    }

    @Test
    void attack() throws Exception{
        for (;;){
            IbsUtils ibsUtils = new IbsUtils();
            //吴昌忻
            ibsUtils.attack("17301491797","BAye6666","5278244032848944213","g3PA9ETIAdaDky9+DmbYEZPqi7kU3phA2sd8rsGKbDi4MSOohVomKwkO2O8nx17fjL02SgeJqRDEJMngEIm9PA==","ba24f80c-b390-461e-ac7e-3329b085eb96"
                    ,"764e16b3-5aa4-4df6-8861-ec29738185c0");
            ibsUtils.attack("17301491797","BAye6666","5218019443118766273","g3PA9ETIAdaDky9+DmbYEZPqi7kU3phA2sd8rsGKbDi4MSOohVomKwkO2O8nx17fjL02SgeJqRDEJMngEIm9PA==","ba24f80c-b390-461e-ac7e-3329b085eb96"
                    ,"764e16b3-5aa4-4df6-8861-ec29738185c0");
            //王晓娇
            ibsUtils.attack("17301491797","BAye6666","5278244032848944213","g3PA9ETIAdaDky9+DmbYEZPqi7kU3phA2sd8rsGKbDi4MSOohVomKwkO2O8nx17fjL02SgeJqRDEJMngEIm9PA==","ba24f80c-b390-461e-ac7e-3329b085eb96"
                    ,"f8b4c8a3-5541-4b00-9b72-b0da92d6b5d9");
            ibsUtils.attack("17301491797","BAye6666","5218019443118766273","g3PA9ETIAdaDky9+DmbYEZPqi7kU3phA2sd8rsGKbDi4MSOohVomKwkO2O8nx17fjL02SgeJqRDEJMngEIm9PA==","ba24f80c-b390-461e-ac7e-3329b085eb96"
                    ,"f8b4c8a3-5541-4b00-9b72-b0da92d6b5d9");
            //王雨晴
            ibsUtils.attack("17301491797","BAye6666","5278244032848944213","g3PA9ETIAdaDky9+DmbYEZPqi7kU3phA2sd8rsGKbDi4MSOohVomKwkO2O8nx17fjL02SgeJqRDEJMngEIm9PA==","ba24f80c-b390-461e-ac7e-3329b085eb96"
                    ,"cc48a05e-c8af-454d-8428-e584bca51155");
            ibsUtils.attack("17301491797","BAye6666","5218019443118766273","g3PA9ETIAdaDky9+DmbYEZPqi7kU3phA2sd8rsGKbDi4MSOohVomKwkO2O8nx17fjL02SgeJqRDEJMngEIm9PA==","ba24f80c-b390-461e-ac7e-3329b085eb96"
                    ,"cc48a05e-c8af-454d-8428-e584bca51155");

            System.out.println("================================================");
            Thread.sleep(1000);
        }

    }
}
