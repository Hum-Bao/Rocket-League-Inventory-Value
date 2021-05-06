package scraping;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.swing.SwingWorker;
public class ReadInventory {
	private static ArrayList<String> itemsParsed = new ArrayList<String>();
	private static String installPath = "";
	//public static Path path = Paths.get(installPath);
	
	public static void resetArrayList() {itemsParsed = new ArrayList<String>();}
	public static void updateInstallPath(String text) {installPath = text;}
	
	public static void inventoryThread() {
		SwingWorker<Void, String> inventoryWorker = new SwingWorker<Void, String>() {
			@Override
			public Void doInBackground() {
				String fileExtension = installPath.substring(installPath.indexOf("."), installPath.length());
				//System.out.println(fileExtension);
				if (fileExtension.toLowerCase().equals(".json")) getJSON();
				else getCSV();
				//getJSON();
				return null;
			}
			@Override
			public void done() {
				GUI.appendText("Done!\n");
				GUI.appendText("\nValue: " + Scraper.getSum1() + "-" + Scraper.getSum2() + " credits\n");
				//GUI.text.append("\nYour blueprints's value is between " + Scraper.blueprintSum1 + " and " + Scraper.blueprintSum2 + " credits\n");
				Scraper.updatePrintValue();
				GUI.updateOutput(Scraper.getSum1() + "-" + Scraper.getSum2());
			}
		};
		inventoryWorker.execute();
	}
	
	public static void emptyArray(String[] inventoryList) {
		inventoryList[1] = null;
		inventoryList[4] = null;
		inventoryList[5] = null;
		inventoryList[6] = null;
		inventoryList[8] = null;
		inventoryList[11] = null;
	}
	
	public static void getCSV() {
		//List<List<String>> inventoryList = new ArrayList<>();
		//List<String> inventoryList2 = new ArrayList<String>();
		try {
			BufferedReader brCSV = new BufferedReader(new FileReader(installPath));
			String line;
			brCSV.readLine();
			while ((line = brCSV.readLine()) != null) {
				String[] inventoryList = line.split(",");
				emptyArray(inventoryList);
				String slot = inventoryList[2];
				if (!slot.equals("Blueprint")) {
					String tradeable = inventoryList[9];
					if (tradeable.equals("true"))  {
						String quality = inventoryList[7];
						if (!quality.equals("UNKNOWN?")) {
							String productID = inventoryList[0];
							String painted = inventoryList[3];
							if (!painted.equals("none")) {
								String colourExtension;
								if (painted.equals("Sky Blue")) colourExtension = "/sblue";
								else {
									byte spaceIndex = (byte)painted.indexOf(" ");
									colourExtension = "/" + painted.substring(spaceIndex+1).toLowerCase();
								}
								productID = productID+colourExtension;
							}
							int amount = Integer.parseInt(inventoryList[10]);
							for(int count = 0; count<amount; count++) itemsParsed.add(productID);
							GUI.appendText("Found product id: " + productID+ " x " + amount + "\n");
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
			errorCSV.printStackTrace();
			GUI.appendText(errorCSV.getMessage());
			//Scraper.updatePrintValue();
		}
	}
	@JsonIgnoreProperties({"name","certification","certification_value","rank_label","quality","crate","instance_id"})
	public static void getJSON() {
		JSONParser parser = new JSONParser();
		try {
			Object parserObject = parser.parse(new FileReader(installPath));
			JSONObject inventory = (JSONObject) parserObject;
			JSONArray inventoryList = (JSONArray) inventory.get("inventory");
			for (Object obj2 : inventoryList) {
				JSONObject jsonObject = (JSONObject) obj2;
			
				//Boolean tradeable = (Boolean) jsonObject.get("tradeable");
				//if (tradeable == true) {
					String slot = (String) jsonObject.get("slot");
					if (!slot.equals("Blueprint")) {
						Boolean tradeable = (Boolean) jsonObject.get("tradeable");
						if (tradeable == true) {
						String quality = (String) jsonObject.get("quality");
						if(!quality.equals("UNKNOWN?")) {
							String productID = Long.toString((long)jsonObject.get("product_id"));
							String painted = (String)jsonObject.get("paint");
							if (!painted.equals("none")) {
								String colourExtension;
								if (painted.equals("Sky Blue")) colourExtension = "/sblue";
								else colourExtension = "/" + painted.substring(((byte)painted.indexOf(" "))+1).toLowerCase();
								productID = productID+colourExtension;
							}
							int amount = ((Long)jsonObject.get("amount")).intValue();
							for(int count = 0; count<amount; count++) itemsParsed.add(productID);
							GUI.appendText("Found product id: " + productID+ " x " + amount + "\n");
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
			GUI.appendText(e.getMessage());
			//Scraper.updatePrintValue();
		}
	}
}