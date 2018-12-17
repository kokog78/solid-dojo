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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LicenseJobTest {

	private LicenseJob job = new LicenseJob();
	
	@Before
	public void initOutput() throws SQLException {
		LicenseJob.JDBC_URL = "jdbc:h2:./data/test";
		new DatabaseInitializer().run();
	}
	
	@After
	public void resetOutput() throws SQLException {
		try (Connection conn = DriverManager.getConnection(LicenseJob.JDBC_URL)) {
			conn.createStatement().executeLargeUpdate("drop table licenses;");
			conn.commit();
		}
	}
	
	@Test
	public void should_download_and_process_empty_file() throws Exception {
		initSource("ID,TYPE,OWNER,VALID");
		
		job.run();
		
		List<Map<String, Object>> results = mapResults();
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void should_download_file_with_one_license() throws Exception {
		initSource("ID,TYPE,OWNER,VALID", "A,B,C,2020-01-01");
		
		job.run();
		
		List<Map<String, Object>> results = mapResults();
		assertThat(results).hasSize(1);
		assertLicense(results.get(0), "A", "B", "C", "2020-01-01");
	}
	
	@Test
	public void should_download_file_with_two_licenses() throws Exception {
		initSource("ID,TYPE,OWNER,VALID", "E,F,G,2021-02-02", "H,I,J,2022-03-03");
		
		job.run();
		
		List<Map<String, Object>> results = mapResults();
		assertThat(results).hasSize(2);
		assertLicense(results.get(0), "E", "F", "G", "2021-02-02");
		assertLicense(results.get(1), "H", "I", "J", "2022-03-03");
	}
	
	private void initSource(String ... lines) throws IOException {
		List<String> list = Arrays.stream(lines).collect(Collectors.toList());
		File file = File.createTempFile("download-", ".csv");
		Files.write(file.toPath(), list, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
		LicenseJob.DOWNLOAD_URL = "file:///" + file.getAbsolutePath();
	}
	
	private List<Map<String, Object>> mapResults() throws SQLException {
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
	
	private void assertLicense(Map<String, Object> license, String id, String type, String owner, String date) {
		assertThat(license.get("ID")).isEqualTo(id);
		assertThat(license.get("TYPE")).isEqualTo(type);
		assertThat(license.get("OWNER")).isEqualTo(owner);
		SimpleDateFormat fmt = new SimpleDateFormat("YYYY-MM-dd");
		assertThat(fmt.format(license.get("VALID"))).isEqualTo(date);
	}
	
}
