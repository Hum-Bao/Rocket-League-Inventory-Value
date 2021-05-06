package scraping;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GUI {
	
	private static JLabel output = new JLabel();
	private static JTextArea text = new JTextArea(10,20);
	private static Path path = null;
	
	public static void appendText(String textInput) {text.append(textInput);}
	public static void updateOutput(String textInput) {output.setText(textInput);}
	
	public static void popOut() {
		
		final JFrame frame = new JFrame("Rocket League Inventory Value");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JPanel panel = new JPanel();
		final JTabbedPane tP = new JTabbedPane(JTabbedPane.LEFT); 
		panel.setLayout((LayoutManager) new BoxLayout(panel, BoxLayout.Y_AXIS));
		final JScrollPane sP= new JScrollPane(text,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		sP.getVerticalScrollBar().setUnitIncrement(12);
		DefaultCaret caret = (DefaultCaret)text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		tP.addTab("Load", panel);
		tP.addTab("Output", sP);
		
		final JButton chooseFile = new JButton("Choose File");
		chooseFile.setPreferredSize(new Dimension(200,120));
		chooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {path = Paths.get(System.getProperty("user.home")+"\\AppData\\Roaming\\bakkesmod\\bakkesmod\\data");} catch (Exception invalidDirectory) {System.out.println("Directory does not exist");}
				
				final JFileChooser chooser = new JFileChooser(path.toString());
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(new FileNameExtensionFilter("JSON, CSV", "json", "csv"));
				byte returnVal = (byte)chooser.showOpenDialog(new JFrame());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					ReadInventory.updateInstallPath(chooser.getCurrentDirectory() + "\\\\" + chooser.getSelectedFile().getName());
					text.append(chooser.getCurrentDirectory() + "\\" + chooser.getSelectedFile().getName()+"\n");
					output.setText("");
					ReadInventory.resetArrayList();
					Scraper.resetSums();
					Scraper.updatePrintValue(false);
					Loading.worker();
					ReadInventory.inventoryThread();
				}
			}
		});
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gBC = new GridBagConstraints();
		gBC.gridwidth=GridBagConstraints.REMAINDER;
		gBC.anchor = GridBagConstraints.CENTER;
		frame.getContentPane().add(tP);
		panel.add(chooseFile, gBC);
		output.setFont(new Font("",Font.PLAIN,30));
		panel.add(output, gBC);
		text.setEditable(false);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
