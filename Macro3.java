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

public class Macro3 extends JPanel {

	static JButton jbt1 = new JButton("Browse for the File");
	private static final long serialVersionUID = 1L;
	static process p = new process();

	public Macro3() {
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(20));
		jbt1.setBackground(Color.WHITE);
		jbt1.setForeground(Color.BLACK);
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
		frame.add(new Macro3());
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
	static String[] trimmed;
	static String[] data;
	static int i = 0;
	static int count = 0;

	static class Action1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			jbt1.setEnabled(false);
			p.processMethod();
			System.out.println("Your PBI Input File has been Created, Thank you!!!!!");
			jbt1.setEnabled(true);
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
							String reason = null;

							for (Object ob : inputContent) {
								if (i == 0) {
									i++;
									continue;
								}

								loopcsv = (String[]) ob;
								loopcsv[0] = loopcsv[0].trim();
								loopcsv[5] = loopcsv[5].trim();

								if ((loopcsv[3].contains("S"))) {

									if (loopcsv[0].equals(client)) {

										if (loopcsv[5].isEmpty() && s1.equals(null)) {

											if (loopcsv[5].equals(s1) && loopcsv[5].contentEquals("")) {

												acct_cnt = acct_cnt + 1;
												acct_balace = acct_balace + Double.parseDouble(loopcsv[2]);
												append_cr_blank = s1;
											}
										} else {
											if (loopcsv[5].contains(s1)) {
												acct_cnt_CR = acct_cnt_CR + 1;
												acct_balace_CR = acct_balace_CR + Double.parseDouble(loopcsv[2]);
												append_cr = s1;

											}

										}

									}

								}

							}
							if (s1.contains("/")) {
								String[] CR = s1.split("/");
								RCA = CR[0];
								Clean_up = CR[1];
							} else {
								RCA = s1;
								Clean_up = "";
							}

							if (s1.isEmpty() && s1.equals(null)) {
								reason = "PTR round-off clean up DAB/DEB correction has been done;";
							}

							if ((s1.contains("1-11071804441")) || (s1.contains("1-10189632163"))) {
								reason = "The pft encounter related to the accounts have Bad-debt issue;";
							}
							if ((s1.contains("1-9259107464")) || (s1.contains("1-11483542281"))
									|| (s1.contains("1-11745475461"))) {
								reason = "The accounts have GL vs GSR issue;";
							}
							if ((s1.contains("1-12230053841")) || s1.contains("1-6500551913")
									|| s1.contains("1-10408199848")) {
								reason = "PTR round-off clean up has been done for these accounts;";
							}
							if (s1.contains("1-10517258651")) {
								reason = "The accounts have split-billing issue";
							}

							if (s1.contains("1-7341928981")) {
								reason = "Combine: Inactive account with 0 balance is having active pft_encntr;";
							}

							if (acct_cnt > 0) {
								double Cleanup_bal = Math.round(acct_balace * 100.0) / 100.0;
								String fields = null;

								String notes_wc = line1 + " Clean up; " + line2 + reason + line3 + "; " + line4
										+ client;

								fields = "\n,Problem_RCM_AMS,Amod Rahatkar," + client + ",,RCM_balancing," + notes_wc
										+ ",,Non-Routine Incident," + nextday
										+ ",3-Moderate/Limited,2-High,High,Problem_RCM_AMS," + user_name + ",,,Completed,Unresolvable,,"
										+ notes_wc
										+ ",Clean-up for DAB/DEB or PTR round-off has been done.,Cerner - Application Software,"
										+ "RCMAUDIT#A#" + acct_cnt + "#" + Cleanup_bal + ",General Information";
								outputFile.append(fields);

							} else if (acct_cnt_CR > 0) {
								double RCA_bal = Math.round(acct_balace_CR * 100.0) / 100.0;
								String fields = null;
								if (Clean_up != "") {

									String notes_wc = line1 + "Root Cause; " + line2 + reason + line3 + "; " + line4
											+ client;

									fields = "\n,Problem_RCM_AMS,Amod Rahatkar," + client + ",,RCM_balancing,"
											+ notes_wc + ",,Non-Routine Incident," + nextday
											+ ",3-Moderate/Limited,2-High,High,Problem_RCM_AMS," + user_name
											+ ",,,Completed,Unresolvable,," + notes_wc + "," + RCA + ",Cerner - Application Software,"
											+ "RCMAUDIT#R#" + acct_cnt_CR + "#" + RCA_bal + "#" + RCA
											+ ",General Information," + Clean_up + ",General Information";
									outputFile.append(fields);
								}

								else {
									String notes_wc = line1 + "Root Cause; " + line2 + reason + line3 + "; " + line4
											+ client;
									fields = "\n,Problem_RCM_AMS,Amod Rahatkar," + client + ",,RCM_balancing,"
											+ notes_wc + ",,Non-Routine Incident," + nextday
											+ ",3-Moderate/Limited,2-High,High,Problem_RCM_AMS," + user_name
											+ ",,,Completed,Unresolvable,," + notes_wc + "," + RCA + ",Cerner - Application Software,"
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
