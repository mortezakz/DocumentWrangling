package csvhandling;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class TestClass {

	/*public void writeTest() throws IOException {
		
	}*/
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// TestClass test = new TestClass(); 
		// test.writeTest();
		
		CSVReader reader = new CSVReader(new FileReader("NSF_master_table.csv"));
		CSVWriter writer = new CSVWriter(new FileWriter("ModifiedTable.csv"), ',');

		String[] nextLine;

		String[] newFileLine;

		int count = 0;

		//prints first row (column headings) without cleaning
		nextLine = reader.readNext();
		newFileLine = nextLine;
		writer.writeNext(newFileLine);
		
		while ((nextLine = reader.readNext()) != null) {
			// nextLine[] is an array of values from the line

			newFileLine = nextLine;

			// newFileLine[19] = nextLine[19].toUpperCase();

			// THIS IS WHER THE PROBLEM WAS. I WAS GETTING nextLine[2] instead
			// of [3]. It appears that the award number actually shows up in
			// different place in text. Not just the beginning.
			
			// checks for and deletes award number
			String awardNumber = nextLine[3];

			if (nextLine[19].indexOf(awardNumber) != -1) {
				System.out.println(++count);
				System.out.println(nextLine[19].indexOf(awardNumber));

				newFileLine[19] = nextLine[19].substring(nextLine[19].indexOf(awardNumber) + awardNumber.length() , newFileLine[19].length());
				nextLine[19] = newFileLine[19]; //updates nextLine
			}
			
			// checks for and deletes author name (lower case)
			String authorName = nextLine[24];
		
			if ((nextLine[19].indexOf(authorName) != -1) &&
					(nextLine[19].substring(nextLine[19].indexOf(authorName) + authorName.length()).startsWith("[A-Z]"))) {
				System.out.println(++count);
				System.out.println(nextLine[19].indexOf(authorName));

				nextLine[19] = newFileLine[19];
				newFileLine[19] = nextLine[19].substring(nextLine[19].indexOf(authorName) + authorName.length() , newFileLine[19].length());

			}
			
			// checks for and deletes author name (upper case)
			authorName = nextLine[24].toUpperCase();
					
			if (nextLine[19].indexOf(authorName) != -1) {
				System.out.println(++count);
				System.out.println(nextLine[19].indexOf(authorName));

				nextLine[19] = newFileLine[19];			
				newFileLine[19] = nextLine[19].substring(nextLine[19].indexOf(authorName) + authorName.length() , newFileLine[19].length());

			}
			
			// ISSUE: unintended name deletion at Row 43, others
			
			// checks for and deletes institution name (lower case)
			String institutionName = nextLine[8];
					
			if ((nextLine[19].indexOf(institutionName) != -1) &&
					(nextLine[19].substring(nextLine[19].indexOf(institutionName) + institutionName.length()).startsWith("* [A-Z]"))) {
				System.out.println(++count);
				System.out.println(nextLine[19].indexOf(institutionName));

				nextLine[19] = newFileLine[19];
				newFileLine[19] = nextLine[19].substring(nextLine[19].indexOf(institutionName) + institutionName.length() , newFileLine[19].length());

			}
			
			// checks for and deletes institution name (upper case)
			institutionName = nextLine[8].toUpperCase();
								
			if (nextLine[19].indexOf(institutionName) != -1) {
				System.out.println(++count);
				System.out.println(nextLine[19].indexOf(institutionName));

				nextLine[19] = newFileLine[19];			
				newFileLine[19] = nextLine[19].substring(nextLine[19].indexOf(institutionName) + institutionName.length() , newFileLine[19].length());

			}

			writer.writeNext(newFileLine);

			// System.out.println(nextLine[19] + nextLine[1] + "etc...");
		}

		writer.close();
	}

}
