package bds;

/**
 * игрок
 */
public class Gamer {
    // уникальный id игрока
    private int userId;
    // наименование игрока
    private String userName;
    // текущий остаток денежных средств игрока
    private float score = 0.0F;
    // количество хороших запросов игрока к серверу
    private int goodRequestCount = 0;
    // количество плохих запросов игрока к серверу
    private int badRequestCount = 0;
    // общее время запросов игрока к серверу
    private long allRequestTime = 0L;
    // среднее время запросов игрока к серверу
    private long averageRequestTime = 0L;

    /**
     * Получить id игрока
     * @return уникальный id игрока
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Установить id игрока
     * @param userId уникальный id игрока
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Получить наименование игрока
     * @return наименование игрока
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Установить наименование игрока
     * @param userName наименование игрока
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Получить остаток средств игрока
     * @return счёт игрока
     */
    public float getScore() {
        return score;
    }

    /**
     * измениить остаток средств игрока
     * @param difference величина изменения остатка средств игрока
     */
    public void changeScore(float difference) {
        score = score + difference;
    }

    /**
     * Получить количество хороших запросов к серверу
     * @return количество хороших запросов к серверу
     */
    public int getGoodRequestCount() {
        return goodRequestCount;
    }

    /**
     * изменить количество хороших запросов игрока к серверу
     * @param difference величина изменения количества хороших запросов игрока к серверу
     */
    public void changeGoodRequestCount(int difference) {
        goodRequestCount = goodRequestCount + difference;
        recalculateAverageRequestTime();
    }

    /**
     * Получить количество плохих запросов игрока к серверу
     * @return количество плохих запросов к серверу
     */
    public int getBadRequestCount() {
        return badRequestCount;
    }

    /**
     * изменить количество плохих запросов игрока к серверу
     * @param difference величина изменения количества плохих запросов к серверу
     */
    public void changeBadRequestCount(int difference) {
        badRequestCount = badRequestCount + difference;
        recalculateAverageRequestTime();
    }

    /**
     * Получить все время запросов игрока к серверу в миллисекундах
     * @return время всех запросов игрока к серверу
     */
    public long getAllRequestTime() {
        return allRequestTime;
    }

    /**
     * изменить общее время запросов игрока к серверу
     * @param difference величина изменения общего времени запросов игрока к серверу
     */
    public void changeAllRequestTime(long difference) {
        allRequestTime = allRequestTime + difference;
        recalculateAverageRequestTime();
    }

    /**
     * Получить среднее время запроса игрока к серверу
     * @return среднее время одного запроса игрока к серверу
     */
    public long getAverageRequestTime() {
        return averageRequestTime;
    }

    /**
     * Пересчитать среднее время запроса игрока к серверу
     */
    private void recalculateAverageRequestTime() {
        if ((badRequestCount!=0) || (goodRequestCount!=0))
            averageRequestTime = (long) allRequestTime / (badRequestCount + goodRequestCount);
        else
            averageRequestTime = 0;
    }
}
