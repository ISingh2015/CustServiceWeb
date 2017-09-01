/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Zed
 */
@ManagedBean(name = "custPropertyCache", eager = true)
@ApplicationScoped 

public class CustPropertyCache implements Serializable {

    private Properties custPropertyCache = new Properties();

    /**
     * @return the custPropertyCache
     */
    public Properties getCustPropertyCache() {
        return custPropertyCache;
    }

    /**
     * @param custPropertyCache the custPropertyCache to set
     */
    public void setCustPropertyCache(Properties custPropertyCache) {
        this.custPropertyCache = custPropertyCache;
    }

    public CustPropertyCache() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("CustPropertyCache.properties");

        try {
            URL s = this.getClass().getClassLoader().getResource("CustPropertyCache.properties");
            File f = new File(s.toURI().getPath());
            custPropertyCache.load(in);
            System.out.println("Read Cust Service properties " + f.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return getCustPropertyCache().getProperty(key);
    }

    public Set<String> getAllPropertyNames() {
        return getCustPropertyCache().stringPropertyNames();
    }

    public boolean containsKey(String key) {
        return getCustPropertyCache().containsKey(key);
    }

    public void setProperty(String key, String value) {
        getCustPropertyCache().setProperty(key, value);
        writeFileProperies();

//        getCustPropertyCache().store(null, "Saved " + new Date());
    }

    private boolean writeFileProperies() {
        boolean written = false;
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("CustPropertyCache.properties");

        try {
            URL s = this.getClass().getClassLoader().getResource("CustPropertyCache.properties");
            File f = new File(s.toURI().getPath());
            OutputStream fos = new FileOutputStream(f);
            custPropertyCache.store(fos, new Date().toString());
            fos.close();

            System.out.println("Read / Write Cust Service properties " + f.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return written;
    }
}
