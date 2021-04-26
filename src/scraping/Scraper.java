package scraping;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.DomElement;
import java.util.ArrayList;
public class Scraper {
	
	//protected static void recieveItems (ArrayList<String> itemsParsed, String slot) {
	protected static final WebClient webClient = new WebClient();
	public static int sum1 = 0;
	public static int sum2 = 0;
	public static int count = 0;
	//protected static int blueprintSum1 = 0;
	//protected static int blueprintSum2 = 0;
	protected static boolean printValue = false;
	
	protected static void recieveItems (ArrayList<String> itemsParsed) {
		int arraySize = itemsParsed.size();
		for (int count = 0; count < itemsParsed.size(); count++) {
			String searchQuery = itemsParsed.get(count);
			GUI.text.append("Currently checking ID: " + searchQuery+"\n");
			//search(searchQuery, arraySize, slot);
			search(searchQuery, arraySize);
		}
		webClient.close();
	}
	//protected static void search(String searchQuery, int arraySize, String slot) {
	protected static void search(String searchQuery, int arraySize) {
		try {
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setDownloadImages(false);
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
				DomElement element = page.getFirstByXPath("/html/body/div[8]/div[4]/div[2]/table/tbody/tr[2]/td[2]");
			//}
			String text = element.getTextContent();
			text = text.replaceAll("\\s", "");
			if (text.equals(" ")||text.equals("â€ƒ") || text.equals("?")) {
				GUI.text.append("Credit amount = " + "No credit information"+"\n\n");
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
				GUI.text.append("Credit amount = " + text+"\n\n");
				String splitInt[] = text.split("-");
				sum2+=Integer.parseInt(splitInt[1]);
				sum1+=Integer.parseInt(splitInt[0]);
			}
			//webClient.close();
		}
		catch(Exception e){
			e.printStackTrace();
			GUI.text.append(e.getMessage());
		}
		count++;
		if (count == arraySize) {
			printValue = true;
		}
	}
}
