package bds;


/**
 * Единичное событие в жизни счёта пользователя (Account)
 */
public class AccountHistoryQuantum extends Account {

    // уникальный код события
    private long historyId;
    // ставка (на какую сторону монеты)
    private String coinSide;
    // изменение счёта игрока
    private float difference;
    // время совершения события со счётом пользователя в миллисекундах
    private long time;


    /**
     * @param historyId уникальный код события
     * @param difference изменение счёта игрока
     * @param time время совершения события со счётом пользователя в миллисекундах
     * @param coinSide ставка (на какую сторону монеты)
     * @param account
     */
    public AccountHistoryQuantum(int historyId, float difference, long time, String coinSide, Account account) {
        super(account.userId, account.userName, account.score);
        this.historyId = historyId;
        this.difference = difference;
        this.time = time;
        this.coinSide = coinSide;
    }

    /**
     * @return изменение счёта игрока
     */
    public float getDifference() {
        return difference;
    }

    /**
     * @param difference изменение счёта игрока
     */
    public void setDifference(float difference) {
        this.difference = difference;
    }

    /**
     * @return время совершения события со счётом пользователя в миллисекундах
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time время совершения события со счётом пользователя в миллисекундах
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return уникальный код события
     */
    public long getHistoryId() {
        return historyId;
    }

    /**
     * @param historyId уникальный код события
     */
    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    /**
     * @return ставка (на какую сторону монеты)
     */
    public String getCoinSide() {
        return coinSide;
    }

    /**
     * @param coinSide ставка (на какую сторону монеты)
     */
    public void setCoinSide(String coinSide) {
        this.coinSide = coinSide;
    }
}
