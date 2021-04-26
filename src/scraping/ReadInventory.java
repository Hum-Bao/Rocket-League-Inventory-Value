package scraping;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.util.List;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import javax.swing.SwingWorker;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReadInventory {
	protected static ArrayList<String> itemsParsed = new ArrayList<String>();
	protected static String installPath = "";
	//public static Path path = Paths.get(installPath);
	protected static void inventoryThread() {
		SwingWorker<Void, String> inventoryWorker = new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				String fileExtension = installPath.substring(installPath.indexOf("."), installPath.length());
				//System.out.println(fileExtension);
				if (fileExtension.toLowerCase().equals(".json")) getJSON();
				else getCSV();
				//getJSON();
				return null;
			}
			@Override
			protected void done() {
				GUI.text.append("Done!\n");
				GUI.text.append("\nValue: " + Scraper.sum1 + "-" + Scraper.sum2 + " credits\n");
				//GUI.text.append("\nYour blueprints's value is between " + Scraper.blueprintSum1 + " and " + Scraper.blueprintSum2 + " credits\n");
				GUI.output.setText(Scraper.sum1 + "-" + Scraper.sum2);
			}
		};
		inventoryWorker.execute();
	}
	
	protected static void getCSV() {
		//List<List<String>> inventoryList = new ArrayList<>();
		//List<String> inventoryList2 = new ArrayList<String>();
		try {
			BufferedReader brCSV = new BufferedReader(new FileReader(installPath));
			String line;
			brCSV.readLine();
			String slot = null;
			while ((line = brCSV.readLine()) != null) {
				String[] values = line.split(",");
				//inventoryList.add(Arrays.asList(values));
				List<String> inventoryList = new ArrayList<String>(Arrays.asList(values));
				String tradeable = inventoryList.get(9);
				if (tradeable.equals("true"))  {
					slot = inventoryList.get(2);
					if (!slot.equals("Blueprint")) {
						String quality = inventoryList.get(7);
						if (!quality.equals("UNKNOWN?")) {
							String product_id = inventoryList.get(0);
							String painted = inventoryList.get(3);
							if (!painted.equals("none")) {
								String colourExtension;
								if (painted.equals("Sky Blue")) colourExtension = "/sblue";
								else {
									int spaceIndex = painted.indexOf(" ");
									colourExtension = "/" + painted.substring(spaceIndex+1).toLowerCase();
								}
								product_id = product_id+colourExtension;
							}
							itemsParsed.add(product_id);
							GUI.text.append("Found product id: " + product_id+ "\n");
						}
					}
				}
				//System.out.println(inventoryList.get(0));
				//System.out.println(inventoryList2.get(0));
				//System.out.println(inventoryList2.get(9));
			}
			brCSV.close();
			//Scraper.recieveItems(itemsParsed, slot);
			Scraper.recieveItems(itemsParsed);
		} catch (Exception errorCSV) {
			
		}
	}
	
	protected static void getJSON() {
		JSONParser parser = new JSONParser();
		String slot = null;
		try {
			Object obj = parser.parse(new FileReader(installPath));
			JSONObject inventory = (JSONObject) obj;
			JSONArray inventoryList = (JSONArray) inventory.get("inventory");
			for (Object obj2 : inventoryList) {
				JSONObject jsonObject = (JSONObject) obj2;
				//String name = (String) jsonObject.get("name");
				Boolean tradeable = (Boolean) jsonObject.get("tradeable");
				if (tradeable == true) {
					slot = (String) jsonObject.get("slot");
					if (!slot.equals("Blueprint")) {
						String quality = (String) jsonObject.get("quality");
						if(!quality.equals("UNKNOWN?")) {
							long product_id = (long) jsonObject.get("product_id");
							itemsParsed.add(Long.toString(product_id));
							GUI.text.append("Found product id: " + product_id+ "\n");
						}
					}
				}
				/*
				String slot = (String) jsonObject.get("slot");
				String quality = (String) jsonObject.get("quality");
				if (tradeable == true && !slot.equals("Blueprint") && !quality.equals("UNKNOWN?")) {
					itemsParsed.add(Long.toString(product_id));
					GUI.text.append("Found product id: " + product_id+ "\n");
				}
				*/
			}
			//Scraper.recieveItems(itemsParsed, slot);
			Scraper.recieveItems(itemsParsed);
		}catch (Exception e) {
			e.printStackTrace();
			GUI.text.append(e.getMessage());
		}
	}
}