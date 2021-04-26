package scraping;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GUI {
	
	protected static JLabel output = new JLabel();
	protected static JTextArea text = new JTextArea(10,20);
	public static Path path = null;
	protected static void popOut() {
		
		final JFrame frame = new JFrame("Rocket League Inventory Value");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		JTabbedPane tP = new JTabbedPane(JTabbedPane.LEFT); 
		tP.setBorder(BorderFactory.createEmptyBorder());
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		text.setEditable(false);
		JScrollPane sP= new JScrollPane(text,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		sP.setBorder(BorderFactory.createEmptyBorder());
		DefaultCaret caret = (DefaultCaret)text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		tP.addTab("Load", panel);
		tP.addTab("Output", sP);
		
		JFrame explorer = new JFrame();
		JButton chooseFile = new JButton("Choose File");
		chooseFile.setPreferredSize(new Dimension(200,120));
		chooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					path = Paths.get(System.getProperty("user.home")+"\\AppData\\Roaming\\bakkesmod\\bakkesmod\\data");
				} catch (Exception invalidDirectory) {
					System.out.println("Directory does not exist");
				}
				JFileChooser chooser = new JFileChooser(path.toString());
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(new FileNameExtensionFilter("JSON, CSV", "json", "csv"));
				int returnVal = chooser.showOpenDialog(explorer);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					ReadInventory.installPath = chooser.getCurrentDirectory() + "\\\\" + chooser.getSelectedFile().getName();
					text.append(chooser.getCurrentDirectory() + "\\" + chooser.getSelectedFile().getName()+"\n");
					Scraper.sum1=0;
					Scraper.sum2=0;
					Scraper.count=0;
					output.setText("");
					ReadInventory.itemsParsed = new ArrayList<String>();
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
		output.setBorder(new EmptyBorder(0,0,0,0));
		output.setFont(new Font("",Font.PLAIN,30));
		panel.add(output, gBC);
		sP.getVerticalScrollBar().setUnitIncrement(12);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
