package com.custservice;

import com.cust.domain.vo.ElegantHitCounter;
import com.cust.persistance.PersistanceManager;
import com.cust.persistance.managers.LoginManager;
import com.custservice.vo.InvImageDescVO;
import com.custservice.vo.MediaBean;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ISanhot
 */
@ManagedBean(name = "custNavigation", eager = true)
@SessionScoped
public class NavigationManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private String appName = "DocBuilder System", imageFileToDisplay, formattedHitCounts;
    private ArrayList<InvImageDescVO> invImageAndDescrList;
    private String errorServletName, errorCode, errorMsg, errorException, errorType, errorURI;

    public String getFormattedHitCounts() {
        float no = this.getHitCount();
        if (!this.isHitIncremented()) {
            this.incrementHit();
        }
        DecimalFormat df;
        if (no <= 99999) {
            df = new DecimalFormat("##,000");
            formattedHitCounts = df.format(no);
        } else if (no >= 100000 && no < 999999) {
            df = new DecimalFormat("##.#####");
            no = no / 100000;
            formattedHitCounts = df.format(no) + " K";
        } else {
            df = new DecimalFormat("##.#######");
            no = no / 1000000;
            formattedHitCounts = df.format(no) + " M";
        }
//        return String.format("%010d", Long.parseLong(df.format(no)));//df.format(no);
        return formattedHitCounts;
    }

    public void setFormattedHitCounts(String formattedHitCounts) {
        this.formattedHitCounts = formattedHitCounts;
    }

    public String getErrorServletName() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        errorServletName = (String) context.getRequestMap().get("javax.servlet.error.servlet_name");
        return errorServletName;
    }

    public void setErrorServletName(String errorServletName) {
        this.errorServletName = errorServletName;
    }

    public String getErrorURI() {
        return errorURI;
    }

    public void setErrorURI(String errorURI) {
        this.errorURI = errorURI;
    }

    public String getErrorCode() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        errorCode = String.valueOf((Integer) context.getRequestMap().get("javax.servlet.error.status_code"));
        return errorCode;
    }

    public String getErrorType() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        errorType = (String) context.getRequestMap().get("javax.servlet.error.exception.type");
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMsg() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        errorMsg = (String) context.getRequestMap().get("javax.servlet.error.message");
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorException() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        errorException = (String) ((Exception) context.getRequestMap().get("javax.servlet.error.exception")).toString();
        return errorException;
    }

    public void setErrorException(String errorException) {
        this.errorException = errorException;
    }

    private final String appImage = "/images/elegant48.png";
    private String copyRight;
    private final String[] menuImage = {"/images/home.png", "/images/elegant48.png", "/images/elegant48.png", "/images/elegant48.png", "/images/elegant48.png", "/images/elegant48.png", "/images/contactme.png"};
    private final String[] invImage = {"/images/login.png", "/images/salesman.png", "/images/salesmanrep.png", "/images/supplier.png", "/images/supplierRep.png", "/images/customer.png",
        "/images/customerRep.png", "/images/purchase.png", "/images/purchaserep.png", "/images/purchasebillselect.png", "/images/invoice.png",
        "/images/invoicerep.png", "/images/invoicerepselect.png", "/images/purchasertn.png", "/images/purchasertnrep.png", "/images/purchasertnrepselect.png",
        "/images/invoicertn.png", "/images/invoicertnrep.png", "/images/invoicertnrepselect.png", "/images/admin.png", "/images/adminrep.png",
        "/images/helpscrsample.png"
    };

    private CustPropertyCache custProperties;
    private Long hitCount;
    private int imageCnt = 0;
    private boolean hitIncremented = false;
    private String loginName = "", loginPassword = "", loggedIn = "", tripleclickImage = "/images/logo-TCv3.gif", tripleClickURL = "http://www.tripleclicks.com/10925328/";
    private String generalMessage = ""; // used to pop messages to clients via ajax pull
    private String terms = "", privacy = "", refund = "", disclaimer = "";

    private final String[] tripleClickImages = {"/images/logo-TCv31.png", "/images/logo-TCv32.jpg", "/images/logo-TCv33.jpg", "/images/logo-TCv35.jpg",
        "/images/logo-TCv36.jpg", "/images/logo-TCv37.jpg", "/images/logo-TCv38.png", "/images/logo-TCv39.png", "/images/logo-TCv40.jpg"};

    private String[] tripleClickURLs = {"http://www.tripleclicks.com/10925328/", "http://www.tripleclicks.com/10925328/", "http://www.tripleclicks.com/10925328/", "http://www.tripleclicks.com/10925328/",
        "http://www.tripleclicks.com/10925328/", "http://www.tripleclicks.com/10925328/", "http://www.tripleclicks.com/10925328/",
        "http://www.tripleclicks.com/10925328/", "http://www.tripleclicks.com/10925328/", "http://www.tripleclicks.com/10925328/"};
    private String downloadKey;
    private String verifyCode = "";
    private boolean mobileDevice;
    public static final String FILEPATHINV = "e:\\elegantsetups\\ElegantInventory_windows_1_0_0.exe";
    public static final String FILEPATHACC = "e:\\elegantsetups\\ElegantAccounts_windows_1_0_0 .exe";
    public static final String FILEPATHPAY = "e:\\elegantsetups\\ElegantPayroll_windows_1_0_0 .exe";
    private boolean pollEnabled = true;
    private ArrayList<String> fileStats = new ArrayList<String>();
    private ElegantHitCounter elegantHitCounter;
    private String previousPage = "", calledPage = "";
    private String downloadFileURLImage = "/images/download-icon.png";

    public NavigationManager() {
        createInventoryDat();
    }

    public String getDate() {
        return new SimpleDateFormat("EEE yyyy MMMMM dd hh:mm:ss aaa").format(new java.util.Date());
    }

    public String loginPage() {
        setGeneralMessage("");
        LoginBean loginBean = (LoginBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginBean");
        if (loginBean != null) {
            loginBean.setErrorMsg("");
        }
        if (getLoggedIn().equalsIgnoreCase("SignIn")) {
            return "/pges/loginpanel?faces-redirect=true";
        } else {
            doLogout();
            return "/default?faces-redirect=true";
        }

    }

    private void doLogout() {
        setLoginName("");
        setLoginPassword("");
        setGeneralMessage("");
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        custNavigation.setLoggedIn("SignIn");
        custNavigation.setLoginName(getLoginName());
        custNavigation.setLoginPassword(getLoginPassword());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        setLoggedIn("SignIn");

    }

    public String homePage() {
        setPreviousPage("/default?faces-redirect=true");
        return "/default?faces-redirect=true";
    }

    public String aboutPage() {
        setGeneralMessage("");
        return "/pges/about?faces-redirect=true";
    }

    public String inventoryPage() {
        setGeneralMessage("");
        setCalledPage("/pges/inventory?faces-redirect=true");
        return getCalledPage();
    }

    public String inventoryBuyPage() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        setGeneralMessage("");
        custNavigation.setDownloadKey("inv");
        createFileStats(FILEPATHINV);
        setCalledPage("/pges/inventorybuy?faces-redirect=true");
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        boolean foundDevice = req.getHeader("User-Agent").indexOf("Mobile") != -1;
        setMobileDevice(foundDevice);
        if (getLoggedIn().equalsIgnoreCase("SignIn")) {
            return "/pges/loginpanel?faces-redirect=true";
        }
        return getCalledPage();
    }

    public String accountingPage() {
        setGeneralMessage("");
        setPreviousPage("/pges/accounting?faces-redirect=true");
        return "/pges/accounting?faces-redirect=true";
    }

    public String accountingBuyPage() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        setGeneralMessage("");
        custNavigation.setDownloadKey("acc");
        createFileStats(FILEPATHACC);
        setCalledPage("/pges/accountingbuy?faces-redirect=true");
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        boolean foundDevice = req.getHeader("User-Agent").indexOf("Mobile") != -1;
        setMobileDevice(foundDevice);
        if (getLoggedIn().equalsIgnoreCase("SignIn")) {
            return "/pges/loginpanel?faces-redirect=true";
        }
        return getCalledPage();
    }

    public String payrollPage() {
        setGeneralMessage("");
        return "/pges/payroll?faces-redirect=true";
    }

    public String payrollBuyPage() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        setGeneralMessage("");
        custNavigation.setDownloadKey("pay");
        createFileStats(FILEPATHPAY);
        setCalledPage("/pges/payrollbuy?faces-redirect=true");
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        boolean foundDevice = req.getHeader("User-Agent").indexOf("Mobile") != -1;
        setMobileDevice(foundDevice);
        if (getLoggedIn().equalsIgnoreCase("SignIn")) {
            return "/pges/loginpanel?faces-redirect=true";
        }
        return getCalledPage();

    }

    public String benefitsPage() {
        setGeneralMessage("");
        setPreviousPage("/pges/benefits?faces-redirect=true");
        return "/pges/benefits?faces-redirect=true";
    }

    public String downloadPage() {
        setGeneralMessage("");
        setPreviousPage("/pges/downloadPage?faces-redirect=true");
        return "/pges/downloadPage?faces-redirect=true";
    }

    public String contactPage() {
        setGeneralMessage("");
        setPreviousPage("/pges/contactPage?faces-redirect=true");
        return "/pges/contactPage?faces-redirect=true";
    }

    public String newUserPage() {
        setGeneralMessage("");
        LoginBean loginBean = (LoginBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginBean");
        if (loginBean != null) {
            loginBean.setErrorMsg("");
        }
        return "/pges/newupage?faces-redirect=true";
    }

    public String resetPage() {
        LoginBean loginBean = (LoginBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginBean");
        setGeneralMessage("");
        loginBean.setErrorMsg("");
        return "/pges/resetpwdPage?faces-redirect=true";
    }

    public String forgotPwdPage() {
        LoginBean loginBean = (LoginBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginBean");
        setGeneralMessage("");
        loginBean.setErrorMsg("");
        return "/pges/forgotpwdPage?faces-redirect=true";
    }

    public String downloadInvPage() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        setGeneralMessage("");
        custNavigation.setDownloadKey("inv");
        createFileStats(FILEPATHINV);
        setCalledPage("/pges/downloadPage?faces-redirect=true");
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("header " + req.getHeader("User-Agent"));
        boolean foundDevice = req.getHeader("User-Agent").indexOf("Mobile") != -1;
        setMobileDevice(foundDevice);
        if (getLoggedIn().equalsIgnoreCase("SignIn")) {
            return "/pges/loginpanel?faces-redirect=true";
        }
        return getCalledPage();
    }

    public String downloadAccPage() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        setGeneralMessage("");
        custNavigation.setDownloadKey("acc");
        createFileStats(FILEPATHACC);
        setCalledPage("/pges/downloadPage?faces-redirect=true");
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        boolean foundDevice = req.getHeader("User-Agent").indexOf("Mobile") != -1;
        setMobileDevice(foundDevice);

        if (getLoggedIn().equalsIgnoreCase("SignIn")) {
            return "/pges/loginpanel?faces-redirect=true";
        }

        return getCalledPage();
    }

    public String downloadPayPage() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        setGeneralMessage("");
        custNavigation.setDownloadKey("pay");
        createFileStats(FILEPATHPAY);
        setCalledPage("/pges/downloadPage?faces-redirect=true");
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        boolean foundDevice = req.getHeader("User-Agent").indexOf("Mobile") != -1;
        setMobileDevice(foundDevice);

        if (getLoggedIn().equalsIgnoreCase("SignIn")) {
            return "/pges/loginpanel?faces-redirect=true";
        }

        return getCalledPage();
    }

    /**
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return the appImage
     */
    public String getAppImage() {
        return appImage;
    }

    public String[] getInvImage() {
        return invImage;
    }

    public String[] getInvImageDescr() {
        return invImage;
    }

    private void incrementHit() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        LoginManager loginManager = new LoginManager();
        try {
            elegantHitCounter.setHitCounter(elegantHitCounter.getHitCounter() + 1);
            elegantHitCounter = loginManager.saveElegantHitCounter(elegantHitCounter);
            setHitCount(elegantHitCounter.getHitCounter());
        } catch (Exception e) {
            custNavigation.setGeneralMessage("Erro Occured while update. Please try again Later.. Thank you");
        }
