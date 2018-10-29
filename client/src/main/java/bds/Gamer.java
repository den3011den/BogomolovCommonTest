package bds;

public class Gamer {
    private int userId;
    private String userName;
    private float score;
    private int goodRequestCount;
    private int badRequestCount;
    private long allRequestTime;
    private long averageRequestTime;

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

    public void changeScore(float difference) {
        score = score + difference;
    }

    public int getGoodRequestCount() {
        return goodRequestCount;
    }

     public void changeGoodRequestCount(int difference) {
        goodRequestCount = goodRequestCount + difference;
        recalculateAverageRequestTime();
    }

    public int getBadRequestCount() {
        return badRequestCount;
    }

    public void changeBadRequestCount(int difference) {
        badRequestCount = badRequestCount + difference;
        recalculateAverageRequestTime();
    }

    public long getAllRequestTime() {
        return allRequestTime;
    }

    public void changeAllRequestTime(long difference) {
        allRequestTime = allRequestTime + difference;
        recalculateAverageRequestTime();
    }

    public long getAverageRequestTime() {
        return averageRequestTime;
    }

    public void recalculateAverageRequestTime() {
        if ((badRequestCount!=0) || (goodRequestCount!=0))
            averageRequestTime = (long) allRequestTime / (badRequestCount + goodRequestCount);
        else
            averageRequestTime = 0;
    }
}
