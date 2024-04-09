package vip.wqby.ccitserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vip.wqby.ccitserver.pojo.*;
import vip.wqby.ccitserver.pojo.task.homeworkTask;
import vip.wqby.ccitserver.pojo.task.rollCallTask;
import vip.wqby.ccitserver.queue.AllQueue;
import vip.wqby.ccitserver.service.IbsService;
import vip.wqby.ccitserver.util.IbsUtils;
import vip.wqby.ccitserver.util.LogUtils;
import vip.wqby.ccitserver.util.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IbsServiceImpl implements IbsService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public CommonResult getHomeWorkList(String username, String password) {
        try{
            IbsUtils ibsUtils = new IbsUtils();
            ArrayList<homework> homeworkList = ibsUtils.getHomeWorkList(username, password);
            return new CommonResult(200,"获取作业成功", homeworkList);
        }catch (Exception e){
            return new CommonResult(444,"获取失败,账号或密码错误",null);
        }
    }

    @Override
    public CommonResult submitTask(String username, String password, String businessID, String cardID) {
        Criteria criteria = Criteria.where("cardId").is(cardID);
        Query query = Query.query(criteria);
        Card card = mongoTemplate.findOne(query, Card.class, "card");
        System.out.println(card);
        if (card==null){
            return new CommonResult(444,"该卡密不存在！",null);
        }
        if (card.isUse()==true){
            return new CommonResult(443,"该卡密已经被使用过！",null);
        }

        //提交队列
        AllQueue.homeworkQueue.add(new homeworkTask(username,password,businessID, TimeUtils.getTimeStr(new Date())));

        Card newCard = new Card();
        newCard.set_id(card.get_id());
        newCard.setCardId(card.getCardId());
        newCard.setUse(true);
        mongoTemplate.save(newCard,"card");

        return new CommonResult(200,"添加任务队列成功！",null);
    }

    @Override
    public CommonResult getCardId(String authorization) {
        if (!authorization.equals("helloworld")){
            return new CommonResult(200,"获取失败",null);
        }
        Criteria criteria = Criteria.where("isUse").is(false);
        Query query = Query.query(criteria);
        Card card = mongoTemplate.findOne(query, Card.class, "card");
        return new CommonResult(200,"获取成功",card);
    }

    @Override
    public CommonResult getCourseList(String username, String password) {
        try{
            IbsUtils ibsUtils = new IbsUtils();
            ArrayList<Course> homeworkList = ibsUtils.getCourseList(username, password);
            return new CommonResult(200,"获取作业成功", homeworkList);
        }catch (Exception e){
            return new CommonResult(444,"获取失败,账号或密码错误",null);
        }
    }

    @Override
    public CommonResult submitTask_rollCall(String username, String password,  String courseId,  String userId, String token, String cardID) {
        LogUtils.info(username,password,courseId,userId,token,cardID);

        Criteria criteria = Criteria.where("cardId").is(cardID);
        Query query = Query.query(criteria);
        Card card = mongoTemplate.findOne(query, Card.class, "card");
        System.out.println(card);
        if (card==null){
            return new CommonResult(444,"该卡密不存在！",null);
        }
        if (card.isUse()==true){
            return new CommonResult(443,"该卡密已经被使用过！",null);
        }

//        Query query1 = new Query();
//        query1.addCriteria(Criteria.where("username").is(username));
//        query1.addCriteria(Criteria.where("courseId").is(courseId));
//        rollCallTask one = mongoTemplate.findOne(query1, rollCallTask.class,"ibsqdUserList");
//        System.out.println(one);

        if (mongoTemplate.findOne(Query.query(Criteria.where("username").is(username).and("courseId").is(courseId)), rollCallTask.class,"ibsqdUserList")!=null){
            return new CommonResult(888,"该用户的该课程已经存在任务队列，请勿重复添加");
        }
        //添加任务到数据库
        rollCallTask rollCallTask = new rollCallTask();
        rollCallTask.setUsername(username);
        rollCallTask.setPassword(password);
        rollCallTask.setCourseId(courseId);
        rollCallTask.setUserId(userId);
        rollCallTask.setToken(token);
        mongoTemplate.save(rollCallTask,"ibsqdUserList");

        Card newCard = new Card();
        newCard.set_id(card.get_id());
        newCard.setCardId(card.getCardId());
        newCard.setUse(true);
        mongoTemplate.save(newCard,"card");

        return new CommonResult(200,"添加任务队列成功！",null);
    }

}
