package bds;

/**
 * Счёт пользователя в системе
 */
public class Account {

    // уникальный id пользователя
    protected int userId;
    // наименование пользователя
    protected String userName;
    // счёт пользователя (остаток средств)
    protected float score;


    /**
     * @param userId уникальный id пользователя
     * @param userName наименование пользователя
     * @param score счёт пользователя (остаток средств)
     */
    public Account(int userId, String userName, float score) {
        this.userId = userId;
        this.userName = userName;
        this.score = score;
    }

    /**
     * @return уникальный id пользователя
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId уникальный id пользователя
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return наименование пользователя
     */
    public String getUserName() {
        return userName;
    }


    /**
     * @param userName наименование пользователя
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    /**
     * @return счёт пользователя (остаток средств)
     */
    public float getScore() {
        return score;
    }

    /**
     * @param score счёт пользователя (остаток средств)
     */
    public void setScore(float score) {
        this.score = score;
    }


    /**
     * @param difference на какое число изменяется счёт пользователя (остаток средств). Может быть положительным и отрицательным числом
     */
    public void changeScore(float difference) {
        score = score + difference;
    }
}
