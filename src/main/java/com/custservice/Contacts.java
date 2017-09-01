/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custservice;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author ISanhot
 */
@ManagedBean(name = "contacts", eager = true)
@SessionScoped

public class Contacts implements Serializable{

    private static long serialVersionUID = 1L;    
    private String emailField = "", phoneField = "", nameField = "", remarks = "", message = "";    

    /**
     * @return the emailField
     */
    public String getEmailField() {
        return emailField;
    }

    /**
     * @param emailField the emailField to set
     */
    public void setEmailField(String emailField) {
        this.emailField = emailField;
    }

    /**
     * @return the phoneField
     */
    public String getPhoneField() {
        return phoneField;
    }

    /**
     * @param phoneField the phoneField to set
     */
    public void setPhoneField(String phoneField) {
        this.phoneField = phoneField;
    }

    /**
     * @return the nameField
     */
    public String getNameField() {
        return nameField;
    }

    /**
     * @param nameField the nameField to set
     */
    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
