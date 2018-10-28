package bds;

public class Account {

    protected int userId;
    protected String userName;
    protected float score;

    public Account(int userId, String userName, float score) {
        this.userId = userId;
        this.userName = userName;
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void changeScore(float difference) {
        score = score + difference;
    }
}
