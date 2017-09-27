package com.terabits.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TerminalListener implements ServletContextListener {
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    public void contextInitialized(ServletContextEvent arg0) {
//        随Spring一起启动的监听程序
        TerminalListenerRun run = new TerminalListenerRun();
        run.setServletContextEvent(arg0);
        new Thread(run).start();
    }
}
