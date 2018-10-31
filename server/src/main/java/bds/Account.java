package bds;

/**
 * Счёт пользователя в системе
 */
public class Account {

    // уникальный id пользователя
    protected int userId;
    // наименование пользователя
    protected String userName;
    // текущий статок средств пользователя в игровых единицах
    protected float score;


    /**
     * Параметризованый конструктор
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
     * Получить id пользователя (аккаунта)
     * @return уникальный id пользователя
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Установить id пользователя (аккаунта)
     * @param userId уникальный id пользователя
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Получить наименование пользователя
     * @return наименование пользователя
     */
    public String getUserName() {
        return userName;
    }


    /**
     * Устанвоить наименование пользователя
     * @param userName наименование пользователя
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    /**
     * Получить текущий остаток средств пользователя
     * @return текущий остаток средств пользователя аккаунта
     */
    public float getScore() {
        return score;
    }

    /**
     * Установить текущий остаток средств пользователя
     * @param score текущий остаток средств пользователя
     */
    public void setScore(float score) {
        this.score = score;
    }


    /**
     * Nзменить текущий остаток средств пользователя аккаунта
     * @param difference на какое число изменится текущий остаток средств пользователя аккаунта. Может быть положительным и отрицательным числом
     */
    public void changeScore(float difference) {
        score = score + difference;
    }
}
