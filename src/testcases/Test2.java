package testcases;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.ExcelData;
import utility.Log;
import utility.Utills;

public class Test2 {

	private static final String String = null;
	WebDriver driver;

	@AfterTest
	public void AfterTest() throws InterruptedException {
		driver.quit();
	}

	@Test
	public void test() throws InterruptedException {
		//把打开浏览器封装一下
		
		Utills.openBrowser("http://172.166.100.103:85","firefox");
		
		//driver.get("http://172.166.100.103:85");
		//Log.info("780A系统已打开");
//		//用那3个方法读取测试数据
//		driver.findElement(By.xpath(".//*[@id='account']/div/input")).sendKeys(ExcelData.getMapData("valid_username"));
//		Log.info("username is input");
//		//把locator封装到测试文件中，
//		driver.findElement(Utills.getLocator("username_editbox_locator")).sendKeys(ExcelData.getMapData("valid_username"));
//		Log.info("username is input");
//		//把整个element封装
		//		
		Utills.getElement("username_editbox_locator").sendKeys("admin");
//		Log.info("username is input");
		//把sendkeys一起封装
		//Utills.driver = driver;	
		//Utills.inputValue("valid_username", "username_editbox_locator");
		//Utills.inputValue(false, "valid_username", "username_editbox_locator");
		//Log.info("780A系统已打开");
		
		//用map键值对读取数据
		driver = Utills.driver ;
		driver.findElement(By.xpath(".//*[@id='password']/div/input")).sendKeys(Utills.getTestData("valid_password"));
		Log.info("password is input");
		driver.findElement(By.id("submit")).click();
		Thread.sleep(2000);
		
		
		
		// 点击行政办公
		driver.findElement(By.xpath(".//*[@id='r1']/ul/li[5]")).click();
		Log.warn("click XingZhengBanGong");
		Thread.sleep(3000);
		driver.findElement(By.xpath(".//*[@id='m09']/ul/li[1]/div[1]")).click();
		Log.info("click BanGongYongPinGuanLi");
		driver.findElement(By.xpath(".//*[@id='m09']/ul/li[1]/div[2]/ul/li[4]/span")).click();
		Log.error("In to office product management");
		driver.switchTo().frame("context");
		Log.debug("In to iFrame");
		driver.findElement(By.xpath("html/body/div[1]/div[1]/div/div[2]/span[1]")).click();
		Log.info("click XinZeng");
		driver.findElement(By.name("pro_name")).sendKeys("test");
		Log.info("input Name");
		driver.findElement(By.name("pro_unit")).sendKeys("unit");
		Log.info("input unit");
		String origHandle = driver.getWindowHandle();
		Log.info("get current window handle");
		Thread.sleep(2000);
		driver.findElement(By.xpath(".//*[@id='_4_']/div[1]/span[1]")).click();
		Log.info("click TianJia");
		Thread.sleep(2000);
		
		// 用utility里的公用方法
		Utills.driver = driver;
		Utills.switchWindow(origHandle);
		driver.switchTo().frame("right");
		driver.findElement(By.xpath(".//*[@id='admin']/td/span")).click();
		Log.info("select GuanLiYuan");
		driver.close();
		Log.info("close window");
		Thread.sleep(2000);
		driver.switchTo().window(origHandle);
		Log.info("switch window");
		driver.switchTo().frame("context");
		Log.info("switch frame");
		driver.findElement(By.xpath(".//*[@id='_5_']/div[1]/span[1]")).click();
		// 用utility里的公用方法
		Utills.driver = driver;
		Utills.switchWindow(origHandle);

		driver.switchTo().frame("head");
		driver.findElement(By.id("DALL_DEPT")).click();
		Log.info("select QuanTiBuMen");
		Thread.sleep(2000);
		driver.switchTo().window(origHandle);
		Log.info("back to original weindow");
		driver.switchTo().frame("context");
		Log.info("switch frame");
		driver.findElement(By.xpath("html/body/div[1]/div[1]/div/div[2]/span[1]")).click();
		Thread.sleep(2000);
		// 退出
		driver.switchTo().defaultContent();
		driver.findElement(By.xpath(".//*[@id='r3']/span[4]")).click();
		Thread.sleep(2000);

		
	}

	public void switchWindow(String origHandle) {
		Set<String> allHandles = driver.getWindowHandles();
		Iterator<String> it = allHandles.iterator();
		while (it.hasNext()) {
			String handle = it.next();
			String s = it.next();
			if (!s.equals(origHandle)) {
				driver.switchTo().window(s);
			}
		}
	}

}
