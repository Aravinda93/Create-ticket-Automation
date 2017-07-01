import javax.sound.sampled.LineListener;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Browse {

	public static void main(String[] args) {
		//To store csv data into arraylist
		ArrayList<String> inputcsv = new ArrayList<>();
		ArrayList<String> outputcsv = new ArrayList<>();
		
		//To store processed output
		ArrayList<String> comparecsv = new ArrayList<>();
		ArrayList<String> finalunique = new ArrayList<>();
		
		// Input of file which needs to be parsed
		String csvFile = "C:\\Users\\ab048536\\Desktop\\BALANCING MAILS\\SAMPLE\\test2.csv";
		String newcsv = "C:\\Users\\ab048536\\Desktop\\BALANCING MAILS\\SAMPLE\\test1.csv";
		BufferedReader csvReader;
		String csvSplitBy = ",";

		// Read and store the data for output csv (1st csv)
		try {
			String line;
			csvReader = new BufferedReader(new FileReader(csvFile));
			while ((line = csvReader.readLine()) != null) {
				inputcsv.add(line);
			}
			csvReader.close();
		} catch (Exception e) {
			System.out.println("Error reading first file");
		}

		// Read and store the data for new input csv (2nd csv)
		try {
			String line;
			csvReader = new BufferedReader(new FileReader(newcsv));
			while ((line = csvReader.readLine()) != null) {
				outputcsv.add(line);
			}
			csvReader.close();
		} catch (Exception e) {
			System.out.println("Error reading second file");
		}

		// Remove the duplicates based on column
		for (int i = 0; i < inputcsv.size(); i++) {
			String[] data = inputcsv.get(i).split(csvSplitBy);
			String col1 = data[0];
			String col2 = data[1];
			String col3 = data[2];
			boolean uniquecolumn = true;
			for (int j = 0; j < outputcsv.size(); j++) {
				String[] tmp = outputcsv.get(j).split(csvSplitBy);
				if (j != i) {
					if (col1.equals(tmp[0]) && col2.equals(tmp[1]) && col3.equals(tmp[2])) {
						uniquecolumn = false;
						break;
					}
				}
			}
			if (uniquecolumn) {
				comparecsv.add(inputcsv.get(i));
			}
		}

		// Remove duplicates from finalist
		for (int i = 0; i < comparecsv.size(); i++) {
			String[] data = comparecsv.get(i).split(csvSplitBy);
			String col1 = data[0];
			String col2 = data[1];
			String col3 = data[2];
			boolean uniquecolumn = true;
			for (int j = 0; j < comparecsv.size(); j++) {
				String[] tmp = comparecsv.get(j).split(csvSplitBy);
				if (j != i) {
					if (col1.equals(tmp[0]) && col2.equals(tmp[1]) && col3.equals(tmp[2])) {
						uniquecolumn = false;
						break;
					}
				}
			}
			if (uniquecolumn) {
				finalunique.add(comparecsv.get(i));
			}
		}

		if (finalunique.isEmpty()) {
			System.out.println("There could be no data results gathered from the supplied\n"
					+ "CSV file which meets the required criteria.");
		} else {
			System.out.println("Column 1\tColumn 2\tColumn 3\tColumn 4");
			System.out.println("================================================" + "========================\n");
			for (int i = 0; i < finalunique.size(); i++) {
				String[] tmp = finalunique.get(i).split(csvSplitBy);
				System.out.println(tmp[0] + "\t" + tmp[1]+ "\t" + tmp[2]+"\t"+tmp[3]);
			}
		}
	}
}