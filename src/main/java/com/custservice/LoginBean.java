/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custservice;

import com.cust.domain.vo.ElegantUser;
import com.cust.persistance.CustServiceConstants;
import com.cust.persistance.PersistanceManager;
import com.cust.persistance.managers.LoginManager;
import com.custservice.vo.MediaBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ISanhot
 */
@ManagedBean(name = "loginBean", eager = true)
@SessionScoped
public class LoginBean implements Serializable {

    private static long serialVersionUID = 1L;
    private boolean userLoggedIn = false;
    private String loginName = "", loginPassword = "", loginPasswordReset = "", errorMsg = "", mobileNo = "", emailAddress = "";
    private ElegantUser elegantUser;

    /**
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * @param loginName the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * @return the loginPassword
     */
    public String getLoginPassword() {
        return loginPassword;
    }

    /**
     * @param loginPassword the loginPassword to set
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String doResetPwd() {
        if (getLoginName() == null || getLoginName().equals("") || getLoginPassword() == null || getLoginPassword().equals("") || getLoginPasswordReset() == null || getLoginPasswordReset().equals("")) {
            setErrorMsg("Please Enter Reset Credentials");
            return "";
        }
        if (!userFound()) {
            return "";
        }
        if (doUpdatePasswd()) {
            initAll();
            return "/pges/loginpanel?faces-redirect=true";
        } else {
            return "";
        }

    }

    public String doforgotPwd() {
        if (getLoginName() == null || getLoginName().equals("")) {
            setErrorMsg("Please Enter Signin Id or Email Id");
            return "";
        }
        ElegantUser user = userFoundByNameOrEmail();
        if (user == null) {
            return "";
        }
        if (doUpdateForgotPasswd(user)) {
            initAll();
            return "/pges/loginpanel?faces-redirect=true";
        } else {
            return "";
        }

    }

    private void initAll() {
        setLoginName("");
        setLoginPassword("");
        setLoginPasswordReset("");
        setErrorMsg("");
    }

    private boolean doUpdatePasswd() {
        boolean updated = false;
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        try {
            LoginManager loginManager = new LoginManager();
            ArrayList<ElegantUser> userList = loginManager.getUserByName(loginName);
            String encoded = new String(Base64.getEncoder().encode(loginPassword.getBytes()));
            ElegantUser elegantUser = !userList.isEmpty() ? userList.get(0) : null;
            if (elegantUser == null) {
                return updated;
            }
            encoded = new String(Base64.getEncoder().encode(loginPasswordReset.getBytes()));
            elegantUser.setUserLoginPwd(encoded);
            loginManager.saveUserList(userList, false);
            custNavigation.setGeneralMessage("Password Re-set Successfull.Please use new password to login");
            updated = true;
        } catch (Exception e) {
            custNavigation.setGeneralMessage("Erro Occured while update. Please try again Later.. Thank you");
        }
        return updated;
    }

    private boolean doUpdateForgotPasswd(ElegantUser user) {
        boolean updated = false;
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        try {
            LoginManager loginManager = new LoginManager();
            user = loginManager.sendUserGeneratePassword(user);
            custNavigation.setGeneralMessage("New Password is sent to the registered Email. Please use new password to login");
            updated = true;
        } catch (Exception e) {
            custNavigation.setGeneralMessage("Erro Occured while update. Please try again Later.. Thank you");
        }
        return updated;
    }

    public String doResetCancel() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        custNavigation.setGeneralMessage("");
        setErrorMsg("");
        if (custNavigation.getPreviousPage().equals("")) {
            custNavigation.setPreviousPage("/default?faces-redirect=true");
        }
        return custNavigation.getPreviousPage();
    }

    public String doLogin() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        custNavigation.setGeneralMessage("");
        if (getLoginName() == null || getLoginName().equals("") || getLoginPassword() == null || getLoginPassword().equals("")) {
            setErrorMsg("Please Enter Login Credentials");
            return "/pges/loginpanel?faces-redirect=true";
        }
        if (!userFound()) {
            return "/pges/loginpanel?faces-redirect=true";
        }
        if (custNavigation.getLoggedIn().equalsIgnoreCase("SignIn")) {
            custNavigation.setLoggedIn("SignOut");
            custNavigation.setLoginName(loginName);
            custNavigation.setLoginPassword(loginPassword);
        }
        LoginBean loginBean = (LoginBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginBean");
        if (loginBean != null) {
            loginBean.setErrorMsg("");
        }
        return custNavigation.getCalledPage();
    }

    private boolean userFound() {
        boolean found = false;
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        MediaBean mediaBean = (MediaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mediaBean");
        custNavigation.setGeneralMessage("");
//        String s = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
        String s = CustServiceConstants.URL;
        PersistanceManager.getInstance().setLoginServer(s);
        LoginManager loginManager = new LoginManager();
        try {
            loginManager.getPreferenceList(); // common perference from server. including service names
            String encoded = new String(Base64.getEncoder().encode(loginPassword.getBytes()));
            ElegantUser elegantUser = loginManager.dogetUserByCredentials(loginName, encoded);
            if (elegantUser == null) {
                setErrorMsg("Invalid User Name or Password. Please retry");
                return found;
            }
            if (elegantUser.getAccountStatus() == 1) {
                setErrorMsg("Your Account is Frozen or not accessible");
                return found;
            } else if (elegantUser.getAccountLocked() != 0) {
                setErrorMsg("Your Account is Locked");
                return found;
            }
            if (mediaBean!= null && (!custNavigation.getVerifyCode().trim().equals(mediaBean.getDigits().toString().trim()))) {
                setErrorMsg("Invalid Captcha. Please retry");
                return found;
            }

            if (validateData(elegantUser) && elegantUser.getAccountLocked() == 0) {
                elegantUser.setLoggedIn(1);
                HttpSession ses = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
                if (ses != null) {
                    ses.setAttribute("loggedInUser", elegantUser);
                }
                ArrayList<ElegantUser> userList = new ArrayList<ElegantUser>();
                userList.add(elegantUser);
                loginManager.saveUserList(userList, false);
                setElegantUser(elegantUser);
                found = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            custNavigation.setGeneralMessage("Could not Login. Please try again Later. Thank you.");
        }
        return found;
    }

    private ElegantUser userFoundByNameOrEmail() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        MediaBean mediaBean = (MediaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mediaBean");
        custNavigation.setGeneralMessage("");
        String s = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
        PersistanceManager.getInstance().setLoginServer(s);
        LoginManager loginManager = new LoginManager();
        try {
            loginManager.getPreferenceList(); // common perference from server. including service names
            ElegantUser user = loginManager.getUserByLoginOrEmail(loginName);
            if (user != null) {
                if (user.getAccountStatus() == 1) {
                    setErrorMsg("Your Account is Frozen or not accessible");
                    return null;
                } else if (user.getAccountLocked() != 0) {
                    setErrorMsg("Your Account is Locked");
                    return null;
                }
            } else {
                setErrorMsg("User Does not Exist.. Please retry");
                return null;
            }
            if (!custNavigation.getVerifyCode().trim().equals(mediaBean.getDigits().toString().trim())) {
                setErrorMsg("Invalid Captcha. Please retry");
                return null;
            }
            if (validateData(user) && user.getAccountLocked() == 0) {
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
            custNavigation.setGeneralMessage("Could not Login. Please try again Later. Thank you.");
        }
        return null;
    }

    private boolean validateData(ElegantUser user) {
        String errorText = "";
        if (user.getAccountStatus() == null || user.getMembershipDate() == null || user.getGracePeriod() == null) {
            errorText = "Error in User Account.";
            errorText += user.getAccountStatus() == null ? "\nStatus Field is Null " : "";
            errorText += user.getMembershipDate() == null ? "\nMemberShipDate is Null" : "";
            errorText += user.getGracePeriod() == null ? "\nGrace Period is Null" : "";
            errorText += "\nPlease email HelpDesk [eleganInfo@gmail.com] to get the issue resolved. ";
            setErrorMsg(errorText);
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date now = calendar.getTime();
        long secs = new Date().getTime();

        calendar.setTime(user.getMembershipDate());
        calendar.add(Calendar.DATE, user.getGracePeriod());
        Date userMemberShipDate = calendar.getTime();

        calendar.setTime(now);
        calendar.add(Calendar.DATE, user.getGracePeriod() * -1);

        if (user.getAccountType() >= 1 && user.getGracePeriod() > 365) {
            errorText = "The 365 day Membership Period is expired on " + userMemberShipDate.toString() + "\nPlease renew your membership by visiting www.elegantSoftware.in \nThank you for your co.operatioin.\n ";
            setErrorMsg(errorText);
        } else if (user.getAccountType() == 0 && user.getGracePeriod() > 365) {
            errorText = "The 30 day Trial Membership Period is expired on " + userMemberShipDate.toString() + "\nPlease renew your membership by visiting www.elegantSoftware.in \nThank you for your co.operatioin.\n ";
            setErrorMsg(errorText);
            return true;
        } else if (user.getAccountLocked() != 0) {
            setErrorMsg("Your Account is Locked");
            return true;
        } else if (user.getAccessInventory() == 0) {
            setErrorMsg("You do not have access to the Inventory Module");
            return true;
        }
        return true;
    }

    public String doLoginCancel() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        custNavigation.setGeneralMessage("");
        setErrorMsg("");
        if (custNavigation.getPreviousPage().equals("")) {
            custNavigation.setPreviousPage("/default?faces-redirect=true");
        }
        return custNavigation.getPreviousPage();
    }

    /**
     * @return the mobileNo
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * @param mobileNo the mobileNo to set
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @return the loginPasswordReset
     */
    public String getLoginPasswordReset() {
        return loginPasswordReset;
    }

    /**
     * @param loginPasswordReset the loginPasswordReset to set
     */
    public void setLoginPasswordReset(String loginPasswordReset) {
        this.loginPasswordReset = loginPasswordReset;
    }

    /**
     * @return the elegantUser
     */
    public ElegantUser getElegantUser() {
        return elegantUser;
    }

    /**
     * @param elegantUser the elegantUser to set
     */
    public void setElegantUser(ElegantUser elegantUser) {
        this.elegantUser = elegantUser;
    }

    /**
     * @return the userLoggedIn
     */
    public boolean isUserLoggedIn() {
        ElegantUser userInSession = null;
        HttpSession ses = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
        if (ses != null) {
            userInSession = (ElegantUser) ses.getAttribute("loggedInUser");
        }
        if (userInSession != null) {
            userLoggedIn = true;
        }
        return userLoggedIn;
    }

    /**
     * @param userLoggedIn the userLoggedIn to set
     */
    public void setUserLoggedIn(boolean userLoggedIn) {

        this.userLoggedIn = userLoggedIn;
    }
}
