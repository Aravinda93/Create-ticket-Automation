import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MACRO2 extends JPanel {

	static JButton jbt1 = new JButton("Browse for the File");
	private static final long serialVersionUID = 1L;
	static process p = new process();

	public MACRO2() {
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(20));
		box.add(jbt1);

		add(box);
	}

	public static void createAndShowGui() {

		JFrame frame = new JFrame("PBI AUTOMATION");
		frame.getContentPane().setBackground(Color.green);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 200);
		frame.setLocationRelativeTo(null);
		frame.add(new MACRO2());
		frame.setLocationByPlatform(true);
		frame.setVisible(true);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

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
			String owner = System.getProperty("user.name");
			JFileChooser fileChooser = new JFileChooser("C:\\Users\\" + owner + "\\Desktop");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "csv");
			fileChooser.setFileFilter(filter);
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy HH-mm-ss");

			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				try {
					java.io.File file = fileChooser.getSelectedFile();
					Scanner input = new Scanner(file);

					FileWriter outputFile = new FileWriter("PBI INPUT " + dateFormat.format(date) + ".csv");
					File inputloc = fileChooser.getSelectedFile();
					System.out.println(inputloc);
					CSVReader inputFileReader = new CSVReader(new FileReader(inputloc));
					List<String[]> inputContent = inputFileReader.readAll();

					outputFile.append("Client");
					outputFile.append(',');

					outputFile.append("Account Count");
					outputFile.append(',');

					outputFile.append("Balance Sum");
					outputFile.append(',');

					outputFile.append("CR's");

					ArrayList<String> Client = new ArrayList<>();
					ArrayList<String> Unique_Client = new ArrayList<>();
					ArrayList<String> CR = new ArrayList<>();
					ArrayList<String> Unique_CR = new ArrayList<>();

					for (Object object : inputContent) {
						if (i == 0) {
							i++;
							continue;
						}
						row = (String[]) object;

						if ((row[3].contains("S"))) {
							Client.add(row[0]);
							CR.add(row[5]);

						}
					}

					for (String s : Client) {
						if (!Unique_Client.contains(s)) {
							Unique_Client.add(s);
							

						}
					}

					for (String s : CR) {
						if (!Unique_CR.contains(s)) {
							Unique_CR.add(s);

						}

					}

					for (String client : Unique_Client) {

						for (String s1 : Unique_CR) {
							int acct_cnt = 0, acct_cnt_CR = 0;
							double acct_balace = 0.0, acct_balace_CR = 0.0;
							int cr_length = 0;
							String append_cr_blank = null, append_cr = null;

							for (Object ob : inputContent) {
								if (i == 0) {
									i++;
									continue;
								}

								loopcsv = (String[]) ob;

								if ((loopcsv[3].contains("S"))) {

									if (loopcsv[0].equals(client)) {
										if ((loopcsv[5].equals("")) || loopcsv[5].equals(null)) {
											if (loopcsv[5].equals(s1)) {
												acct_cnt = acct_cnt + 1;
												acct_balace = acct_balace + Double.parseDouble(loopcsv[2]);
												append_cr_blank = s1;

											}
										} else {
											if (loopcsv[5].equals(s1)) {
												acct_cnt_CR = acct_cnt_CR + 1;
												acct_balace_CR = acct_balace_CR + Double.parseDouble(loopcsv[2]);
												append_cr = s1;

											}

										}

									}

								}

							}

							if (acct_cnt > 0) {
								// DecimalFormat df2 = new DecimalFormat(".##");
								// double format_balance = 0.0;
								// format_balance = df2.format(acct_balace);
								outputFile.append('\n');
								outputFile.append(client);
								outputFile.append(',');
								outputFile.append(String.valueOf(acct_cnt));
								outputFile.append(',');
								outputFile.append(String.valueOf(acct_balace));
								outputFile.append(',');
								outputFile.append(append_cr_blank);
							}

							else if (acct_cnt_CR > 0) {
								outputFile.append('\n');
								outputFile.append(client);
								outputFile.append(',');
								outputFile.append(String.valueOf(acct_cnt_CR));
								outputFile.append(',');
								outputFile.append(String.valueOf(acct_balace_CR));
								outputFile.append(',');
								outputFile.append(append_cr);

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
