package WebTest;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SwaggerUITest {

	public static WebDriver driver;
	WebElement pets;
	String screenshot_files=System.getProperty("user.dir") + "/screenshots/";
	
	public void getScreenshot() throws IOException {
		Date currentDate=new Date();
		String fileName=currentDate.toString().replace(" ", "-").replace(":","-");
		File source=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(source, new File(screenshot_files + fileName + ".png"));
	}
	
	@BeforeClass
	public void browserSetup() {	
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get("https://petstore.swagger.io/#/");
	}
	

	@Test
	public void testUIBehaviorForEachRestMethodSection() {
		try
		{			
			List<WebElement> Tag = driver.findElements(By.className("opblock-tag"));			
			List<WebElement> operations = driver.findElements(By.cssSelector("button[class='opblock-summary-control']"));
			
			ListIterator<WebElement> tagIterator = Tag.listIterator();
			ListIterator<WebElement> opIterator = operations.listIterator();
	
			// iterator for the sections to make sure that all are expanded before testing
			// the methods in each section.
	
			while (tagIterator.hasNext()) {
	
				WebElement elem = tagIterator.next();
	
				if (!elem.getAttribute("data-is-open").contains("true")) {
					elem.click();
				}
	
				if (elem.getAttribute("data-is-open").contains("true")) {
					Assert.assertTrue(true, "Tab Expanded");
				} else {
					Assert.assertTrue(false, "Tab Not Expanded");
				}
			}
//iterator for the methods
			while (opIterator.hasNext()) {
	
				WebElement methods = opIterator.next();
	
				if (!methods.getAttribute("aria-expanded").contains("true")) {
					methods.click();
					assertEquals(false, driver.findElement(By.cssSelector("button[class='btn try-out__btn']")).getText().contains("Try it out"));
				}
				else
				{
					methods.click();
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	
	}	
	
	@AfterMethod
	public void tearDown(ITestResult result)
	{	 
		System.out.println(result.getStatus());
		if(ITestResult.FAILURE==result.getStatus())
		{	
			TakesScreenshot ts=(TakesScreenshot)driver;
			File source=ts.getScreenshotAs(OutputType.FILE);
			try
			{
				FileUtils.copyFile(source, new File("screenshot_files"+result.getName()+".png"));
		
			}	
			catch (Exception e)
			{
				System.out.println("Exception while taking screenshot "+e.getMessage());
			} 
		}
		driver.quit();
	}
}
