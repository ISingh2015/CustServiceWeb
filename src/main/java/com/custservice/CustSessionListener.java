package com.custservice;

import java.util.Date;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class CustSessionListener implements HttpSessionListener {
//	Logger logger = LoggerFactory.getLogger(CustSessionListener.class);

    public void sessionCreated(HttpSessionEvent arg0) {
        System.out.println("Session Created " + new Date());
    }

    public void sessionDestroyed(HttpSessionEvent arg0) {
        System.out.println("Session Destroy " + new Date());        
    }

}
