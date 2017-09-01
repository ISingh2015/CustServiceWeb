/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custservice;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ISanhot
 */
public class CustRequestPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent pe) {
    }

    @Override
    public void beforePhase(PhaseEvent pe) {
        FacesContext context = pe.getFacesContext();
        ExternalContext ext = context.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) ext.getResponse();
        response.addHeader("Content-Type", "width=device-width, initial-scale=1.0");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
