package bds;

import bds.communication.ClientRequest;
import bds.communication.ServerResponse;

public class Game {
    private static final float PROBABILITY = 0.5F; // вероятность выигрыша 50%
    private static final float BET_COEFFICIENT = 1.9F;
    private ClientRequest clientRequest;
    private ServerResponse serverResponse;

    public Game(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public boolean gameRound() {
        boolean winFlag = (Math.random() <= Game.PROBABILITY) ? true : false;
        float win = clientRequest.getBet();
        String message = "";
        if (winFlag) {
            win = clientRequest.getBet() * Game.BET_COEFFICIENT;
            message = "You won " + String.valueOf(win);
        }
        else {
            win = clientRequest.getBet() * (-1);
            message = "You lost " + clientRequest.getBet();
        }

        serverResponse = new ServerResponse();
        serverResponse.setClientRequest(clientRequest);
        serverResponse.setWin(win);
        serverResponse.setStatus(1);
        serverResponse.setMessage(message);

        return true;
    }

    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public ServerResponse getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }
}
