import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.opencsv.CSVReader;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Macro1 extends JPanel {

	static JButton jbt1 = new JButton("Browse for the File");
	private static final long serialVersionUID = 1L;
	static process p = new process();

	public Macro1() {
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(20));
		box.add(jbt1);

		add(box);
	}

	public static void createAndShowGui() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("PBI AUTOMATION");
		SwingUtilities.updateComponentTreeUI(frame);
		frame.getContentPane().setBackground(Color.green);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 150);
		frame.setLocationRelativeTo(null);
		frame.add(new Macro1());
		frame.setLocationByPlatform(true);
		frame.setVisible(true);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
		jbt1.addActionListener(new Action1());
	}

	// Class for the button1 to browse and choose the output file from the local
	// system.

	static StringBuilder sb = new StringBuilder();
	static String[] row;
	static String[] loopcsv;
	static String[] data;
	static int i = 0;
	static int count = 0;

	static class Action1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			p.processMethod();
			System.out.println("End of Creation");
		}
	}

	static class process {
		public void processMethod() {
			Dimension screenSize;
			JDialog.setDefaultLookAndFeelDecorated(true);
			String owner = System.getProperty("user.name");
			JFileChooser fileChooser = new JFileChooser("C:\\Users\\" + owner + "\\Desktop");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "csv");
			fileChooser.setFileFilter(filter);
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy HH-mm-ss a");
			SimpleDateFormat NextdayFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			fileChooser.setPreferredSize(new java.awt.Dimension(screenSize.width / 2, screenSize.height / 2));
			Date dt = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			c.add(Calendar.DATE, 1);
			dt = c.getTime();
			String nextday = NextdayFormat.format(dt);
			String user_name = System.getProperty("user.name");

			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				try {
					java.io.File file = fileChooser.getSelectedFile();
					Scanner input = new Scanner(file);

					FileWriter outputFile = new FileWriter("PBI INPUT " + dateFormat.format(date) + ".csv");
					File inputloc = fileChooser.getSelectedFile();
					System.out.println(inputloc);
					CSVReader inputFileReader = new CSVReader(new FileReader(inputloc));
					List<String[]> inputContent = inputFileReader.readAll();

					ArrayList<String> Client = new ArrayList<>();
					ArrayList<String> Unique_Client = new ArrayList<>();

					for (Object object : inputContent) {
						if (i == 0) {
							i++;
							continue;
						}
						row = (String[]) object;

						if ((row[3].contains("S"))) {
							Client.add(row[0]);

						}
					}

					for (String s : Client) {
						if (!Unique_Client.contains(s)) {
							Unique_Client.add(s);

						}
					}

					String column = getPropValues("CR", "config");
					List<String> Unique_CR = Arrays.asList(column.split(","));
					String heading = getPropValues("Column", "config");
					outputFile.append(heading);
					String line1 = getPropValues("line1", "config");
					String line2 = getPropValues("line2", "config");
					String line3 = getPropValues("line3", "config");
					String line4 = getPropValues("line4", "config");

					for (String client : Unique_Client) {

						for (String s1 : Unique_CR) {
							int acct_cnt = 0, acct_cnt_CR = 0;
							double acct_balace = 0.0, acct_balace_CR = 0.0;
							String append_cr_blank = null, append_cr = null;
							String RCA = null, Clean_up = null;

							for (Object ob : inputContent) {
								if (i == 0) {
									i++;
									continue;
								}

								loopcsv = (String[]) ob;

								if ((loopcsv[3].contains("S"))) {

									if (loopcsv[0].equals(client)) {

										if ((loopcsv[5].equals(" ")) || loopcsv[5].equals(null)
												|| loopcsv[5].isEmpty()) {
											if (loopcsv[5].contains(s1)) {
												acct_cnt = acct_cnt + 1;
												acct_balace = acct_balace + Double.parseDouble(loopcsv[2]);
												append_cr_blank = s1;

											}
										} else {
											if (loopcsv[5].contains(s1)) {
												acct_cnt_CR = acct_cnt_CR + 1;
												acct_balace_CR = acct_balace_CR + Double.parseDouble(loopcsv[2]);
												append_cr = s1;
												if (s1.contains("/")) {
													String[] CR = s1.split("/");
													RCA = CR[0];
													Clean_up = CR[1];
												} else {
													RCA = s1;
													Clean_up = "";
												}

											}

										}

									}

								}

							}
							if (acct_cnt > 0) {
								double Cleanup_bal = Math.round(acct_balace * 100.0) / 100.0;
								String fields = null;

								fields = "\n,Problem_RCM_AMS,Amod Rahatkar," + client + ",,RCM_balancing," + line1
										+ " Clean up; " + line2 + "Reason; " + line3 + "; " + line4 + client
										+ ",,Non-Routine Incident," + nextday
										+ ",3-Moderate/Limited,2-High,High,Problem_RCM_AMS," + user_name + ",,,Draft,,"
										+ line1 + "Clean up; " + line2 + "Reason; " + line3 + "; " + line4 + client
										+ ",Clean-up for DAB/DEB or PTR round-off has been done.,Cerner - Application Software,"
										+ "RCMAUDIT#A#" + acct_cnt + "#" + Cleanup_bal + ",General Information";
								outputFile.append(fields);

							} else if (acct_cnt_CR > 0) {
								double RCA_bal = Math.round(acct_balace_CR * 100.0) / 100.0;
								String fields = null;
								if (Clean_up != "") {

									fields = "\n,Problem_RCM_AMS,Amod Rahatkar," + client + ",,RCM_balancing," + line1
											+ "Root Cause; " + line2 + "Reasonx	; " + line3 + "; " + line4 + client
											+ ",,Non-Routine Incident," + nextday
											+ ",3-Moderate/Limited,2-High,High,Problem_RCM_AMS," + user_name
											+ ",,,Draft,," + line1 + "Root-cause; " + line2 + "Reason; " + line3 + "; "
											+ line4 + client + "," + RCA + ",Cerner - Application Software,"
											+ "RCMAUDIT#R#" + acct_cnt_CR + "#" + RCA_bal + "#" + RCA
											+ ",General Information," + Clean_up + ",General Information";
									outputFile.append(fields);
								}

								else {
									fields = "\n,Problem_RCM_AMS,Amod Rahatkar," + client + ",,RCM_balancing," + line1
											+ "Root Cause; " + line2 + "Reasonx	; " + line3 + "; " + line4 + client
											+ ",,Non-Routine Incident," + nextday
											+ ",3-Moderate/Limited,2-High,High,Problem_RCM_AMS," + user_name
											+ ",,,Draft,," + line1 + "Root-cause; " + line2 + "Reason; " + line3 + "; "
											+ line4 + client + "," + RCA + ",Cerner - Application Software,"
											+ "RCMAUDIT#R#" + acct_cnt_CR + "#" + RCA_bal + "#" + RCA
											+ ",General Information";
									outputFile.append(fields);
								}

							}
						}
					}
					inputFileReader.close();
					input.close();
					outputFile.close();

				} catch (Exception e) {
					System.out.println("Please Try Again Later!!!!");
				}

			}
		}

	}
}
