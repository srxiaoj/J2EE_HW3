/**
 * @author Haorui Wu
 * @date 11/23/2015
 * @courseNumber: 08672
 */

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class FavoriteForm {
    private String URL;
    private String comment;

    public FavoriteForm(HttpServletRequest request) {
        URL = sanitize(request.getParameter("URL"));
        comment = sanitize(request.getParameter("comment"));
    }

    public String getURL() {
        return URL;
    }

    public String getComment() {
        return comment;
    }
    
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (URL == null || URL.length() == 0) {
            errors.add("URL is required");
        }
        if (comment == null || comment.length() == 0) {
            errors.add("comment is required");
        }

        return errors;
    }

    private String sanitize(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
}
