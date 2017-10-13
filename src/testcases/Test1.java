package testcases;

import org.testng.annotations.Test;

import utility.Log;

import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;

public class Test1 {
	
	WebDriver driver;
	
  @Test
  public void f() {
	  driver.get("http://www.baidu.com");
	  Log.info("baidu is open");
  }
  
  @BeforeMethod
  public void beforeMethod() {
	  driver = new FirefoxDriver();
	  Log.info("firefox launched");
  }

  @AfterMethod
  public void afterMethod() {
	  driver.quit();
  }

}
