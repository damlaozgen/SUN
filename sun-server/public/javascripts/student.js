$(function() {
    $.get("/students", function(students){
        $.each(student, function(index, person){
            $("#students").append($("<li>").text(student.user.name));
        });
    });});