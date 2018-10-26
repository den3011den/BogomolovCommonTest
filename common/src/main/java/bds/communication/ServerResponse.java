package bds.communication;

// класс ответа сервера
public class ServerResponse {
    private ClientRequest clientRequest;
    private int status;
    private String message;
    private float win;

    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getWin() {
        return win;
    }

    public void setWin(float win) {
        this.win = win;
    }

    public String toJson() {
        return "{\"clientRequest\":" + clientRequest.toJson() + "," +
                "\"status\":" + Integer.toString(status) + "," +
                "\"message\":\"" + message + "\"," +
                "\"win\":" + Float.toString(win) + "}";
    }

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
