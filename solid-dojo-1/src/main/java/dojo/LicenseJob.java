package dojo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class LicenseJob {
	
	final static String DOWNLOAD_URL = "https://raw.githubusercontent.com/kokog78/solid-dojo/master/solid-dojo-1/data/licenses.csv";
	final static String JDBC_URL = "jdbc:h2:./data/licenses";

	public void run() throws IOException, SQLException {
		File file = downloadFile();
		List<CSVRecord> licenses = parse(file);
		updateLicenses(licenses);
	}
	
	private File downloadFile() throws IOException {
		File file = File.createTempFile("licenses-", ".csv");
		URL targetURL = new URL(DOWNLOAD_URL);
		try (InputStream stream = targetURL.openStream()) {
			Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		return file;
	}
	
	private List<CSVRecord> parse(File file) throws IOException {
		try (
				BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
				CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
			) {
			return csvParser.getRecords();
		}
	}
	
	private void updateLicenses(List<CSVRecord> licenses) throws SQLException {
		try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
			PreparedStatement stmt = conn.prepareStatement("insert into licenses (id,type,owner,valid) values (?,?,?,?)");
			for (CSVRecord license : licenses) {
				stmt.setString(1, license.get("ID"));
				stmt.setString(2, license.get("TYPE"));
				stmt.setString(3, license.get("OWNER"));
				stmt.setDate(4, Date.valueOf(license.get("VALID")));
				stmt.executeUpdate();
			}
		}
	}
	
}
