package WebTest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener extends SwaggerUITest implements ITestListener {
	public void onTestFailure(ITestResult result)
	{
		System.out.println("Test Failed.. Screenshot Captured");
		try {
			getScreenshot();			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
