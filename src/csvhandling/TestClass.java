package csvhandling;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class TestClass {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		CSVReader reader = new CSVReader(new FileReader("NSF_master_table.csv"));
		CSVWriter writer = new CSVWriter(new FileWriter("ModifiedTable.csv"), ',');

		String[] nextLine;

		String[] newFileLine;

		int count = 0;

		while ((nextLine = reader.readNext()) != null) {
			// nextLine[] is an array of values from the line

			newFileLine = nextLine;

			// newFileLine[19] = nextLine[19].toUpperCase();

			String awardNumber = nextLine[2];

			if (nextLine[19].indexOf(awardNumber) != -1) {
				//System.out.println(++count);
				System.out.println(nextLine[19].indexOf(awardNumber));
				newFileLine[19] = nextLine[19].substring(nextLine[19].indexOf(awardNumber)+awardNumber.length(),newFileLine[19].length());

			}

			writer.writeNext(newFileLine);

			// System.out.println(nextLine[19] + nextLine[1] + "etc...");
		}

		writer.close();

	}

}
