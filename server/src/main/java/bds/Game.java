package bds;

import bds.communication.ClientRequest;

/**
 * игра - алгоритм и игровой раунд
 */
public class Game {
    // вероятность выигрыша 50%
    private static final float PROBABILITY = 0.5F;
    // коэффициент выигрыша
    private static final float BET_COEFFICIENT = 1.9F;
    // запрос клиента с данными ставки
    private ClientRequest clientRequest;

    /**
     * @param clientRequest запрос клинета с данными о ставке
     */
    public Game(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    /**
     * @return выигрыш (отрицательное число - проигрыш)
     */
    public float gameRound() {
        String winFlag = (Math.random() <= Game.PROBABILITY) ? "FRONTSIDE" : "BACKSIDE";
        float win;
        if (winFlag.equals(clientRequest.getCoinSide())) {
            win = clientRequest.getBet() * Game.BET_COEFFICIENT;
        }
        else {
            win = clientRequest.getBet() * (-1);
        }

        return win;
    }

    /**
     * @return запрос клинета с данными о ставке
     */
    public ClientRequest getClientRequest() {
        return clientRequest;
    }


    /**
     * @param clientRequest  запрос клинета с данными о ставке
     */
    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

 }
