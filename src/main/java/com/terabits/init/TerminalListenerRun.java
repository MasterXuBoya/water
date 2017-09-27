package com.terabits.init;

import com.terabits.meta.po.Device.HeartBeatPO;
import com.terabits.meta.po.Device.TerminalOfflinePO;
import com.terabits.meta.po.Device.TerminalPO;
import com.terabits.service.TerminalListenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import java.util.ArrayList;
import java.util.List;

import static com.terabits.constant.TerminalListenerConstant.OFFLINE_STATUS;
import static com.terabits.constant.TerminalListenerConstant.ONLINE_STATUS;
import static com.terabits.constant.TerminalListenerConstant.OVER_TIME;
import static com.terabits.constant.TerminalListenerConstant.SLEEP_TIME;
import static java.lang.Thread.sleep;

public class TerminalListenerRun implements Runnable {

    private ServletContextEvent sce;
    private static Logger logger = LoggerFactory.getLogger(TerminalListenerRun.class);

    public void setServletContextEvent(ServletContextEvent sce) {
        this.sce = sce;
    }

    public ServletContextEvent getServletContextEvent() {
        return this.sce;
    }

    public void run() {
        while (true) {
            WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
            TerminalListenerService terminalListenerService = ctx.getBean(TerminalListenerService.class);
            try {
                List<TerminalPO> terminalPOS = new ArrayList<TerminalPO>();
                terminalPOS = terminalListenerService.selectAllTerminal();
                for (TerminalPO terminal : terminalPOS) {
                    String deviceId = terminal.getDeviceId();
                    HeartBeatPO terminalHeartBeat = terminalListenerService.selectHeartBeatByDeviceId(deviceId);
                    String lastHeartBeatTime = terminalHeartBeat.getGmtModified();
                    int result = terminalListenerService.calculateWhetherOffline(lastHeartBeatTime, OVER_TIME);
                    /**如果设备上一次心跳包的时间超过OVER_TIME*/
                    if (result == OFFLINE_STATUS) {
                        //更改设备列表中的在线状态
                        int lastStatus = terminal.getState();
                        String imei = terminal.getImei();
                        int updateResult = terminalListenerService.updateTerminalStatusByImei(result, terminal.getImei());
                        System.out.println("displayId" + terminal.getDisplayId() + "号设备检查掉线，更新状态结果" + updateResult);
                        //判断设备是否已经掉线，若掉线则只刷新掉线检查时间
                        if (lastStatus == OFFLINE_STATUS) {
                            int insertResult = terminalListenerService.updateOfflineTimeByImei(imei);
                            System.out.println("displayId" + terminal.getDisplayId() + "号设备更新了掉线时间，更新结果" + insertResult);
                        } else {
                            TerminalOfflinePO terminalOfflinePO = new TerminalOfflinePO();
                            terminalOfflinePO.setImei(terminal.getImei());
                            int insertResult = terminalListenerService.insertOfflineRecord(terminalOfflinePO);
                            System.out.println("displayId" + terminal.getDisplayId() + "号设备检查掉线，插入故障结果" + insertResult);
                        }

                    } else if (result == ONLINE_STATUS) {
                        //terminalListenerService.updateTerminalStatusByImei(result, terminal.getImei());
                        System.out.println("displayId" + terminal.getDisplayId() + "号设备检查已经连接");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
