/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custservice;

import com.cust.domain.vo.ElegantUser;
import com.cust.persistance.PersistanceManager;
import com.cust.persistance.managers.LoginManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ISanhot
 */
@ManagedBean(name = "newUser", eager = true)
@SessionScoped
public class NewUserBean implements Serializable {

    private static long serialVersionUID = 1L;
    private String name = "", address = "", state = "", city = "", country = "", emaiil = "", webSite = "", loginName = "", loginPwd = "", confirmLoginPwd = "", pinCode = "", telNo = "", mobileNo = "";

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the emaiil
     */
    public String getEmaiil() {
        return emaiil;
    }

    /**
     * @param emaiil the emaiil to set
     */
    public void setEmaiil(String emaiil) {
        this.emaiil = emaiil;
    }

    /**
     * @return the webSite
     */
    public String getWebSite() {
        return webSite;
    }

    /**
     * @param webSite the webSite to set
     */
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

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
     * @return the loginPwd
     */
    public String getLoginPwd() {
        return loginPwd;
    }

    /**
     * @param loginPwd the loginPwd to set
     */
    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String saveUser() {
        String errMsg = "";
        boolean error = false;
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        custNavigation.setGeneralMessage("");

        LoginBean loginBean = (LoginBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginBean");
        if (getName() == null || getName().equals("")) {
            errMsg += "Name field";
            error = true;
        }
        if (getLoginName() == null || getLoginName().equals("")) {
            errMsg += ", Login Name field";
            error = true;
        }
        if (getLoginPwd() == null || getLoginPwd().equals("") || getConfirmLoginPwd() == null || getConfirmLoginPwd().equals("")) {
            errMsg += ", Pssword field";
            error = true;
        }
        if (getMobileNo() == null || getMobileNo().equals("")) {
            errMsg += ", Mobile No field";
            error = true;
        }
        if (getEmaiil() == null || getEmaiil().equals("")) {
            errMsg += ", Email field";
            error = true;
        }
        if (error) {
            errMsg = "Manditory " + errMsg;
            loginBean.setErrorMsg(errMsg);
            return "";
        }
        if (getEmaiil().contains("@@") || getEmaiil().contains("..") || !custNavigation.isValidEmailField(emaiil)) {
            errMsg = "Enterd email is invalid";
            loginBean.setErrorMsg(errMsg);
            return "";
        }
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,10}";
        String s = new String(getLoginPwd().getBytes()), s1 = new String(getConfirmLoginPwd().getBytes());
        if (!s.matches(pattern)) {
            errMsg = "Password should be atleast 8 chars, must contain one lower case, one upper case, one digit and no spaces";
            loginBean.setErrorMsg(errMsg);
            return "";
        }
        if (getLoginName().contains(s)) {
            errMsg = "Password cannot contain Login Name";
            loginBean.setErrorMsg(errMsg);
            return "";
        }
        if (!s.equals(s1)) {
            errMsg = "Password and Confirm Password do not match";
            loginBean.setErrorMsg(errMsg);
            return "";
        }
        if (getMobileNo().trim().equals(getTelNo().trim())) {
            errMsg = "Telephone No and Mobile No cannot be the Same";
            loginBean.setErrorMsg(errMsg);
            return "";

        }
        error = false;
        try {
            String s2 = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
            PersistanceManager.getInstance().setLoginServer(s2);

            LoginManager loginManager = new LoginManager();
            loginManager.getPreferenceList(); // common perference from server. including service names                
            ArrayList<ElegantUser> userList = loginManager.getUserByName(getLoginName());
            if (!userList.isEmpty()) {
                loginBean.setErrorMsg("User Name Already exist ");
                error = true;
            }
            userList = loginManager.getUserByEmail(getEmaiil());
            if (!userList.isEmpty()) {
                loginBean.setErrorMsg("Email address is already taken");
                error = true;
            }
            userList = loginManager.getUserByMobile(getMobileNo());
            if (!userList.isEmpty()) {
                loginBean.setErrorMsg("Mobile No already exist");
                error = true;
            }

            if (error) {
                return "";
            }
            byte[] encoded = Base64.getEncoder().encode(getLoginPwd().getBytes());
            userList = new ArrayList<ElegantUser>();
            ElegantUser elegantUser = new ElegantUser();
            elegantUser.setCompID(9999l);
            elegantUser.setUserName(getName());
            elegantUser.setUserLoginName(getLoginName());
            elegantUser.setUserLoginPwd(new String(encoded));
            elegantUser.setEmailId(getEmaiil());
            elegantUser.setWebSite("");
            elegantUser.setMobileNo(getMobileNo());
            elegantUser.setUserAddress(getAddress());
            elegantUser.setPinCode(Integer.getInteger(getPinCode()));
            elegantUser.setCountry(getCountry());
            elegantUser.setCity(getCity());
            elegantUser.setTelephoneNo(getTelNo());
            elegantUser.setState(getState());
            elegantUser.setAccountLocked(0);
            elegantUser.setGracePeriod(30);
            elegantUser.setAccountStatus(0);
            elegantUser.setAccountType(0);
            userList.add(elegantUser);
            loginManager.saveUserList(userList, true);
            custNavigation.setGeneralMessage("User account Created Successfully. Please login with your credentails to continue.");
            initAll();
            loginBean.setErrorMsg("");
        } catch (Exception e) {
            custNavigation.setGeneralMessage("Could not Login, server might be down. Please try again Later. Thank you.");
        }
        return "/default?faces-redirect=true";
    }

    private void initAll() {
        setName("");
        setLoginName("");
        setLoginPwd("");
        setConfirmLoginPwd("");
        setAddress("");
        setCity("");
        setCountry("");
        setEmaiil("");
        setPinCode("");
        setState("");
        setMobileNo("");
        setTelNo("");
    }

    public String cancelSave() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        if (custNavigation.getPreviousPage().equals(""))  custNavigation.setPreviousPage("/default?faces-redirect=true");
        return custNavigation.getPreviousPage();
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the pinCode
     */
    public String getPinCode() {
        return pinCode;
    }

    /**
     * @param pinCode the pinCode to set
     */
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    /**
     * @return the telNo
     */
    public String getTelNo() {
        return telNo;
    }

    /**
     * @param telNo the telNo to set
     */
    public void setTelNo(String telNo) {
        this.telNo = telNo;
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
     * @return the confirmLoginPwd
     */
    public String getConfirmLoginPwd() {
        return confirmLoginPwd;
    }

    /**
     * @param confirmLoginPwd the confirmLoginPwd to set
     */
    public void setConfirmLoginPwd(String confirmLoginPwd) {
        this.confirmLoginPwd = confirmLoginPwd;
    }

}
