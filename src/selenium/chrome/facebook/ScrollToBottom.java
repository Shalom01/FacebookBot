package selenium.chrome.facebook;

import org.openqa.selenium.JavascriptExecutor;

public class ScrollToBottom extends FBBot {
	
	public static void scroll(){
	try {
	    long lastHeight = (long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight");
	    while (true) {
	        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
	        Thread.sleep(2000);
	        long newHeight = (long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight");
		        if (newHeight == lastHeight) {
		            break;
		        }
	        lastHeight = newHeight;
	    	}
		    Thread.sleep(2000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}
}
