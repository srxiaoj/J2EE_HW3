

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class FavoriteForm {
    private String URL;

    public FavoriteForm(HttpServletRequest request) {
        URL = sanitize(request.getParameter("URL"));
    }

    public String getURL() {
        return URL;
    }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (URL == null || URL.length() == 0) {
            errors.add("Item is required");
        }

        return errors;
    }

    private String sanitize(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
}
