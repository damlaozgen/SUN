package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import models.*;

import play.data.*;

import java.lang.Exception;
import java.util.List;
import java.util.Map;

import play.db.ebean.Model;

import static play.libs.Json.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }
    public static Result createStudentPage(){
        return ok(createStudentPage.render());
    }

    public static Result addStudent() {
        DynamicForm.Dynamic f = Form.form().bindFromRequest().get();
        Map<String, String> data = f.getData();
        User user = new User(data.get("name"), data.get("email"), data.get("password"));
        user.save();
        Student student = new Student(user);
        student.save();
        return redirect(routes.Application.index());
    }

    public static Result getStudents() {
        User user = new User("data", "data", "data");
        user.save();
        Student student = new Student(user);
        student.save();
        List<Student> students = new Model.Finder(String.class, Student.class).all();
        return ok(toJson(students));
    }
}
