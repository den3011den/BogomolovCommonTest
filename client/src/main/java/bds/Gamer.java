package bds;

/**
 * игрок
 */
public class Gamer {
    // уникальный id игрока
    private int userId;
    // наименование игрока
    private String userName;
    // счёт игрока
    private float score = 0.0F;
    // количество хороших запросов к серверу
    private int goodRequestCount = 0;
    // количество плохих запросов к серверу
    private int badRequestCount = 0;
    // общее время запросов к серверу
    private long allRequestTime = 0L;
    // среднее время запросов к серверу
    private long averageRequestTime = 0L;

    /**
     * @return уникальный id игрока
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId уникальный id игрока
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return наименование игрока
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName наименование игрока
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return счёт игрока
     */
    public float getScore() {
        return score;
    }

    /**
     * @param difference величина изменения счёта игрока
     */
    public void changeScore(float difference) {
        score = score + difference;
    }

    /**
     * @return количество хороших запросов к серверу
     */
    public int getGoodRequestCount() {
        return goodRequestCount;
    }

     public void changeGoodRequestCount(int difference) {
        goodRequestCount = goodRequestCount + difference;
        recalculateAverageRequestTime();
    }

    /**
     * @return количество плохих запросов к серверу
     */
    public int getBadRequestCount() {
        return badRequestCount;
    }

    /**
     * @param difference величина изменения количества плохих запросов к серверу
     */
    public void changeBadRequestCount(int difference) {
        badRequestCount = badRequestCount + difference;
        recalculateAverageRequestTime();
    }

    /**
     * @return время всех запросов к серверу
     */
    public long getAllRequestTime() {
        return allRequestTime;
    }

    /**
     * @param difference величина изменения времени всех запросов сервера
     */
    public void changeAllRequestTime(long difference) {
        allRequestTime = allRequestTime + difference;
        recalculateAverageRequestTime();
    }

    /**
     * @return среднее время одного запроса
     */
    public long getAverageRequestTime() {
        return averageRequestTime;
    }

    /**
     * Считает среднее время запроса при изменении количества плохих запросов, хороших запросов, обещего времени запросов
     */
    private void recalculateAverageRequestTime() {
        if ((badRequestCount!=0) || (goodRequestCount!=0))
            averageRequestTime = (long) allRequestTime / (badRequestCount + goodRequestCount);
        else
            averageRequestTime = 0;
    }
}
