/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custservice.vo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;

/**
 *
 * @author ISanhot
 */
@ManagedBean(name = "mediaBean", eager = true)
@SessionScoped
public class MediaBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer digits;

    public void paint(OutputStream out, Object data) {
        Integer high = 9999;
        Integer low = 1000;
        Random generator = new Random();
        setDigits((Integer) generator.nextInt(high - low + 1) + low);
        try {
//            MediaData paintData = (MediaData) data;
            BufferedImage img = new BufferedImage(150, 75, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = img.createGraphics();
            graphics2D.setBackground(Color.lightGray);
            graphics2D.setColor(Color.white);
            graphics2D.clearRect(0, 0, 150, 75);
            graphics2D.setFont(new Font("Arial", Font.TRUETYPE_FONT, 24));
            graphics2D.drawString(getDigits().toString(), 10, 17);
            ImageIO.write(img, "png", out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the digits
     */
    public Integer getDigits() {
        return digits;
    }

    /**
     * @param digits the digits to set
     */
    public void setDigits(Integer digits) {
        this.digits = digits;
    }
}
