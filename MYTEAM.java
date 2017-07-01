import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import com.opencsv.CSVReader;

public class MYTEAM {

	static int i = 0, j=0;
	static String[] file1;
	static String[] file2;

	public static void main(String[] args) {
		// Read the first file
		try {
			File inputloc = new File("C:\\Users\\ab048536\\Desktop\\BALANCING MAILS\\SAMPLE\\test1.csv");
			CSVReader inputFileReader = new CSVReader(new FileReader(inputloc));
			List<String[]> inputContent = inputFileReader.readAll();

			for (Object object : inputContent) {
				if (i == 0) {
					i++;
					continue;
				}
				file1 = (String[]) object;
				System.out.println(file1[0]);
			}
			
			inputFileReader.close();
		} 
		
		catch (Exception e) {
			System.out.println("Something went wrong with 1st file");
		}
	
	try {
		File inputloc = new File("C:\\Users\\ab048536\\Desktop\\BALANCING MAILS\\SAMPLE\\test2.csv");
		CSVReader inputFileReader = new CSVReader(new FileReader(inputloc));
		List<String[]> inputContent = inputFileReader.readAll();

		for (Object object : inputContent) {
			if (j == 0) {
				j++;
				continue;
			}
			file2 = (String[]) object;
			System.out.println(file2[1]);
		}
		
		inputFileReader.close();
	} 
	
	catch (Exception e) {
		System.out.println("Something went wrong with 1st file");
	}
}

}
