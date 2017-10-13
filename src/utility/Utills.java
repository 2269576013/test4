package utility;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Utills {

	public static WebDriver driver;
	/**
	 * 根据句柄切换窗口
	 * @param origHandle
	 */
	public static void switchWindow(String origHandle){
		Set<String> allHandles= driver.getWindowHandles();
		Iterator<String> it = allHandles.iterator();			
		while(it.hasNext()){
			String handle = it.next();
			String s = it.next();
			if(!s.equals(origHandle)){
				driver.switchTo().window(s);
			}
		}
	}
	
	public static String getTestData(String key){
		ExcelData.setPath(Constants.PATH+Constants.FILENAME, Constants.SHEETNAME);
		int rowNum = ExcelData.getRowContains(key, Constants.KEYCOLUMN);
		String cellValue = ExcelData.getCellData(rowNum, Constants.COLUMN);
		return cellValue;
	}
	
	
	/**
	 * csv文件保存的测试数据	 * 
	 * @param key
	 * @return
	 */
	public static String getCSVTestData(String key){
		String vlaue = null;
		//for each循环，读取csv文件每一行的数据
		for(String[] row:ExcelData.getTestDataFromObjectsFile()){
			//如果第一分行的key值匹配，则第二分列是要取得值
			if(row[0].equalsIgnoreCase(key)){
				vlaue = row[1];
				break;
			}
		}
		return vlaue;
	}
	

	/**
	 * 把locator封装到测试文件里
	 * @param key
	 * @return
	 */
	public static By getLocator(String key){
		By locat = null;		
		String locatorType = null;
		String locator = null;
		for(String[] row:ExcelData.getLocatorsFromObjectsFile()){
			//如果第一分行的key值匹配，则第二分列是要取得值
			if(row[0].equalsIgnoreCase(key)){
				locatorType = row[1];
				locator = row[2];
				break;
			}
		}		
		switch(locatorType){
		case "id":
			locat = By.id(locator);
			break;
		case "name":
			locat = By.name(locator);
			break;
		case "xpath":
			locat = By.xpath(locator);
			break;
		default:
			Log.error("Utills||getLocator: cannot find locator");
			}		
		return locat;
	}

	/**
	 * 封装element
	 * @param key
	 * @return
	 */
	public static WebElement getElement(String key){
		WebElement element = null;
		element = driver.findElement(getLocator(key));
		if(!element.isDisplayed()){
			Log.error("Utills||getElement: cannot find element");
		}
		return element;
		
	}
	
	
	/**
	 * 把sendkeys一起封装
	 
	 * @param data
	 * @param key
	 */
	
	public static void inputValue (String datakey, String locatorkey){
		String keysToSend = getTestData(datakey);		
			getElement(locatorkey).clear();
			getElement(locatorkey).sendKeys(keysToSend);
			Log.error("test data: "+ keysToSend +"is input.");
		}
	
	/**
	 * 把sendkeys一起封装，并且增加一个 flag 变量去判断输入的数据来自测试文件还是自己输入
	 * 比上一个方法适应性更广
	 * @param data
	 * @param key
	 */
	public static void inputValue(boolean flag, String value, String locatorkey){
		String keysToSend = null;
		//flag = true, 输入value，如果 = false，根据value值从测试文件中找数据输入
		if(flag){
			keysToSend = value;
		}else{
			keysToSend = getTestData(value);				
		}
		getElement(locatorkey).clear();
		getElement(locatorkey).sendKeys(keysToSend);
		Log.error("test data: "+ keysToSend +"is input.");
	}
	
	
	/**
	 * 封装打开浏览器方法
	 */
	public static WebDriver openBrowser(String url,String browser) throws Exception {
		try {
			switch (browser) {
			case "firefox":
				//FirefoxProfile profile = new FirefoxProfile();
				//下面参数作用：不要禁用混合内容
	//			profile.setPreference("security.mixed_content.block_active_content", false);
	//			profile.setPreference("security.mixed_content.block_display_content", true);
	//			driver = new FirefoxDriver(profile);
				FirefoxProfile profile = new FirefoxProfile();
				//下面参数作用：不要禁用混合内容
				profile.setPreference("security.mixed_content.block_active_content", false);
				profile.setPreference("security.mixed_content.block_display_content", true);
				profile.setPreference("browser.download.folderList", 2);
				profile.setPreference("browser.helperApps.neverAsk.saveToDisk", 
						"application/msword,application/csv,text/csv,externalApplication:text/html");
				profile.setPreference("browser.download.useDownloadDir", "false");
				//FirefoxProfile profile = ini.getProfile("GIAS_Profile");
				driver = new FirefoxDriver(profile);
				break;
			case "ie":
				//设置
				DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
				ieCapabilities.setCapability("nativeEvents", true);    
				ieCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
				ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
				ieCapabilities.setCapability("disable-popup-blocking", true);
				ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);			
				ieCapabilities.setCapability("requireWindowFocus", false);
				ieCapabilities.setCapability("enablePersistentHover", false);
				System.setProperty("webdriver.ie.driver", Constants.PATH +Constants.IEDRIVER);
				driver = new InternetExplorerDriver(ieCapabilities);
				break;
			default:
				Log.warn("Can not find the browser type. and the browser type is: "+browser);
				break;
			}	
			Log.info("Browser type is "+browser);
			driver.get(url);
			Log.info(url+" is open");
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			//driver.manage().deleteAllCookies();	
			//driver.navigate().to(driver.getCurrentUrl());
			return driver;
		} catch (Exception e) {
			Log.error("Package:utility|| class:Utils|| Method:openBrowser || Unable to Open Browser.");	
			throw (e);
		}
	}
	
	
	/**
	 * 封装click方法
	 */
	public static void waitforJSLoad()throws Exception {
		
		(new WebDriverWait(driver, 80)).until(new ExpectedCondition<Boolean>() {
			  @Override 
	    	  public Boolean apply(WebDriver dr) {
				  try {				
					  	Boolean value = ((JavascriptExecutor) dr).executeScript("return document.readyState").equals("complete");
					  	 return value;
					 } catch (Exception e) {
						 return Boolean.FALSE;
						}
	    	   }
	    }); 
	}
	public static void elementClick(String locator) throws Exception{
		WebElement element ;
		element = getElement(locator);
		element.click();
		Log.info(element+" is clicked.");
		waitforJSLoad();	
	}
	
	/**
	 * switch frame 封装
	 * @param id
	 */
	public static void switchFrame(String id){
		driver.switchTo().frame(id);
		
	}

	
	/**
	 * select方法封装
	 * @param value
	 * @param locator
	 * @param flag
	 */
	public static void selectValue(String value,String locator,String flag){
		WebElement element = getElement(locator);
		Select select = new Select(element);
		String dataValue =getTestData(value);
		if(flag.equalsIgnoreCase("byvalue")){
			
			select.selectByValue(dataValue);
		}else{
			select.selectByVisibleText(dataValue);
		}
		Log.info(dataValue +" is selected.");
	}

	
	/**
	 * assert 封装
	 * @param value ： 从testdata文件中根据value读取预期值
	 * @param locator：要获取text的页面元素
	 */
	public static void assertText(String value, String locator){
		String actualValue = "";
		String expectedValue = "";
		actualValue = getElement(locator).getText();
		expectedValue = getTestData(value);
		Assert.assertEquals(actualValue,expectedValue,"Assert fail. actualValue is"+actualValue+", expectedValue is"+expectedValue);
		
	}
	
	
	/**
	 * assertText 封装 + 截图
	 * @param value
	 * @param locator
	 * @param sTestCaseName
	 */
	public static void assertText(String value,String locator,String sTestCaseName){
		String actualValue = "";
		String expectedValue = "";
		try{
			
		actualValue=getElement(locator).getText();
		expectedValue = getTestData(value);
		
		Assert.assertEquals(actualValue,expectedValue,"Assert is Fail with Actual data is ["+actualValue+"] And Expected Result is ["+expectedValue+"].");
		}catch(AssertionError e){
			Log.error(e.getMessage());
			takeScreenshot(sTestCaseName);
			Assert.assertFalse(true);
		}
	}
	
	
	/**
	 * assertRadioButton封装
	 * @param locator
	 * @param sTestCaseName
	 */
	public static void assertRadioButton(String locator,String sTestCaseName){
		boolean actualValue = false;
		WebElement element = getElement(locator);
		try{
		actualValue =getElement(locator).isSelected();
		
		Assert.assertTrue(actualValue, "Assert is Fail with "+ element+" is not checked.");
		
		}catch(AssertionError e){
			Log.error(e.getMessage());
			takeScreenshot(sTestCaseName);
			Assert.assertFalse(true);
		}
	}
	
	
	/**
	 * 截图
	 * @param sTestCaseName
	 */
	public static void takeScreenshot(String sTestCaseName) {

		DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh_mm_ss");
		Date date = new Date();
		
		File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(file, new File(Constants.SNAPSHOTPATH
							+ sTestCaseName +"/"+sTestCaseName+ " # " + dateformat.format(date)
							+ ".png"));


		} catch (Exception e) {
			Log.error("Package Utility|| class Utills|| Method takeScreenshot || issue in Taking Screenshot");
		}

	}
	
	
	/**
	 * checkPoint 截图
	 * @param testCaseName
	 * @param flag
	 * @param checkPointNum
	 * @throws Exception
	 */
	public static void takeScreenshotCheckPoint(String testCaseName, boolean flag, String checkPointNum) throws Exception {
		Log.info("[takeScreenshotCheckPoint]: " + checkPointNum);
		File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String picName = "";

		try {
			if (flag) {
				picName = Constants.SNAPSHOTPATH + testCaseName + "/" + testCaseName + "_" + checkPointNum + "_pass.png";
				FileUtils.copyFile(file, new File(picName));
			} else {
				picName = Constants.SNAPSHOTPATH + testCaseName + "/" + testCaseName + "_" + checkPointNum + "_fail.png";
				FileUtils.copyFile(file, new File(picName));
			}

		} catch (Exception e) {
			Log.error("[takeScreenshotCheckPoint]: Package Utility|| class Utills|| Method takeScreenshot || issue in Taking Screenshot");
			throw e;
		}

	}
}