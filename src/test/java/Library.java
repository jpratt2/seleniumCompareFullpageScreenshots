import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.function.Consumer;

public class Library {
	public static void UrlLoop(String[] urlList, Consumer<FirefoxDriver> method) {
		WebDriverManager.firefoxdriver().setup();
		for (int i = 0; i < urlList.length; i++) {
			FirefoxDriver driver = new FirefoxDriver();//start a new, fresh browser for each web page
			driver.manage().window().setSize(new Dimension(1440, 900));
			String URL = urlList[i];
			driver.get(URL);
			try {
				Thread.sleep(10000);//wait for the page to fully load
			} catch (InterruptedException e) {}
			method.accept(driver);
			driver.quit();
		}
	}
		public static void UrlLoopTwiceLoaded(String[] urlList, Consumer<FirefoxDriver> method){
			WebDriverManager.firefoxdriver().setup();
			for (int i = 0; i < urlList.length; i++) {

				FirefoxDriver driver = new FirefoxDriver();//start a new, fresh browser for each web page
				driver.manage().window().setSize(new Dimension(1440, 900));
				String URL = urlList[i];

				//load the page twice
				for(int x=0;x<2;x++) {
					driver.get(URL);
					try {
						Thread.sleep(10000);//wait for the page to fully load
					} catch (InterruptedException e) {}
				}
				method.accept(driver);
				driver.quit();
			}


	}
	
	
	
}