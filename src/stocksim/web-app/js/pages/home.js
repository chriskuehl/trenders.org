$(function() {
    $("#homeIcon-lessons").click(function(e) {
        lightbox("<div class=\"floatRight\">" +
                  "  <img src=\"static/images/icons-128/lessons-128.png\" alt=\"\" />" +
                  "  <a id=\"closeLightbox\" class=\"button\">Close</a>" +
                  "</div>" +
                  "" +
                  "<h2>Lessons</h2>" +
                  "<p>Personal finance is an important subject for all individuals, and one which can be difficult to teach because of how foreign it is to some students. We break it down into the basics and don’t assume prior knowledge.</p>" +
                  "<p>Lessons can be completed in just a few minutes, and range in topic from the stock market to saving money to the importance of diversification.</p>" +
                  "<p>If you’re a teacher, you may want to consider creating a class and having students sign up so that you can monitor their progress on lessons and allow them to compete in our virtual stock market simulation.</p>" +
                  "" + 
                  "<div class=\"clear\"></div>");
        
        e.preventDefault();
        return false;
    });
    
    $("#homeIcon-stocksim").click(function(e) {
        lightbox("<div class=\"floatRight\">" +
                  "  <img src=\"static/images/icons-128/trending-128.png\" alt=\"\" />" +
                  "  <a id=\"closeLightbox\" class=\"button\">Close</a>" +
                  "</div>" +
                  "" +
                  "<h2>Stock Simulator</h2>" +
                  "<p>Personal finance can be a complex topic, and it's easy for students to get lost in all the information available to them, especially when it comes to an abstract idea like the stock market.</p>" +
                  "<p>Our stock market simulator allows you to create a class and then have your students join it. Upon joining, you can monitor their progress, and your students can see how they're doing compared to other students in your class.</p>" +
                  "<p>Competing in a competitive stock simulation turns economics into a game instead of something else to learn about. Because of the simple nature of trenders.org, students can easily make changes to their portfolio in just a few minutes.</p>" +
                  "" + 
                  "<div class=\"clear\"></div>");
        
        e.preventDefault();
        return false;
    });
    
    $("#homeIcon-classrooms").click(function(e) {
        lightbox("<div class=\"floatRight\">" +
                  "  <img src=\"static/images/icons-128/classrooms-128.png\" alt=\"\" />" +
                  "  <a id=\"closeLightbox\" class=\"button\">Close</a>" +
                  "</div>" +
                  "" +
                  "<h2>Classrooms</h2>" +
                  "<p>Your students' privacy is important to us. We make it easy for you to isolate your students from the rest of the internet on trenders.org by creating classrooms.</p>" +
                  "<p>By creating a classroom, you'll be given an ID that your students sign up with. Once they enter this ID, they will be added to your classroom, and their information won't be visible anywhere else on the site except for inside your classroom.</p>" +
                  "<p>Classrooms have the added benefit of allowing you to set your own parameters, such as how much money a student starts with and how often they're allowed to trade.</p>" +
                  "" + 
                  "<div class=\"clear\"></div>");
        
        e.preventDefault();
        return false;
    });
});