//        System.out.println("setting incremented " + this.isHitIncremented());
        this.setHitIncremented(true);
    }

    private Long getHitCount() {
        NavigationManager custNavigation = (NavigationManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("custNavigation");
        String s = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
        PersistanceManager.getInstance().setLoginServer(s);
        LoginManager loginManager = new LoginManager();
        loginManager.getPreferenceList(); // common perference from server. including service names
        elegantHitCounter = new ElegantHitCounter();
        elegantHitCounter.setHitID(5000);
        try {
            elegantHitCounter = loginManager.getElegantHitCounter(elegantHitCounter);
        } catch (Exception e) {
            custNavigation.setGeneralMessage("Erro Occured while update. Please try again Later.. Thank you");
        }
        setHitCount(elegantHitCounter.getHitCounter());
        return this.hitCount;
    }

    private void setHitCount(Long hitCount) {
        this.hitCount = hitCount;
    }

    /**
     * @return the copyRight
     */
    public String getCopyRight() {

        return copyRight;
    }

    /**
     * @param copyRight the copyRight to set
     */
    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    public String getInvImageDescr(int option) {
        if (option == 0) {
            return "<b>SignIn Window</b> SignIn to the elegant Inventory module"
                    + "with your credentials, the SignIn screen provides access to elegant modules Screens, Reports, Consolidated Reports and Administration modules."
                    + "If a user is a superuser then he has access to the ADMINISTRATION module with which he could create other users and provide them access to the elegant modules"
                    + "and reports, update their passwords etc... within the limits as specified by the elegant user Login. All Users and  credentials created through the website are stored on elegant servers."
                    + "</br>"
                    + "To do the above, login to our website and create the credentials of your choice.You will need a valid email address and mobile no to create your login.";
        } else if (option == 1) {
            return "<b>Sales Man </b> Create-Update-Remove Sales Person. The sales person once created is used for recording Orders, Invoices & returns. An existing sales person appears in the list as shown in the image and can be updated but DOUBLE CLICKING, buttons are reassigned automatically and the sales person can be edited or deleted (in case of no transactions). On completing the update click the SAVE button. The enteries in the listing table will be disabled till the update is completed or disgarded. Text Filed and number fileds on the forms follow validations, help button on the form is used to select a manager code for the sales person.  budgeting is possible and will be made available in the accounting module. To view a List or actvie or inactive sales people, simply press the PRINT LIST  button or select the option from the Menu and on the appearing report-filter popup window click the Ok button to view the report to the screen. Printing hardcopies to shared or connected devices, exporting of the report is possible to different formats (e.g. PDf, Excel, Word.). Report layout can be changed depending on hardcopy printout requirements as shown in the image below.";
        } else if (option == 2) {
            return "<b>Sales Person Report Filter Panel</b> This produces a listing of Sales people within your oganisation, To filter the report, sort the report apply the criteria in the window before clicking the ok button. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the create dates as this is important. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 3) {
            return "<b>Supplier</b> Create-Update-Remove Suppliers. The supplier onces created can be used to raise orders. An existing supplier can be edited by  DOUBLE CLICKING in the list below as shown in the screen or deleted (In case of no ORDER or INVOICE transactions found). A unique code in generated for each supplier along with the name and address information. Email fields and website fields are important and are used for mailing orders intimations to supplier websites or email domains. If enrolled suppliers could approve or reject an order using external portal). To view a List or actvie or inactive suppliers, simply press the PRINT LIST  button or select the option from the Menu and on the appearing report-filter popup window click the Ok button to view the report to the screen. Printing hardcopies to shared or connected devices, exporting of the report is possible to different formats (e.g. PDf, Excel, Word.). Report layout can be changed depending on hardcopy printout requirements as shown in the image below.";
        } else if (option == 4) {
            return "<b>Supplier Report Filter Panel</b> This produces a listing of Supplier within your oganisation, To filter the report, sort the report apply the criteria in the window before clicking the ok button. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the create dates as this is important. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 5) {
            return "<b>Customer</b> Create-Update-Remove Customers. Once created are used to raise invoice depending on the sale of an existing product. The invoice module is exlained below. A unique code is generated for each Customer, with the address, email and website information. These are validated and can be edited anytime from within the system by any administrator or user. To edit an existing customer DOUBLE CLICK on the itme in the list as shown in the figure on the left. The buttons in the action panel are enable or disabled depending on the assigned rights. On completing the update click the SAVE button. The enteries in the listing table will be disabled till the update is completed or disgarded. Email fields and website fields are important and are used for mailing order intimations to supplier websites or email domains. If enrolled suppliers could approve or reject an order using external portal). To view a List or actvie or inactive sales people, simply press the PRINT LIST  button or select the option from the Menu and on the appearing report-filter popup window click the Ok button to view the report to the screen. Printing hardcopies to shared or connected devices, exporting of the report is possible to different formats (e.g. PDf, Excel, Word.). Report layout can be changed depending on hardcopy printout requirements as shown in the image below.";
        } else if (option == 6) {
            return "<b>Customer Report Filter Panel</b> This produces a listing of Customers within your oganisation, To filter the report, sort the report apply the criteria in the window before clicking the ok button. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the create dates as this is important. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 7) {
            return "<b>Order</b> The Order Data Entry Form. Create-Update-Remove Orders for your oganisation. To create new orders click the NEW button in the action panel. Once the order form is filled click the save button to save the order. If the user Signed in is a ADMINSITRATOR the authorisation panel appears and the use is allowed only to authorise the bill, deletion or update is only allowed to OPERATORS (see User ADMINISTRATION module below). The user can add as many as thousand line items in the order. With each line items there is a column showing the current stock available for the line item. The column at the end would show the total of the line item and the TOTAL field on the form would reflect the total of the ORDER. To view a List or active or inactive orders, simply press the PRINT LIST  button or select the option from the Menu and on the appearing report-filter popup window click the Ok button to view the report to the screen. Printing hardcopies to shared or connected devices, exporting of the report is possible to different formats (e.g. PDf, Excel, Word.). Report layout can be changed depending on hardcopy printout requirements as shown in the image below. There is also a PRINT BILL button which enables printing of single or multiple orders at one time.";
        } else if (option == 8) {
            return "<b>Order Report Filter Panel</b> This produces a listing of order within your oganisation, To filter the report, sort the report apply the criteria in the window before clicking the ok button. The Help buttons with ? on the screen will display a code selection panel for Sales Person or Supplier. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the create dates as this is important. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 9) {
            return "<b>Order Bill Print Filter Panel</b> This produces a Line-Item Order listing for your oganisation, To filter the report, apply the from - to criteria in the window before clicking the ok button. The Help buttons with ? on the screen will display a code selection panel allowing to select Bill Nos. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the Order Nos in the selection criteria. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 10) {
            return "<b>Invoice</b> The Invoice Data Entry Form. Create-Update-Remove invoices for your oganisation. To create new orders click the NEW button in the action panel. Once the order form is filled click the save button to save the order. If the user Signed in is a ADMINSITRATOR the authorisation panel appears and the use is allowed only to authorise the bill, deletion or update is only allowed to OPERATORS (see User ADMINISTRATION module below). The user can add as many as thousand line items in the order. With each line items there is a column showing the current stock available for the line item. The column at the end would show the total of the line item and the TOTAL field on the form would reflect the total of the ORDER. To view a List or active or inactive orders, simply press the PRINT LIST  button or select the option from the Menu and on the appearing report-filter popup window click the Ok button to view the report to the screen. Printing hardcopies to shared or connected devices, exporting of the report is possible to different formats (e.g. PDf, Excel, Word.). Report layout can be changed depending on hardcopy printout requirements as shown in the image below. There is also a PRINT BILL button which enables printing of single or multiple orders at one time.";
        } else if (option == 11) {
            return "<b>Invoice Report Filter Panel</b> This produces a listing of invoices, To filter the report, sort the report apply the criteria in the window before clicking the ok button. The Help buttons with ? on the screen will display a code selection panel for Sales Person or Supplier. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the create dates as this is important. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 12) {
            return "<b>Invoice Bill Print Filter Panel</b> This produces a Line-Item Invoice listing, To filter the report, apply the from - to criteria in the window before clicking the ok button. The Help buttons with ? on the screen will display a code selection panel allowing to select Bill Nos. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the Order Nos in the selection criteria. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 13) {
            return "The Order Return Data Entry Form. Create-Update-Remove Orders Returned for your oganisation. To create new orders click the NEW button in the action panel. Once the order form is filled click the save button to save the order. If the user Signed in is a ADMINSITRATOR the authorisation panel appears and the use is allowed only to authorise the bill, deletion or update is only allowed to OPERATORS (see User ADMINISTRATION module below). The user can add as many as thousand line items in the order. With each line items there is a column showing the current stock available for the line item. The column at the end would show the total of the line item and the TOTAL field on the form would reflect the total of the ORDER. To view a List or active or inactive orders, simply press the PRINT LIST  button or select the option from the Menu and on the appearing report-filter popup window click the Ok button to view the report to the screen. Printing hardcopies to shared or connected devices, exporting of the report is possible to different formats (e.g. PDf, Excel, Word.). Report layout can be changed depending on hardcopy printout requirements as shown in the image below. There is also a PRINT BILL button which enables printing of single or multiple orders at one time.";
        } else if (option == 14) {
            return "<b>Order Return Print Filter Panel</b> This produces a Order Return listing within your oganisation, To filter the report, apply the from - to criteria in the window before clicking the ok button. The Help buttons with ? on the screen will display a code selection panel for Sales Person or Supplier. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the Order Nos in the selection criteria. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 15) {
            return "<b>Order Return Bill Print Filter Panel</b> This produces a Line-Item Order Return listing, To filter the report, apply the from - to criteria in the window before clicking the ok button. The Help buttons with ? on the screen will display a code selection panel allowing to select Bill Nos. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the Order Nos in the selection criteria. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 16) {
            return "<b>Invoice Return</b> The Invoice Data Entry Form. Create-Update-Remove invoices for your oganisation. To create new orders click the NEW button in the action panel. Once the order form is filled click the save button to save the order. If the user Signed in is a ADMINSITRATOR the authorisation panel appears and the use is allowed only to authorise the bill, deletion or update is only allowed to OPERATORS (see User ADMINISTRATION module below). The user can add as many as thousand line items in the order. With each line items there is a column showing the current stock available for the line item. The column at the end would show the total of the line item and the TOTAL field on the form would reflect the total of the ORDER. To view a List or active or inactive orders, simply press the PRINT LIST  button or select the option from the Menu and on the appearing report-filter popup window click the Ok button to view the report to the screen. Printing hardcopies to shared or connected devices, exporting of the report is possible to different formats (e.g. PDf, Excel, Word.). Report layout can be changed depending on hardcopy printout requirements as shown in the image below. There is also a PRINT BILL button which enables printing of single or multiple orders at one time.";
        } else if (option == 17) {
            return "<b>Invoice Return Print Filter Panel</b> This produces a Invoice Return listing, To filter the report, apply the from - to criteria in the window before clicking the ok button. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the Order Nos in the selection criteria. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 18) {
            return "<b>Invoice Return Bill Print Filter Panel</b> This produces a Line-Item Invoice return listing, To filter the report, apply the from - to criteria in the window before clicking the ok button. The Help buttons with ? on the screen will display a code selection panel allowing to select Bill Nos. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the Order Nos in the selection criteria. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 19) {
            return "<b>User Administration</b> Create-Update-Remove Users for your organisation who are required access to the elegant inventory module, assign user access rights for the various inventory modules and reports. This user could be either enabled or disabled to grant or prevent login to the elegant inventory module.";
        } else if (option == 20) {
            return "<b>User Administration Report Filter Panel</b> This produces a listing of User in your oganisation, To filter the report, sort the report apply the criteria in the window before clicking the ok button. The report is displayed in seconds on your screen, depending on your internet connection speed. If nothing found is displayed try adjusting the create dates as this is important. To print hard copies of the report or navigate between pages, use the buttons in the top panel of the report view. To export the report use  the SAVE icon on the report view.";
        } else if (option == 21) {
            return "<b>Sample Help Screen</b>This is a help screen which popups when the user presses the Help Button on any of the form or sub forms (e.g. ones used for listing and reports). The list is searchable and user could enter a string in the text box on the top of the window as in the screen to filter the code selection. To select a code user needs to click on the item in the list and then click the OK button in the panel at the bottom, which will close the popup and return the user to the previous windodw.";
        }

        return "";
    }

    private void createInventoryDat() {
        for (int i = 0; i < invImage.length; i++) {
            InvImageDescVO iid = new InvImageDescVO();
            iid.setDescr(getInvImageDescr(i));
            iid.setName(getInvImage()[i]);
            getInvImageAndDescrList().add(iid);
//            System.out.println("added " + i);
        }
        setCopyRight("Copyrights  © " + new SimpleDateFormat("YYYY").format(new Date()) + " DocBuilderSoftware.in All rights reserved. Designed by Inderjit S.S.");
        getHitCount();
        setCalledPage("/default?faces-redirect=true");
    }

    /**
     * @return the invImageAndDescrList
     */
    public ArrayList<InvImageDescVO> getInvImageAndDescrList() {
        if (invImageAndDescrList == null) {
            invImageAndDescrList = new ArrayList<InvImageDescVO>();
        }
        return invImageAndDescrList;
    }

    /**
     * @param invImageAndDescrList the invImageAndDescrList to set
     */
    public void setInvImageAndDescrList(ArrayList<InvImageDescVO> invImageAndDescrList) {
        this.invImageAndDescrList = invImageAndDescrList;
    }

    public String openImage() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setImageFileToDisplay(context.getRequestParameterMap().get("fileName"));
        return "openimage?faces-redirect=true";
    }

    /**
     * @return the imageFileToDisplay
     */
    public String getImageFileToDisplay() {
        return imageFileToDisplay;
    }

    /**
     * @param imageFileToDisplay the imageFileToDisplay to set
     */
    public void setImageFileToDisplay(String imageFileToDisplay) {
        this.imageFileToDisplay = imageFileToDisplay;
    }

    /**
     * @return the custProperties
     */
    public CustPropertyCache getCustProperties() {
        if (this.custProperties == null) {
            this.custProperties = new CustPropertyCache();
        }
        return custProperties;
    }

    /**
     * @param custProperties the custProperties to set
     */
    public void setCustProperties(CustPropertyCache custProperties) {
        this.custProperties = custProperties;
    }

    /**
     * @return the hitIncremented
     */
    public boolean isHitIncremented() {
        return hitIncremented;
    }

    /**
     * @param hitIncremented the hitIncremented to set
     */
    public void setHitIncremented(boolean hitIncremented) {
        this.hitIncremented = hitIncremented;
    }

    /**
     * @return the menuImage
     */
    public String[] getMenuImage() {
        return menuImage;
    }

