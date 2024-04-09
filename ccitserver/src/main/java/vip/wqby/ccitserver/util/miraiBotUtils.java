package vip.wqby.ccitserver.util;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.utils.BotConfiguration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class miraiBotUtils {
    //    public static void main(String[] args) {
//        Bot bot = BotFactory.INSTANCE.newBot(1684540890, "BAye6666");
//        bot.login();
//        miraiBotUtils.afterLogin(bot);
//    }
    static Bot bot = BotFactory.INSTANCE.newBot(648047199, "722830@Wgq", new BotConfiguration() {{
        // 配置，例如：
        setProtocol(MiraiProtocol.ANDROID_PAD);
    }});

//    static Bot bot = BotFactory.INSTANCE.newBot(1403346203, "BAye6666");

    public static void login() {
        bot.login();
    }

    public static void sendNormal(long group, long qq, Message message) {
        try {
            bot.getGroup(group).get(qq).sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendGroup(long group, Message message) {
        try {
            bot.getGroup(group).sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



/*
    public static void afterLogin(Bot bot) {
//        bot.getGroup(296897195).sendMessage(new MessageChainBuilder().append(new At(2200507849L)).append("测试java艾特傻逼功能").build());
//        bot.getGroup(296897195).sendMessage(new MessageChainBuilder().append(new At(9479421L)).append("测试java艾特帅哥功能").build());

        bot.getGroup(296897195).get(1900364029).sendMessage(new MessageChainBuilder().append("帅B测试").build());

        long yourQQNumber = 9479421;
        bot.getEventChannel().subscribeAlways(FriendMessageEvent.class, (event) -> {
            if (event.getSender().getId() == yourQQNumber) {
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("Hi, you just said: '")
                        .append(event.getMessage())
                        .append("'")
                        .build()
                );
            }
        });
    }*/

