package scraping;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomText;

import java.util.ArrayList;
public class Scraper {
	
	//protected static void recieveItems (ArrayList<String> itemsParsed, String slot) {
	private static final WebClient webClient = new WebClient();
	private static int sum1 = 0;
	private static int sum2 = 0;
	//private static int count = 0;
	//protected static int blueprintSum1 = 0;
	//protected static int blueprintSum2 = 0;
	private static boolean printValue = false;
	
	public static int getSum1() {return sum1;}
	public static int getSum2() {return sum2;}
	public static void resetSums() {
		sum1 = 0;
		sum2 = 0;
	}
	public static boolean getPrintValue() {return printValue;}
	public static void updatePrintValue() {printValue = !printValue;}
	
	public static void updatePrintValue(boolean value) {printValue = value;}
	
	public static void recieveItems (ArrayList<String> itemsParsed) {
		setWebClientOptions();
		for (int count = 0; count < itemsParsed.size(); count++) {
			String searchQuery = itemsParsed.get(count);
			GUI.appendText("Currently checking ID: " + searchQuery+"\n");
			//search(searchQuery, arraySize, slot);
			search(searchQuery);
		}
		webClient.close();
	}
	
	public static void setWebClientOptions() {
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setDownloadImages(false);
	}
	
	//protected static void search(String searchQuery, int arraySize, String slot) {
	public static void search(String searchQuery) {
		try {
			final String searchUrl = "https://rl.insider.gg/en/pc/" + searchQuery;
			final HtmlPage page = webClient.getPage(searchUrl);
			//DomElement element = null;
			/*
			boolean blueprint = false;
			if (slot.equals("Blueprint")) {
				element = page.getFirstByXPath("/html/body/div[8]/div[4]/div[2]/table/tbody/tr[6]/td[2]");
				blueprint = true;
			}
			*/
			//else {
				final DomElement itemValue = page.getFirstByXPath("/html/body/div[8]/div[4]/div[2]/table/tbody/tr[2]/td[2]");
				final DomText itemName = page.getFirstByXPath("/html/body/div[8]/div[5]/a[1]/div/h2/text()[1]");
				if(itemName != null) GUI.appendText("Name: " + itemName + "\n");	
				else GUI.appendText("Item not found\n\n");
			//}
			String text = itemValue.getTextContent().replaceAll("\\s","");
			//text = text.replaceAll("\\s", "");
			if (text.equals(" ")||text.equals("â€ƒ") || text.equals("?")) {
				GUI.appendText("Credit amount = " + "No credit information"+"\n\n");
			}
			/*
			else if (blueprint == true) {
				GUI.text.append("Blueprint credit amount = " + text+"\n\n");
				String splitInt[] = text.split("-");
				for (String temp: splitInt) {
				if (count%2 == 0) blueprintSum2+=Integer.parseInt(temp);
					else blueprintSum1+=Integer.parseInt(temp);
				}
			}
			*/
			else {
				GUI.appendText("Credit amount = " + text+"\n\n");
				String splitInt[] = text.split("-");
				sum2+=Integer.parseInt(splitInt[1]);
				sum1+=Integer.parseInt(splitInt[0]);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			GUI.appendText(e.getMessage());
		}
		//count++;
		//if (count == arraySize) {
			//printValue = true;
		//}
	}
}
