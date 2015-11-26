/**
 * @author Haorui Wu
 * @date 11/23/2015
 * @courseNumber: 08672
 */

public class FavoriteBean {
    private int favoriteId;
    private int userId;
    private String URL;
    private String comment;
    private int clickCount;
    private int position;

    public int getFavoriteId() {
        return favoriteId;
    }

    public int getUserId() {
        return userId;
    }

    public String getURL() {
        return URL;
    }

    public String getComment() {
        return comment;
    }

    public int getClickCount() {
        return clickCount;
    }

    public int getPosition() {
        return position;
    }

    public void setFavoriteId(int i) {
        favoriteId = i;
    }

    public void setUserId(int i) {
        userId = i;
    }

    public void setURL(String s) {
        URL = s;
    }

    public void setComment(String s) {
        comment = s;
    }

    public void setClickCount(int i) {
        clickCount = i;
    }

    public void setPosition(int i) {
        position = i;
    }
}
