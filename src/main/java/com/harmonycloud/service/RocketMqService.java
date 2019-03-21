package com.harmonycloud.service;

import com.alibaba.fastjson.JSON;
import com.harmonycloud.bo.UserPrincipal;
import com.harmonycloud.dto.Audit;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.OrderException;
import com.harmonycloud.util.IpUtil;
import com.harmonycloud.util.LogUtil;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;


@Service
public class RocketMqService {
    private Logger logger = LoggerFactory.getLogger(RocketMqService.class);


    private DefaultMQProducer producer;
    /**
     * NameServer 地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Autowired
    private HttpServletRequest request;


    @PostConstruct
    public void defaultMQProducer() {
        String msg = LogUtil.getRequest(request) + ", information='";

        //生产者的组名
        producer = new DefaultMQProducer("Order");
        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(namesrvAddr);
        //producer.setVipChannelEnabled(false);
        try {
            producer.start();
            logger.info(msg + "-------->:producer启动了'");
            System.out.println("-------->:producer启动了");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public String sendMsg(String topic, String tags, String information) throws Exception {
        String msg = LogUtil.getRequest(request) + ", information='";

        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        String correlation = request.getHeader("x-b3-traceid");
        Audit audit = new Audit(new Date(), "Critical", "Computer", IpUtil.getIpAddress(request),
                userDetails.getId(), "CIMS", correlation, "MedicationOrder", information);

        Message message = new Message(topic, tags, JSON.toJSONString(audit).getBytes());
        StopWatch stop = new StopWatch();
        stop.start();
        SendResult result = producer.send(message);
        if (result.getSendStatus().equals(SendStatus.SEND_OK)) {
            logger.info(msg + "send message success'");

            System.out.println("发送响应：MsgId:" + result.getMsgId() + "，发送状态:" + result.getSendStatus());
        } else {
            throw new OrderException(ErrorMsgEnum.ROCKETMQ_ERROR.getMessage());
        }
        stop.stop();
        return "{\"MsgId\":\"" + result.getMsgId() + "\"}";
    }
}
