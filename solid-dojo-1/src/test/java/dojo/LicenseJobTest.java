package dojo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;

public class LicenseJobTest {

	private LicenseJob job = new LicenseJob();
	
	@BeforeClass
	public static void initJdbc() throws SQLException {
		LicenseJob.JDBC_URL = "jdbc:h2:mem:test";
		new DatabaseInitializer().run();
	}
	
	@Test
	public void should_download_file() throws Exception {
		initFile("ID,TYPE,OWNER,VALID", "A,B,C,2020-01-01");
		job.run();
		List<Map<String, Object>> result = getLicenses();
		assertThat(result).hasSize(1);
		assertLicense(result.get(0), "A", "B", "C");
	}
	
	private void initFile(String ... lines) throws IOException {
		List<String> list = Arrays.stream(lines).collect(Collectors.toList());
		File file = File.createTempFile("download-", ".csv");
		Files.write(file.toPath(), list, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
		LicenseJob.DOWNLOAD_URL = "file:///" + file.getAbsolutePath();
	}
	
	private List<Map<String, Object>> getLicenses() throws SQLException {
		List<Map<String, Object>> result = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(LicenseJob.JDBC_URL)) {
			ResultSet licenses = conn.createStatement().executeQuery("select * from licenses");
			while (licenses.next()) {
				Map<String, Object> map = new HashMap<>();
				map.put("ID", licenses.getString("ID"));
				map.put("TYPE", licenses.getString("TYPE"));
				map.put("OWNER", licenses.getString("OWNER"));
				map.put("VALID", licenses.getDate("VALID"));
				result.add(map);
			}
		}
		return result;
	}
	
	private void assertLicense(Map<String, Object> license, String id, String type, String owner) {
		assertThat(license.get("ID")).isEqualTo(id);
		assertThat(license.get("TYPE")).isEqualTo(type);
		assertThat(license.get("OWNER")).isEqualTo(owner);
	}
	
}
