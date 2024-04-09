package vip.wqby.ccitserver.util;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SingleMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vip.wqby.ccitserver.pojo.task.emailTask;
import vip.wqby.ccitserver.pojo.task.noteTask;
import vip.wqby.ccitserver.pojo.task.qqMessageTask;
import vip.wqby.ccitserver.queue.AllQueue;
import vip.wqby.ccitserver.util.http.Http;
import vip.wqby.ccitserver.util.http.HttpResponse;

import javax.print.Doc;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class qndxxUtils {
    Http http = new Http();
    HttpResponse response = null;
    JSONObject json = null;
    Document document = null;

    {
        http.AutoCookie();
        http.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 15_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.18(0x18001234) NetType/WIFI Language/zh_CN");
    }

    public String testSession(String session) throws Exception {
        http.open("https://service.jiangsugqt.org/youth/lesson");
        http.setHeader("Cookie", "laravel_session=" + session);
        response = http.get();
        System.out.println(response.getBody());
        http.open(response.getLocation());
        response = http.get();
        System.out.println(response.getBody());
        Document document = Jsoup.parse(response.getBody());
        Elements selects = document.select(".confirm-user-info > p");
        String ret = "";
        for (Element element : selects) {
            ret += element.text() + "\n";
        }
        return ret.substring(0, ret.length() - 2);
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


    public void executeTask(String session, String qq, String phone) {
        String lessonName = "";
        String name = "";
        String number = "";
        String organization = "";
        try {
            String token = "";
            String lesson_id = "";
            //先读取有无最新要学习的课程
            http.open("https://service.jiangsugqt.org/youth/report");
            http.setHeader("Cookie", "laravel_session=" + session);
            response = http.get();
            //System.out.println(response.getBody());
            document = Jsoup.parse(response.getBody());
            String status = document.getElementsByClass("text-center").get(0).text();
            if (status.equals("未学习")) {
                //然后才学习
            } else {
                return;
            }
            http.open("https://service.jiangsugqt.org/youth/lesson");
            http.setHeader("Cookie", "laravel_session=" + session);
            response = http.get();
            http.open(response.getLocation());
            response = http.get();
            //System.out.println(response.getBody());
            document = Jsoup.parse(response.getBody());
            //获取个人信息
            Elements elements = document.select(".confirm-user-info > p");
            lessonName = elements.get(0).text().replace(elements.get(0).getElementsByTag("span").get(0).text(),"");
            name = elements.get(1).text().replace(elements.get(1).getElementsByTag("span").get(0).text(),"");
            number = elements.get(2).text().replace(elements.get(2).getElementsByTag("span").get(0).text(),"");
            organization = elements.get(3).text().replace(elements.get(3).getElementsByTag("span").get(0).text(),"");
            LogUtils.info(lessonName,name,number,organization);

            Elements scripts = document.getElementsByTag("script");
            for (Element script : scripts) {
                if (script.data().contains("var token")) {
                    String pattern = "\"\\w{40}\"";
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(script.data());
                    if (m.find()) {
                        String group = m.group();
                        token = group.substring(1, group.length() - 1);
                    }

                    String pattern1 = "'lesson_id':[0-9]{3}";
                    Pattern p1 = Pattern.compile(pattern1);
                    Matcher m1 = p1.matcher(script.data());
                    if (m1.find()) {
                        String group = m1.group();
                        lesson_id = group.substring(group.length() - 3);
                    }
                    break;
                }
            }
            if (lesson_id.equals("") || token.equals("")) {
                throw new Exception("未知异常");
            }
            http.open("https://service.jiangsugqt.org/youth/lesson/confirm");
            response = http.post("_token=" + token + "&lesson_id=" + lesson_id);
            json = JSONObject.parseObject(response.getBody());
            if (json.getString("message").equals("操作成功")) {
                AllQueue.noteTaskQueue.add(new noteTask(phone, "1392612", new String[]{organization,name,lessonName}));
                AllQueue.emailTaskQueue.add(new emailTask("青年大学习任务已完成","来自"+organization+"的"+name+"，你的青年大学习最新一期《"+lessonName+"》已经执行完毕",qq+"@qq.com"));
                //添加@列表
                qndxxUtils.addMessage(new At(Long.valueOf(qq)));
                //发送私人消息
                //miraiBotUtils.sendNormal(296897195, Long.valueOf(qq), new MessageChainBuilder().append(name+"\n").append("青年大学习最新一期\n").append("《" + lessonName + "》\n").append("已完成").build());
                AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195, Long.valueOf(qq), new MessageChainBuilder().append(name+"\n").append("青年大学习最新一期\n").append("《" + lessonName + "》\n").append("已完成").build()));
            } else {
                throw new Exception("未知异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            switch (e.getMessage()) {
                case "未知异常":
                    //只有手动抛出的异常才会踢出队列
                    AllQueue.removeQndxxTask(session);
                    AllQueue.noteTaskQueue.add(new noteTask(phone, "1392613", new String[]{name}));
                    AllQueue.emailTaskQueue.add(new emailTask("青年大学习任务失败","尊敬的"+name+"，您的青年大学习执行过程遇到异常，现已踢出队列，请您重新登录网站添加任务",qq+"@qq.com"));
                    AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195, Long.valueOf(qq), new MessageChainBuilder().append("尊敬的"+name+"，您的青年大学习执行过程遇到异常，现已踢出队列，请您重新登录网站添加任务").build()));
                    //miraiBotUtils.sendNormal(296897195, Long.valueOf(qq), new MessageChainBuilder().append("尊敬的"+name+"，您的青年大学习执行过程遇到异常，现已踢出队列，请您重新登录网站添加任务").build());
                    break;
                default:
                    break;
            }
        }
    }

}
