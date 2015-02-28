package models;

import javax.persistence.*;
import java.lang.String;
import play.db.ebean.Model;

@Entity
public class User extends Model {

	@Id
	public String id;
	public String name;
    public String email;
    public String password;
    public String avatar;
    public String contactInfo;

    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }
}