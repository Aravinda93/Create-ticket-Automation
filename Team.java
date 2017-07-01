import javax.swing.*;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.opencsv.CSVReader;

public class Team extends JPanel {

	static JButton jbt1 = new JButton("Browse for Output File");
	static JButton jbt2 = new JButton("Browse for Add More CR's");
	private static final long serialVersionUID = 1L;

	public Team() {
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(35));
		box.add(jbt1);
		box.add(Box.createVerticalStrut(40));
		box.add(jbt2);
		box.add(Box.createVerticalStrut(35));

		add(box);
	}

	public static String getPropValues(String proper, String type) throws IOException {
		InputStream file;
		String result;
		String propFileName = null;
		Properties prop = new Properties();

		if (type.equalsIgnoreCase("config")) {
			propFileName = "config.properties";
		}

		file = new FileInputStream(propFileName);
		if (file != null) {
			try {
				prop.load(file);
			} catch (IOException e) {

			}
		}

		result = prop.getProperty(proper);

		return result;
	}

	public static void createAndShowGui() {
		JFrame frame = new JFrame("Output Translator");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 250);
		frame.add(new Team());
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.add(new JLabel(new ImageIcon("")));

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
	}

	static StringBuilder sb = new StringBuilder();
	static String[] row;
	static int i = 0;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
		jbt1.addActionListener(new Action1());
		jbt2.addActionListener(new Action2());
	}

	// Class for the button1 to browse and choose the output file from the local
	// system.
	static class Action1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			String owner = System.getProperty("user.name");
			Dimension screenSize;
			JFileChooser fileChooser = new JFileChooser("C:\\Users\\" + owner + "\\Desktop");
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			fileChooser.setPreferredSize(new java.awt.Dimension(screenSize.width / 2, screenSize.height / 2));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "csv");
			fileChooser.setFileFilter(filter);
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				try {
					java.io.File file = fileChooser.getSelectedFile();
					Scanner input = new Scanner(file);

					FileWriter outputFile = new FileWriter("SRInput- 1" + dateFormat.format(date) + ".csv");
					// File newLoc = fileChooser.getCurrentDirectory();
					File inputloc = fileChooser.getSelectedFile();

					CSVReader inputFileReader = new CSVReader(new FileReader(inputloc));
					List<String[]> inputContent = inputFileReader.readAll();

					String column = getPropValues("Column", "config");
					outputFile.append(column);

					for (Object object : inputContent) {
						if (i == 0) {
							i++;
							continue;
						}
						row = (String[]) object;
						if ((row[1].contains("S")) && row[9].contains("0")) {

							String clientName = row[6];
							String accountNumber = row[7];
							String Comment = row[3];
							String KIQ = row[4];
							String inves = row[3];
							String amount = row[2];
							String CR1 = row[8];
							// String investigation = "In"
							String colValue = "\n,Auto," + owner
									+ ",Revenue Cycle Monitoring,CAM alarm EVNTMGT_REVCY_OOB_DABAPAG on AMS_MONITORING_AGENT_"
									+ clientName + ":" + clientName + "_" + accountNumber + ",3: Moderate Impact,"
									+ owner + ",Revenue Cycle,Patient Accounting,Balancing," + clientName
									+ ",,Open,Assigned,No,Comment: " + Comment + " KIQ: " + KIQ + ",,Investigation,,,"
									+ inves + ",,,,,Correspondence,,,RCMAUDIT#A#1#" + amount + ",,,,,,,,,,,,,,,,,"
									+ CR1;

							outputFile.append(colValue);
						} else {
							continue;
						}

					}

					inputFileReader.close();
					input.close();
					outputFile.close();
					System.out.println("Your File Is Now Ready");
				}

				catch (FileNotFoundException e1) {
					System.out.println("File is already in use, close the file and try again.");
				} catch (IOException e1) {
					System.out.println("Something Went Wrong. Please Try again!!!");
				}
			}
		}

	}

	static class Action2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			String owner = System.getProperty("user.name");
			Dimension screenSize;
			JFileChooser fileChooser = new JFileChooser("C:\\Users\\" + owner + "\\Downloads");
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			fileChooser.setPreferredSize(new java.awt.Dimension(screenSize.width / 2, screenSize.height / 2));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "csv");
			fileChooser.setFileFilter(filter);
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			
		}
	}
}