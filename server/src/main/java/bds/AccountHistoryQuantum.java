package bds;

public class AccountHistoryQuantum extends Account {

    private long historyId;
    private String coinSide;
    private float difference;
    private long time;


    public AccountHistoryQuantum(int historyId, float difference, long time, String coinSide, Account account) {
        super(account.userId, account.userName, account.score);
        this.historyId = historyId;
        this.difference = difference;
        this.time = time;
        this.coinSide = coinSide;
    }

    public float getDifference() {
        return difference;
    }

    public void setDifference(float difference) {
        this.difference = difference;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public String getCoinSide() {
        return coinSide;
    }

    public void setCoinSide(String coinSide) {
        this.coinSide = coinSide;
    }
}
