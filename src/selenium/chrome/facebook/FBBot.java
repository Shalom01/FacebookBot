package selenium.chrome.facebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

public class FBBot {
	public static WebDriver driver = null;
	public static void main(String[] args) throws FileNotFoundException {
		
		Set<String> rushList = new HashSet<String>(); //rushList to hold potential rushees
		Set<String> kosherNames = new HashSet<String>(); //list of Jewish boys names
		Set<String> failedMessages = new HashSet<String>(); //names we failed to contact
		
		/*build the kosherNames Set*/
		Scanner scanner = new Scanner(new File("NameList.txt"));
		while(scanner.hasNext()){
			kosherNames.add(scanner.next());
		}
		
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_setting_values.notifications", 2);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", prefs);
		
		Scanner in = new Scanner(System.in); //scanner for user input
		
		System.out.println("Insert directory path of your chrome driver:"); //get the chrome driver location from user
		System.setProperty("webdriver.chrome.driver", in.nextLine());
		
		driver = new ChromeDriver(options);
		driver.navigate().to("https://facebook.com");
		
		WebElement nameBox = driver.findElement(By.xpath("//*[@id=\"email\"]"));
		WebElement passwordBox = driver.findElement(By.xpath("//*[@id=\"pass\"]"));
		WebElement login = driver.findElement(By.xpath("//*[@name=\"login\"]"));
		
		
		System.out.println("Insert your Facebook name:"); //get the users facebook name
		nameBox.sendKeys(in.nextLine());
		System.out.println("Enter your password:"); //get the users facebook password
		passwordBox.sendKeys(in.nextLine());
		login.click(); //login
		
		
		System.out.println("Enter the link of the facebook group you would like to check:"); //get hyperlink of facebook group
		driver.get(in.nextLine());
		
		WebElement post = driver.findElement(By.xpath("//*[@id=\"count_text\"]"));
		post.click();
		
		ScrollToBottom.scroll();
		
		//put all names with partialLinkText of any kosherNames elements into List, then add all those to rushList
		try {
				for(String name: kosherNames) {
					List<WebElement> rush = driver.findElements(By.partialLinkText(name));
					for(String rushName: rushList) {
						rushList.add(name);
					}
				}
		}catch(NoSuchElementException e){
			System.out.println("No potential rushes among names found");
		}
		
		if(rushList.size() == 0) {
			System.out.println("No rushes located");	
		}else { //if we found potential rushes
			for(String name: rushList) { //send them all a personalized message
				try {
						driver.findElement(By.partialLinkText(name)).click();
						Thread.sleep(2500);
						driver.findElement(By.cssSelector("a[class='_42ft _4jy0 _45lc _4jy3 _517h _51sy']")).click();
						Thread.sleep(2000);
						WebElement TextBox = driver.switchTo().activeElement();
						Thread.sleep(2000);
						TextBox.sendKeys("/*Hello friend! Come and join AEPi fraternity*/"); //enter your personalized message
						TextBox.sendKeys(Keys.ENTER);
						Thread.sleep(2000);
						driver.navigate().back();
						Thread.sleep(2000);
						ScrollToBottom.scroll();
						
				}catch(Exception e) {
					System.out.println("Could not text " + name);
					failedMessages.add(name);
				}
			}
			System.out.println("Potential Rushes found: " + rushList + " found\nRushes who could not be messaged:" + failedMessages);
		}
	}
}
