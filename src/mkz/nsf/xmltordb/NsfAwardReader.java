package mkz.nsf.xmltordb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.postgresql.util.PGobject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

public class NsfAwardReader {
	
	public static Logger log = Logger.getLogger(NsfAwardReader.class.getName());

	public static void ReadXmlFiles(String address) throws IOException {

		try {
			NsfDatabaseConnection.connect();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Files.walk(Paths.get(address)).forEach(filePath -> {
			if (Files.isRegularFile(filePath)) {
				try {
					ParseXmlFiles(filePath, NsfDatabaseConnection.c);
				} catch (Exception e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
	}	);

		try {
			NsfDatabaseConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// TODO: make sure you handle exceptions - so that datapoints are not lost.
	public static void ParseXmlFiles(Path filePath, Connection c)
			throws ParserConfigurationException, SQLException {

		log.info(filePath.toString());

		File fXmlFile = new File(filePath.toString());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = null;
		try {
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

		doc.getDocumentElement().normalize();

//		System.out.println("Root element :"
//				+ doc.getDocumentElement().getNodeName());

		Element awardElement = (Element) doc.getDocumentElement()
				.getElementsByTagName("Award").item(0);

		String awardTitle = awardElement.getElementsByTagName("AwardTitle")
				.item(0).getTextContent();

		// TODO what if abstract is missing
		String abstractNarration = awardElement
				.getElementsByTagName("AbstractNarration").item(0)
				.getTextContent();

		Long awardID = Long.parseLong(awardElement
				.getElementsByTagName("AwardID").item(0).getTextContent());

		// Calendar awardEffectiveDateCalendar =
		// DatatypeConverter.parseDateTime(awardElement.getElementsByTagName("AwardEffectiveDate").item(0).getTextContent());

		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		Date uDate = null;
		try {
			uDate = formatter.parse(awardElement
					.getElementsByTagName("AwardEffectiveDate").item(0)
					.getTextContent());
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

		java.sql.Date sDate = new java.sql.Date(uDate.getTime());

		NodeList programReferences = awardElement
				.getElementsByTagName("ProgramReference");

		String ProgramText = "";

		for (int i = 0; i < programReferences.getLength(); i++) {

			Element programNodes = (Element) programReferences.item(i);

			ProgramText += programNodes.getElementsByTagName("Text").item(0)
					.getTextContent();

			if (i != programReferences.getLength() - 1) {
				ProgramText += " - ";
			}

		}

		String insertStm = "INSERT INTO \""
				+ NsfDatabaseConnection.AWARD_TABLE_NAME
				+ "\" (\"Id\",\"Award\",\"Title\",\"Abstract\",\"Program\",\"EffectiveDate\") VALUES (?,?,?,?,?,?)";

		PreparedStatement insertPst = NsfDatabaseConnection.c
				.prepareStatement(insertStm);

		Document document = awardElement.getOwnerDocument();
		DOMImplementationLS domImplLS = (DOMImplementationLS) document
				.getImplementation();
		LSSerializer serializer = domImplLS.createLSSerializer();
		String awardStr = serializer.writeToString(awardElement);

		PGobject awardPgObj = new PGobject();
		awardPgObj.setType("xml");

		try {
			awardPgObj.setValue(awardStr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

		insertPst.setLong(1, awardID);
		insertPst.setObject(2, awardPgObj);
		insertPst.setString(3, awardTitle);
		insertPst.setString(4, abstractNarration);
		insertPst.setString(5, ProgramText);
		insertPst.setDate(6, sDate);

		insertPst.executeUpdate();

		// NsfDatabaseConnection.c.commit();

		// System.out.println(awardElement.getElementsByTagName("AwardEffectiveDate").item(0).getTextContent());

	}

	public static void main(String[] args) throws IOException {

		ReadXmlFiles("E:/Codes/Abstract");

	}

}
