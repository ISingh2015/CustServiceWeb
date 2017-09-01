/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custservice.vo;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

/**
 *
 * @author ISanhot
 */
public class MediaData implements Serializable{

    private Integer Width = 110;
    private Integer Height = 50;
    private Color Background = new Color(190, 214, 248);
    private Color DrawColor = new Color(0, 0, 0);
    private Font font = new Font("Arial", Font.TRUETYPE_FONT, 30);

    /**
     * @return the Width
     */
    public Integer getWidth() {
        return Width;
    }

    /**
     * @param Width the Width to set
     */
    public void setWidth(Integer Width) {
        this.Width = Width;
    }

    /**
     * @return the Height
     */
    public Integer getHeight() {
        return Height;
    }

    /**
     * @param Height the Height to set
     */
    public void setHeight(Integer Height) {
        this.Height = Height;
    }

    /**
     * @return the Background
     */
    public Color getBackground() {
        return Background;
    }

    /**
     * @param Background the Background to set
     */
    public void setBackground(Color Background) {
        this.Background = Background;
    }

    /**
     * @return the DrawColor
     */
    public Color getDrawColor() {
        return DrawColor;
    }

    /**
     * @param DrawColor the DrawColor to set
     */
    public void setDrawColor(Color DrawColor) {
        this.DrawColor = DrawColor;
    }

    /**
     * @return the font
     */
    public Font getFont() {
        return font;
    }

    /**
     * @param font the font to set
     */
    public void setFont(Font font) {
        this.font = font;
    }

}
