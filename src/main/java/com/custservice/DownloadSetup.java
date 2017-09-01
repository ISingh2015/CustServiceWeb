/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ISanhot
 */
@ManagedBean(name = "downloadSetup")
@SessionScoped
public class DownloadSetup implements Serializable{

    private static long serialVersionUID = 1L;    
    private static final int DEFAULT_BUFFER_SIZE = 10240;

    public void downloadFile() {
        NavigationManager navigationManager = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        String key = navigationManager.getDownloadKey();
        if (key.equals("inv")) {
            downLoadInv();
        } else if (key.equals("acc")) {
            downLoadAcc();
        } else if (key.equals("pay")) {
            downLoadPay();
        }
    }

    private void downLoadInv() {
        if (new File(NavigationManager.FILEPATHINV).exists()) {
            downloadFileToClient(NavigationManager.FILEPATHINV);
        }
    }

    private void downLoadAcc() {
        if (new File(NavigationManager.FILEPATHACC).exists()) {
            downloadFileToClient(NavigationManager.FILEPATHACC);
        }
    }

    private void downLoadPay() {
        if (new File(NavigationManager.FILEPATHPAY).exists()) {
            downloadFileToClient(NavigationManager.FILEPATHPAY);
        }
    }

    private void downloadFileToClient(String filename) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context
                .getExternalContext().getResponse();
        File file = new File(filename);
        BufferedInputStream input;
        BufferedOutputStream output;

        try {
            if (!file.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.reset();
            response.setBufferSize(DEFAULT_BUFFER_SIZE);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "attachment;filename=\""
                    + file.getName() + "\"");

            input = new BufferedInputStream(new FileInputStream(file),
                    DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(),
                    DEFAULT_BUFFER_SIZE);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            input.close();
            output.close();
            context.responseComplete();
        } catch (IOException e) {
        }
    }

}
