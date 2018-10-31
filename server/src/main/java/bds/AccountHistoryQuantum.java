package bds;


/**
 * Событие истории аккаунта
 */
public class AccountHistoryQuantum extends Account {

    // уникальный код события истории аккаунта
    private long historyId;
    // ставка (на какую сторону монеты)
    private String coinSide;
    // изменение остатка на счёте игрока в результате события
    private float difference;
    // время совершения события со счётом пользователя в миллисекундах с 1 января 1970 UTC
    private long time;


    /**
     * Параметризованный конструктор объекта
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
     * Получить изменение остатка на счете пользователя в результате собылия с аккаунтом
     * @return изменение остатка на счете игрока в результате собылия с аккаунтом
     */
    public float getDifference() {
        return difference;
    }

    /**
     * Установить величину изменения остатка на счете пользователя в результате собылия с аккаунтом
     * @param difference изменение счёта игрока
     */
    public void setDifference(float difference) {
        this.difference = difference;
    }

    /**
     * Получить время события аккаунта в миллисекундах с
     * @return время совершения события со счётом пользователя в миллисекундах
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time время совершения события со счётом пользователя в миллисекундах с 1 января 1970 UTC
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Получить уникальный идентификатор события
     * @return уникальный идентификатор события
     */
    public long getHistoryId() {
        return historyId;
    }

    /**
     * Установить идентификатор события
     * @param historyId уникальный идентификатор события
     */
    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    /**
     * Получить ставку (на какую сторону монеты была ставка), результатом которой является событие
     * (FRONTSIDE - решка, BACKSIDE - орел)
     * @return ставка (на какую сторону монеты)
     */
    public String getCoinSide() {
        return coinSide;
    }

    /**
     * Установить ставку (на какую сторону монеты была ставка), результатом которой является событие
     * @param coinSide ставка (на какую сторону монеты)
     */
    public void setCoinSide(String coinSide) {
        this.coinSide = coinSide;
    }
}
