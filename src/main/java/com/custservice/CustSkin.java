/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custservice;

import java.io.Serializable;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Zed
 */
@ManagedBean(name = "custAppSkin", eager = true)
@SessionScoped

public class CustSkin implements Serializable{

    
    private static long serialVersionUID = 1L;    
    private String skinName = "";
    private final String themeString[] = {"emeraldTown", "blueSky", "wine", "japanCherry", "ruby", "classic", "deepMarine"};

    public CustSkin() {
        Random ranDom = new Random();
        int low = 0, high = 6;
        int skinNo = ranDom.nextInt(high - low) + low;
        skinName = themeString[skinNo];
    }

    /**
     * @return the SkinName
     */
    public String getSkinName() {
        return skinName;
    }

    /**
     * @param SkinName the SkinName to set
     */
    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

}
