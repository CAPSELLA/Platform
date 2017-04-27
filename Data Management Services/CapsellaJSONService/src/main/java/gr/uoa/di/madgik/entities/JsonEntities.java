package gr.uoa.di.madgik.entities;

import java.sql.Timestamp;

import org.bson.Document;
import org.springframework.data.annotation.Id;

public class JsonEntities {
	
	 @Id
	    private String id;
	    private String uploaderUserame;
	    private Timestamp timestamp;
	    Document doc;
	    public JsonEntities() {
	    }

	    public JsonEntities(String uploaderUserame, Document doc, Timestamp timestamp) {
	        this.setUploaderUserame(uploaderUserame);
	        this.doc = doc;
	        this.setTimestamp(timestamp);
	    }

		public Timestamp getTimestamp() {
			return timestamp;
		}

	public void setTimestamp(Timestamp timestamp) {
			this.timestamp = timestamp;
		}

		public String getUploaderUserame() {
			return uploaderUserame;
		}

		public void setUploaderUserame(String uploaderUserame) {
			this.uploaderUserame = uploaderUserame;
		}

	   

}
