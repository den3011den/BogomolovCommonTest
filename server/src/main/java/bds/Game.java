package bds;

import bds.communication.ClientRequest;

public class Game {
    private static final float PROBABILITY = 0.5F; // вероятность выигрыша 50%
    private static final float BET_COEFFICIENT = 1.9F;
    private ClientRequest clientRequest;

    public Game(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public float gameRound() {
        boolean winFlag = (Math.random() <= Game.PROBABILITY) ? true : false;
        float win;
        if (winFlag) {
            win = clientRequest.getBet() * Game.BET_COEFFICIENT;
        }
        else {
            win = clientRequest.getBet() * (-1);
        }

        return win;
    }

    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

 }
