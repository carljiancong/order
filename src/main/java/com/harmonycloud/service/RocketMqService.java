package com.harmonycloud.service;

import com.alibaba.fastjson.JSON;
import com.harmonycloud.bo.UserPrincipal;
import com.harmonycloud.dto.Audit;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.OrderException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;


@Service
public class RocketMqService {

    private DefaultMQProducer producer;
    /**
     * NameServer 地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @PostConstruct
    public void defaultMQProducer() {

        //生产者的组名
        producer = new DefaultMQProducer("Order");
        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(namesrvAddr);
        //producer.setVipChannelEnabled(false);
        try {
            producer.start();
            System.out.println("-------->:producer启动了");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public String sendMsg(String topic, String tags, String information) throws Exception {
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        String uuid = null;
        for (int i = 0; i < 10; i++) {
            uuid = UUID.randomUUID().toString().replaceAll("-", "");
        }
        Audit audit = new Audit(new Date(), "Critical", "Computer", InetAddress.getLocalHost().getHostAddress(),
                userDetails.getId(), "CIMS", uuid, "MedicationOrder", information);


        Message message = new Message(topic, tags, JSON.toJSONString(audit).getBytes());
        StopWatch stop = new StopWatch();
        stop.start();
        SendResult result = producer.send(message);
        if (result.getSendStatus().equals(SendStatus.SEND_OK)) {
            System.out.println("发送响应：MsgId:" + result.getMsgId() + "，发送状态:" + result.getSendStatus());
        } else {
            throw new OrderException(ErrorMsgEnum.ROCKETMQ_ERROR.getMessage());
        }
        stop.stop();
        return "{\"MsgId\":\"" + result.getMsgId() + "\"}";
    }
}
