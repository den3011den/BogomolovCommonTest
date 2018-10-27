package bds.communication;

/**
 * Определяет работу с ответом сервера клиенту
 */
public class ServerResponse {

    // исходный запрос клиента
    private ClientRequest clientRequest;
    // статус ответа = 1 - удачно, = -1 - не удачно
    private int status;
    // сообщение
    private String message;
    // выигрыш. При проигрыше - отрицательный
    private float win;

    /**
     * @return исходный запрос клиента
     */
    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    /**
     * @param clientRequest исходный запрос клиента
     */
    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    /**
     * @return статус ответа = 1 - удачно, = -1 - не удачно
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status статус ответа = 1 - удачно, = -1 - не удачно
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return сообщение
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message сообщение ответа на запрос
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return выигрыш
     */
    public float getWin() {
        return win;
    }

    /**
     * @param win выигрыш
     */
    public void setWin(float win) {
        this.win = win;
    }

    /**
     * @return json-строка с данными полей объекта типа ServerResponse
     */
    public String toJson() {
        return "{\"clientRequest\":" + clientRequest.toJson() + "," +
                "\"status\":" + Integer.toString(status) + "," +
                "\"message\":\"" + message + "\"," +
                "\"win\":" + Float.toString(win) + "}";
    }

    /**
     * @param jsonString json-строка с данными полей объекта типа ServerResponse
     * @return результат операции. true - парсинг json-строки удачен, false - парсинг json-строки не удачен
     */
    public boolean fromJson(String jsonString) {

        int fromIndex = 0;
        int beginIndex = 0;
        int endIndex = 0;

        beginIndex = jsonString.indexOf("clientRequest", fromIndex) + 15;
        if (beginIndex == -1) return false;
        endIndex = jsonString.indexOf("},", fromIndex) + 1;
        if (endIndex == -1) return false;

        String clientRequestJsonString = jsonString.substring(beginIndex, endIndex);

        if (!clientRequest.fromJson(clientRequestJsonString))
            return false;

        fromIndex = endIndex + 1;

        beginIndex = jsonString.indexOf("status", fromIndex) + 8;
        if (beginIndex == -1) return false;
        endIndex = jsonString.indexOf(",", fromIndex);
        if (endIndex == -1) return false;

        try {
            status = Integer.parseInt(jsonString.substring(beginIndex, endIndex));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

        fromIndex = endIndex + 2;

        beginIndex = jsonString.indexOf("message", fromIndex) + 10;
        if (beginIndex == -1) return false;
        endIndex = jsonString.indexOf(",", fromIndex) - 1;
        if (endIndex == -1) return false;
        try {
            message = jsonString.substring(beginIndex, endIndex);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
        fromIndex = endIndex + 3;

        beginIndex = jsonString.indexOf("win", fromIndex) + 5;
        if (beginIndex == -1) return false;
        endIndex = jsonString.indexOf("}", fromIndex);
        if (endIndex == -1) return false;
        try {
            win = Float.parseFloat(jsonString.substring(beginIndex, endIndex));
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
