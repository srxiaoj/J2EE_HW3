import org.genericdao.PrimaryKey;

/**
 * @author Haorui Wu
 * @date 11/23/2015
 * @courseNumber: 08672
 */

/**
 * genericDAO is very tricky with naming of getter and setter.
 * first letter of the variable should be lower case
 * @author thanksgiving
 *
 */
@PrimaryKey("favoriteId")
public class FavoriteBean {
    private int favoriteId;
    private int userId;
    private String url;
    private String comment;
    private int clickCount;
    private int position;

    public int getFavoriteId() {
        return favoriteId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUrl() {
        return url;
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

    public void setUrl(String s) {
        url = s;
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
