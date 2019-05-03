/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

/**
 *
 * @author Sophia
 */
public class FacesUtil {
    
    /**
     * 
     * @param severity
     * @param summary
     * @param detail 
     */
    public static void showMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesMessage msg = new FacesMessage(severity, summary, detail);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        
        Flash flash = externalContext.getFlash();
        flash.setKeepMessages(true);
        
        facesContext.addMessage(null, msg);
    }
}
