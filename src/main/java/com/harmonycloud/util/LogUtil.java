package com.harmonycloud.util;

import com.harmonycloud.log.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class LogUtil {


    public static String getRequest(HttpServletRequest request) {
        String uuid = null;
        for (int i = 0; i < 10; i++) {
            uuid = UUID.randomUUID().toString().replaceAll("-", "");
        }
        if (request != null) {
            String ip = IpUtil.getIpAddress(request);
            String loginName = request.getHeader("user");
            Log log = new Log(ip, loginName, "CIMS", uuid, "User Application");
            return log.toString();
        }
        return null;
    }

}
