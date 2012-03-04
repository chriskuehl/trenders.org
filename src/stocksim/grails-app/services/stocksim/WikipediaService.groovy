package stocksim

class WikipediaService {
    def getArticleSource(def title, def failOnRedirect) {
        try {
            title = URLEncoder.encode(title.replace(" ", "_"))
            def url = "http://en.wikipedia.org/wiki/$title"
            def content = url.toURL().text
            def source = between(content,
                "<div lang=\"en\" dir=\"ltr\" class=\"mw-content-ltr\">",
                "</div>				<!-- /bodycontent -->"
            )
            
            return source
        } catch (Exception ex) {
            ex.printStackTrace()
            return null
        }
    }
    
    // a regex doesn't work very well since templates can be embedded inside
    // other templates, so we'll just do it sift through each character and keep
    // track of our depth
    def cleanupHTML(def source, def maxChars) {
        source = source.replaceAll("(?s)\\<table(.*?)\\</table\\>", "")
        source = source.replaceAll("(?s)\\<div class=\"dablink\"\\>(.*?)\\</div\\>", "")
        source = source.replaceAll("(?s)\\<div class=\"metadata topicon\"(.*?)\\</div\\>", "")
        source = source.replaceAll("(?s)\\<sup(.*?)\\</sup\\>", "")
        source = source.replaceAll("href=\"/wiki/", "target=\"_blank\" href=\"http://en.wikipedia.org/wiki/")
        
        return source
    }
    
    // TODO: move these to a utility class
    public static String between(String content, String first, String last) {
        return betweenMore(content, first, last, 1);
    }

    public static String betweenMore(String content, String first, String last, int index) {
        try {
            for (int i = 0; i < index; i ++) {
                content = content.substring(content.indexOf(first) + first.length());
            }

            content = content.substring(0, content.indexOf(last));
        } catch (Exception ex) {
            return null;
        }

        return content;
    }
}
