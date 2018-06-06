package gr.uoa.di.madgik.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "image_files")
public class ImageFile implements Serializable{
     
    public String getUuid() {
		return uuid;
	}

	public String getPath() {
		return path;
	}

	public String getFilename() {
		return filename;
	}

	private static final long serialVersionUID = -3009157732242241606L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
     
    @Column(name = "uuid")
    private String uuid;
     
    @Column(name = "path")
    private String path;
    
    @Column(name = "filename")
    private String filename;
 
    protected ImageFile() {}
 
    public ImageFile(String uuid, String path, String filename) {
        this.uuid = uuid;
        this.path = path;
        this.filename = filename;
    }
 
 
}
