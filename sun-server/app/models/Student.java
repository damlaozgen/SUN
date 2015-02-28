package models;

import play.db.ebean.Model;
import javax.persistence.*;
import java.lang.String;

@Entity
public class Student extends Model {

    public Student(User user){
        super();
        this.user = user;
    }
    @OneToOne
    public User user;

    @ManyToMany(cascade = CascadeType.ALL)
    public Student friends;

    public int points;
}