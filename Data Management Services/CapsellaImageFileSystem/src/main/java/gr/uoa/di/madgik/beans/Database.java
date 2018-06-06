package gr.uoa.di.madgik.beans;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import gr.uoa.di.madgik.model.ImageFile;


@Component
public class Database {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public Database(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void insertImageFile(ImageFile imageFile) {
		String sql = "INSERT INTO image_files(uuid, path, filename) VALUES (?,?,?)";
		jdbcTemplate.update(sql, new Object[] { imageFile.getUuid(), imageFile.getPath(), imageFile.getFilename() });

	}
	
	public ImageFile selectImageFile(String uuid) {
		String sql = "SELECT * FROM image_files where uuid=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,uuid);
		
		for (Map row : rows) {
			String filename = (String) (row.get("filename"));
			String path = (String) (row.get("path"));
			ImageFile imageFile = new ImageFile(uuid,path,filename);
			return imageFile;

		}
		
		return null;

	}
	
	public void deleteImageFile(String uuid)
	{
		
		String deleteStatement = "DELETE FROM image_files WHERE uuid=?";
		try {
			jdbcTemplate.update(deleteStatement, uuid);
		} catch (RuntimeException runtimeException) {

			throw runtimeException;
		}
		
	}

}