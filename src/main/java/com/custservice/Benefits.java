/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custservice;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author ISanhot
 */
@ManagedBean(name = "benefits", eager = true)
@RequestScoped
public class Benefits implements Serializable{
    
    private static long serialVersionUID = 1L;    
    private String benefitsDesc="<b>Online Customer and Supplier Management.</b>\nCustomer Contact Information can be stored online on Elegant Server databases, which can be "
            + "used systemwide for creating purchase orders and sales invoices.";

    /**
     * @return the benefitsDesc
     */
    public String getBenefitsDesc() {
        return benefitsDesc;
    }

    /**
     * @param benefitsDesc the benefitsDesc to set
     */
    public void setBenefitsDesc(String benefitsDesc) {
        this.benefitsDesc = benefitsDesc;
    }
    
}
