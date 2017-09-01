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
@ManagedBean(name = "inventoryBuy", eager = true)
@SessionScoped
public class InventoryBuy implements Serializable{
    
    private static long serialVersionUID = 1L;    
    private String buyDesc="<b>Manage Online Customer and Supplier with our commerce inventory database.</b>\nNow all your Customer and Suppliers can be managed centrally and online, take them with you "
            + "anywhere anytime and create all your purchase orders and sales invoices online.";

    /**
     * @return the buyDesc
     */
    public String getBuyDesc() {
        return buyDesc;
    }

    /**
     * @param buyDesc the buyDesc to set
     */
    public void setBuyDesc(String buyDesc) {
        this.buyDesc = buyDesc;
    }

    
}
