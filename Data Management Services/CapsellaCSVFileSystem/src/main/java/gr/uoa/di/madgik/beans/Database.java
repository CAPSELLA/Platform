package gr.uoa.di.madgik.beans;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import gr.uoa.di.madgik.csv.model.CSVFile;

@Component
public class Database {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public Database(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void insertCSVFile(CSVFile csvFile) {
		String sql = "INSERT INTO csv_files(uuid, path, filename) VALUES (?,?,?)";
		jdbcTemplate.update(sql, new Object[] { csvFile.getUuid(), csvFile.getPath(), csvFile.getFilename() });

	}
	
	public CSVFile selectCSVFile(String uuid) {
		String sql = "SELECT * FROM csv_files where uuid=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,uuid);
		
		for (Map row : rows) {
			String filename = (String) (row.get("filename"));
			String path = (String) (row.get("path"));
			CSVFile csvFile = new CSVFile(uuid,path,filename);
			return csvFile;

		}
		
		return null;

	}
	
	public void deleteCSVFile(String uuid)
	{
		
		String deleteStatement = "DELETE FROM csv_files WHERE uuid=?";
		try {
			jdbcTemplate.update(deleteStatement, uuid);
		} catch (RuntimeException runtimeException) {

			throw runtimeException;
		}
		
	}

}