//    /**
//     * @param menuImage the menuImage to set
//     */
//    public void setMenuImage(String[] menuImage) {
//        this.menuImage = menuImage;
//    }
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
     * @return the loggedIn
     */
    public String getLoggedIn() {
        if ("".equals(loggedIn)) {
            this.loggedIn = "SignIn";
        }
        return loggedIn;
    }

    /**
     * @param loggedIn the loggedIn to set
     */
    public void setLoggedIn(String loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * @return the generalMessage
     */
    public String getGeneralMessage() {
        return generalMessage;
    }

    /**
     * @param generalMessage the generalMessage to set
     */
    public void setGeneralMessage(String generalMessage) {
        this.generalMessage = generalMessage;
    }

    /**
     * @return the tripleclickImage
     */
    public String getTripleclickImage() {
        if (imageCnt >= 9) {
            imageCnt = 0;
        }
        tripleclickImage = tripleClickImages[imageCnt];
        imageCnt++;
        return tripleclickImage;
    }

    /**
     * @param tripleclickImage the tripleclickImage to set
     */
    public void setTripleclickImage(String tripleclickImage) {
        this.tripleclickImage = tripleclickImage;
    }

    /**
     * @return the terms
     */
    public String getTerms() {
        terms = "<b>DISCLAIMER</b>\n"
                + "\n"
                + "The use of this site, and the terms and conditions for our providing information, is governed by the information on this web page (hereinafter \"Disclaimer\"). By using this site, you acknowledge that you have read this Disclaimer and that you accept and will be bound by the terms hereof. www.DocBuilderSoftware.in has prepared this Web site for general information purposes, and specifically not as health or medical advice.\n"
                + "\n"
                + "Information provided by www.DocBuilderSoftware.in is for informational purposes only and is not a substitute for professional medical advice. All Rights Reserved. Always consult your doctor before starting any major change in your diet, health programs, or any course of supplementation or treatment, particularly if you are currently under medical care. Only your doctor should evaluate your health problems and prescribe treatment. Statements about products and health conditions have not been evaluated by the U.S. Food & Drug Administration, nor has the FDA evaluated health claims for these products. The FDA has not reviewed or approved these products to diagnose, cure or prevent disease. Please consult your healthcare provider before starting any course of supplementation or treatment, particularly if you are currently under medical care. Make sure you carefully read all product packaging prior to use. If you have or suspect you may have a health problem, you should consult your healthcare provider.\n"
                + "\n"
                + "There is no shipping charges\n"
                + "\n"
                + "Note: There is no delivery to P.O. Boxes.\n"
                + "\n"
                + "<b>COPYRIGHT NOTICE</b>\n"
                + "\n"
                + "Copyright © " + new SimpleDateFormat("YYYY").format(new Date()) + "www.DocBuilderSoftware.in. All Rights Reserved.\n"
                + "\n"
                + "NOTE: All product text and most product images are excerpted from materials copyrighted by ElegantSoftware. However, the compilations and original images found on this web site are copyrighted by www.DocBuilderSoftware.in and may not be reproduced in any manner whatsoever without the express permission of www.DocBuilderSoftware.in.\n"
                + "\n"
                + " ";
        return terms;
    }

    /**
     * @param terms the terms to set
     */
    public void setTerms(String terms) {
        this.terms = terms;
    }

    /**
     * @return the privacy
     */
    public String getPrivacy() {
        privacy = "<b>PLEASE READ CAREFULLY THE PRIVACY POLICY BEFORE USING THE www.DocBuilderSoftware.in WEBSITE.</b> \n"
                + "\n"
                + "At www.DocBuilderSoftware.in, we understand how important the privacy of personal information is for you. This Privacy Policy will tell you what information we collect about you and about your use of www.DocBuilderSoftware.in and its services. It will explain the choices you have about how your personal information is used and how we protect that information. We urge you to read this Privacy Policy carefully.\n"
                + "\n"
                + "www.DocBuilderSoftware.in is committed to protecting the privacy of its users. We make every reasonable effort to ensure that information held by www.DocBuilderSoftware.in is confidential and secure. We disclose our information-security practices to you as clearly and fully as possible. These practices are reviewed regularly, and many of our business decisions, including Web design, technology selections, and third-party business relationships, begin and end with considerations for your privacy.\n"
                + "\n"
                + "While our philosophy will not change, the details of this policy will change over time as we add additional services and form relationships with new business partners. Please refer back to this page periodically. In addition, if we make any material changes in our privacy practices that do not affect user information already stored in our database, we will post a prominent notice on our web site notifying users of the change. In some cases where we post the notice we will also email users, who have opted to receive communications from us, notifying them of the changes in our privacy practices\n"
                + "\n"
                + "At www.DocBuilderSoftware.in, we exercise state-of-the-art care in providing secure transmission of your information from your PC to our servers. Personal information collected by our web site is stored in secure operation environments that are not available to the public. Only employees authorized to access your data are able to do so based on a series of permission levels protected by passwords and software algorithms. All employees sign a confidentiality of data agreement at time of employment in which they agree to uphold the confidentiality of any such information. Only those employees who need access to your information in order to do their jobs are allowed access, each having signed confidentiality agreements. Any employee who violates our privacy or security policies is subject to disciplinary action, including possible termination and civil and/or criminal prosecution.\n"
                + "\n"
                + "No one other than yourself and your trusted providers is permitted to make changes or updates to your Personal Record, unless a third party uses your User ID and Password. It is your responsibility to protect your User ID and Password, and thus your Personal Record, from unauthorized use.\n"
                + "\n"
                + "www.DocBuilderSoftware.in does not sell, trade or rent personal information about its users. The email addresses you provide are used to respond to comments submitted through our message boards, and users that contact our site via email will not be subject to any communication from us other than as a response to the question/suggestion, unless specifically requested by the user.\n"
                + "\n"
                + "The www.DocBuilderSoftware.in website contains links to other sites. Once you enter another website (whether through an advertisement, service, or content link), be aware that www.DocBuilderSoftware.in is not responsible for the privacy practices of such other sites. We encourage you to look for and review the privacy statements of each and every website that you visit through a link or advertisement on www.DocBuilderSoftware.in.\n"
                + "\n"
                + "If you are a minor, please refrain from using this site by yourself, and consider having an adult parent or guardian access this site on your behalf.\n"
                + "\n"
                + "We carefully select third-party vendors to assist us in monitoring how our guests use our Web site and to determine which of our online features they prefer. To help us provide these services, the third-party vendors use cookies to collect aggregated, non-personally identifiable information (not including names, addresses, email addresses or telephone numbers) about the use of our Web site by visitors, registered users, customers and guest users. This information is collected solely for our internal use.\n"
                + "\n"
                + "Some of the content on our web site may be provided through the use of framing or linking technology. Using this technology, from our web site you may view content that may reside on a third party's server. In that case, the third party may place a cookie on your hard drive, and any personal information you provide to that third party may be used by that third party in a manner www.DocBuilderSoftware.in cannot control. By accessing any third party content through our Web site, you are assuming all liability from and risk of the use of such content, including the use of cookies. www.DocBuilderSoftware.in has no responsibility or liability whatsoever for any cookie placed by a third party, for any content viewed on any third party's server, or for any use they may make of information you choose to provide that third party. If you are concerned about a third party's Web site, please review the Privacy Policy for that site.\n"
                + "\n"
                + "Unfortunately, no data protection method or combination of methods can be guaranteed 100 percent secure. We take all reasonable steps to protect your personal information as described throughout this policy, but we cannot ensure or warrant our ability to do so. Despite www.DocBuilderSoftware.in's efforts to protect your personal health information, there is always some risk that an unauthorized third party may find a way around our security systems or that transmissions of your information over the Internet may be intercepted. As a result, you use www.DocBuilderSoftware.in content, products, and services at your own risk. www.DocBuilderSoftware.in or its promoters will not be liable for disclosures of your personal information due to errors in transmission or unauthorized acts of third parties.\n"
                + "\n"
                + "This web site may provide chat rooms, message boards, or other on-line forums for its users. www.DocBuilderSoftware.in does not in any manner guarantee the quality or veracity of the information presented in these chat rooms, message boards, or on-line forums. Any information disclosed in these areas becomes public information.\n"
                + "\n"
                + "www.DocBuilderSoftware.in welcomes your questions and comments about privacy and this policy. If you have any questions about this Privacy Policy or our practices, please contact us at info@elegantsoftware.in\n"
                + "";
        return privacy;
    }

    /**
     * @param privacy the privacy to set
     */
    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    /**
     * @return the refund
     */
    public String getRefund() {
        refund = "Refund Policy goes Here";
        return refund;
    }

    /**
     * @param refund the refund to set
     */
    public void setRefund(String refund) {
        this.refund = refund;
    }

    /**
     * @return the disclaimer
     */
    public String getDisclaimer() {
        disclaimer = "<b>Disclaimer.</b>\n"
                + "\nwww.DocBuilderSoftware.in provides the [www address] Web site as a service to the public and Web site owners.\n"
                + "\n"
                + "www.DocBuilderSoftware.in is not responsible for, and expressly disclaims all liability for, damages of any kind arising out of use, reference to, or reliance on any information contained within the site. While the information contained within the site is periodically updated, no guarantee is given that the information provided in this Web site is correct, complete, and up-to-date.\n"
                + "\n"
                + "Although the www.DocBuilderSoftware.in Web site may include links providing direct access to other Internet resources, including Web sites, www.DocBuilderSoftware.in is not responsible for the accuracy or content of information contained in these sites.\n"
                + "\n"
                + "Links from www.DocBuilderSoftware.in to third-party sites do not constitute an endorsement by www.DocBuilderSoftware.in of the parties or their products and services. The appearance on the Web site of advertisements and product or service information does not constitute an endorsement by www.DocBuilderSoftware.in, and www.DocBuilderSoftware.in has not investigated the claims made by any advertiser. Product information is based solely on material received from suppliers.\n"
                + "\n";

        return disclaimer;
    }

    /**
     * @param disclaimer the disclaimer to set
     */
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String sendEmail() {
        if (!validateFields()) {
            return "";
        }
        initAll();
        return "/default?faces-redirect=true";
    }

    private void initAll() {
        Contacts contactsBean = (Contacts) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contacts");
        if (contactsBean == null) {
            return;
        }
        contactsBean.setEmailField("");
        contactsBean.setMessage("");
        contactsBean.setNameField("");
        contactsBean.setPhoneField("");
        contactsBean.setRemarks("");
        setVerifyCode("");
    }

    private boolean validateFields() {
        boolean validated = true;
        Contacts contactsBean = (Contacts) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contacts");
        MediaBean mediaBean = (MediaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mediaBean");
        if (contactsBean.getNameField().equals("") || contactsBean.getPhoneField().equals("") || contactsBean.getEmailField().equals("") || contactsBean.getRemarks().equals("") || getVerifyCode().equals("")) {
            contactsBean.setMessage("Please fill up the form before clicking Submit");
            validated = false;
            return validated;
        }
        if (!isValidEmailField(contactsBean.getEmailField())) {
            contactsBean.setMessage("Invalid Email address entered.");
            validated = false;
            return validated;

        }
//        System.out.println(contactsBean.getVerifyCode().trim() + "  -  " + mediaBean.getDigits().toString().trim() + "  -  " + contactsBean.getVerifyCode().trim() != mediaBean.getDigits().toString().trim());
        if (!getVerifyCode().trim().equals(mediaBean.getDigits().toString().trim())) {
            contactsBean.setMessage("Invalid Captcha. Please retry");
            validated = false;
            return validated;

        }
        return validated;
    }

    public boolean isValidEmailField(String email) {
        boolean validatedEmail = false;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            validatedEmail = true;
        }
        return validatedEmail;
    }

    /**
     * @return the downloadKey
     */
    public String getDownloadKey() {
        return downloadKey;
    }

    /**
     * @param downloadKey the downloadKey to set
     */
    public void setDownloadKey(String downloadKey) {
        this.downloadKey = downloadKey;
    }

    private void createFileStats(String name) {
        File f = null;
        Path path = null;
        try {
            f = new File(name);
            path = Paths.get(name);
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss");
            getFileStats().clear();
            getFileStats().add(f.getName());
            getFileStats().add(Float.toString(f.length() / (1024 * 1024)) + " MB.");
            getFileStats().add(sdf.format(attr.creationTime().toMillis()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the fileStats
     */
    public ArrayList<String> getFileStats() {
        return fileStats;
    }

    /**
     * @param fileStats the fileStats to set
     */
    public void setFileStats(ArrayList<String> fileStats) {
        this.fileStats = fileStats;
    }

    /**
     * @return the verifyCode
     */
    public String getVerifyCode() {
        return verifyCode;
    }

    /**
     * @param verifyCode the verifyCode to set
     */
    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    /**
     * @return the mobileDevice
     */
    public boolean isMobileDevice() {
        return mobileDevice;
    }

    /**
     * @param mobileDevice the mobileDevice to set
     */
    public void setMobileDevice(boolean mobileDevice) {
        this.mobileDevice = mobileDevice;
    }

    /**
     * @return the tripleClickURLs
     */
    public String getTripleClickURL() {
        if (imageCnt >= 9) {
            imageCnt = 0;
        }
        tripleClickURL = tripleClickURLs[imageCnt];
        imageCnt++;

        return tripleClickURL;
    }

    /**
     * @param tripleClickURLs the tripleClickURLs to set
     */
    public void setTripleClickURL(String tripleClickURL) {
        this.tripleClickURL = tripleClickURL;
    }

    /**
     * @return the pollEnabled
     */
    public boolean isPollEnabled() {
        return pollEnabled;
    }

    /**
     * @param pollEnabled the pollEnabled to set
     */
    public void setPollEnabled(boolean pollEnabled) {
        this.pollEnabled = pollEnabled;
    }

    public void displayLogoutMessage() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    /**
     * @return the calledPage
     */
    public String getCalledPage() {
        return calledPage;
    }

    /**
     * @param calledPage the calledPage to set
     */
    public void setCalledPage(String calledPage) {
        this.calledPage = calledPage;
    }

    /**
     * @return the previousPage
     */
    public String getPreviousPage() {
        return previousPage;
    }

    /**
     * @param previousPage the previousPage to set
     */
    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    /**
     * @return the downloadFileURLImage
     */
    public String getDownloadFileURLImage() {
        return downloadFileURLImage;
    }

    /**
     * @param downloadFileURLImage the downloadFileURLImage to set
     */
    public void setDownloadFileURLImage(String downloadFileURLImage) {
        this.downloadFileURLImage = downloadFileURLImage;
    }
}
