package bds.communication;

// Запрос клиента
public class ClientRequest {

    // номер запроса
    private int requestNumber;

    // id пользователя
    private int userId;

    // наименование пользователя
    private String userName;

    // ставка
    private int bet;

    // Сторона монеты, на которую делается ставка
    private String coinSide; // FRONTSIDE - лицевая сторона ("решка"), BACKSIDE - обратная сторона ("орёл")

    public ClientRequest() {
    }

    public ClientRequest(int requestNumber, int userId, String userName, int bet, String coinSide) {
        this.requestNumber = requestNumber;
        this.userId = userId;
        this.userName = userName;
        this.bet = bet;
        this.coinSide = coinSide;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(int requestNumber) {
        this.requestNumber = requestNumber;
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

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public String getCoinSide() {
        return coinSide;
    }

    public void setCoinSide(String coinSide) {
        this.coinSide = coinSide;
    }

    public String toJson() {
        return "{\"requestNumber\":" + Integer.toString(requestNumber) + "," +
                "\"userId\":" + Integer.toString(userId) + "," +
                "\"userName\":\"" + userName + "\"," +
                "\"bet\":" + bet + "," +
                "\"coinSide\":\"" + coinSide + "\"}";
    }

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

    @Override
    public String toString() {
        return toJson();
    }
}
