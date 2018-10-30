package bds.communication;

/**
 * Определяет работу с запросом клиента к серверу
 *
 * @author Богомолов Д.С.
 * @version 1.0
 */
public class ClientRequest {

    // номер запроса пользователя
    private int requestNumber;

    // id пользователя
    private int userId;

    // наименование пользователя
    private String userName;

    // ставка
    private int bet;

    // Сторона монеты, на которую делается ставка
    // FRONTSIDE - лицевая сторона ("решка"), BACKSIDE - обратная сторона ("орёл")
    private String coinSide;

    /**
     * Конструктор без параметров
     */
    public ClientRequest() {
    }

    /**
     * Параметризованный конструктор
     *
     * @param requestNumber номер по порядку запроса пользователя
     * @param userId        id пользователя
     * @param userName      наименование пользователя
     * @param bet           ставка
     * @param coinSide      cторона монеты, на которую делается ставка. FRONTSIDE - лицевая сторона ("решка"), BACKSIDE - обратная сторона ("орёл")
     */
    public ClientRequest(int requestNumber, int userId, String userName, int bet, String coinSide) {
        this.requestNumber = requestNumber;
        this.userId = userId;
        this.userName = userName;
        this.bet = bet;
        this.coinSide = coinSide;
    }

    /**
     * @return номер по порядку запроса пользователя
     */
    public int getRequestNumber() {
        return requestNumber;
    }

    /**
     * @param requestNumber номер по порядку запроса пользователя
     */
    public void setRequestNumber(int requestNumber) {
        this.requestNumber = requestNumber;
    }

    /**
     * @return id пользователя
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId id пользователя
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
     * @return ставка
     */
    public int getBet() {
        return bet;
    }

    /**
     * @param bet ставка
     */
    public void setBet(int bet) {
        this.bet = bet;
    }

    /**
     * @return cторона монеты, на которую делается ставка. FRONTSIDE - лицевая сторона ("решка"), BACKSIDE - обратная сторона ("орёл")
     */
    public String getCoinSide() {
        return coinSide;
    }

    /**
     * @param coinSide cторона монеты, на которую делается ставка. FRONTSIDE - лицевая сторона ("решка"), BACKSIDE - обратная сторона ("орёл")
     */
    public void setCoinSide(String coinSide) {
        this.coinSide = coinSide;
    }

    /**
     * @return поля объекта типа ClientRequest переведённые в json-строку
     */
    public String toJson() {
        return "{\"requestNumber\":" + Integer.toString(requestNumber) + "," +
                "\"userId\":" + Integer.toString(userId) + "," +
                "\"userName\":\"" + userName + "\"," +
                "\"bet\":" + bet + "," +
                "\"coinSide\":\"" + coinSide + "\"}";
    }

    /**
     * @param jsonString поля объекта типа ClientRequest переведённые в json-строку
     * @return true - операция получения данных объекта из json-строки прошла удачно, false - операция получения данных объекта из json-строки прошла не удачно
     */
    public boolean fromJson(String jsonString) {

        int fromIndex = 0;
        int beginIndex = 0;
        int endIndex = 0;

        beginIndex = jsonString.indexOf("requestNumber", fromIndex) + 15;
        if (beginIndex == -1) return false;
        endIndex = jsonString.indexOf(",", fromIndex);
        if (endIndex == -1) return false;
        try {
            requestNumber = Integer.parseInt(jsonString.substring(beginIndex, endIndex));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

        fromIndex = endIndex + 2;

        beginIndex = jsonString.indexOf("userId", fromIndex) + 8;
        if (beginIndex == -1) return false;
        endIndex = jsonString.indexOf(",", fromIndex);
        if (endIndex == -1) return false;

        try {
            userId = Integer.parseInt(jsonString.substring(beginIndex, endIndex));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

        fromIndex = endIndex + 2;

        beginIndex = jsonString.indexOf("userName", fromIndex) + 11;
        if (beginIndex == -1) return false;
        endIndex = jsonString.indexOf(",", fromIndex) - 1;
        if (endIndex == -1) return false;
        try {
            userName = jsonString.substring(beginIndex, endIndex);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

        fromIndex = endIndex + 3;

        beginIndex = jsonString.indexOf("bet", fromIndex) + 5;
        if (beginIndex == -1) return false;
        endIndex = jsonString.indexOf(",", fromIndex);
        if (endIndex == -1) return false;

        try {
            bet = Integer.parseInt(jsonString.substring(beginIndex, endIndex));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

        fromIndex = endIndex + 2;

        beginIndex = jsonString.indexOf("coinSide", fromIndex) + 11;
        if (beginIndex == -1) return false;
        endIndex = jsonString.indexOf("}", fromIndex) - 1;
        if (endIndex == -1) return false;
        try {
            coinSide = jsonString.substring(beginIndex, endIndex).toUpperCase();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * @return представление полей объекта в json-строке
     */
    @Override
    public String toString() {
        return toJson();
    }
}
