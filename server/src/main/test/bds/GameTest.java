package bds;

import bds.communication.ClientRequest;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void gameRound() {
        ClientRequest clientRequest = new ClientRequest(333, 555, "user555", 20, "BACKSIDE");
        Game game = new Game(clientRequest);
        float win = game.gameRound();
        if (win > 0)
            assertEquals(20 * 1.9, win, 0.001);
        else
            assertEquals(-20, win, 0.001);
    }

    @Test
    public void getClientRequest() {
        ClientRequest clientRequest = new ClientRequest(333, 555, "user555", 20, "BACKSIDE");
        Game game = new Game(clientRequest);
        assertEquals(clientRequest, game.getClientRequest());
    }

    @Test
    public void setClientRequest() {
        ClientRequest clientRequest1 = new ClientRequest(333, 555, "user555", 20, "BACKSIDE");
        ClientRequest clientRequest2 = new ClientRequest(555, 777, "user777", 22, "BACKSIDE");
        Game game = new Game(clientRequest1);
        game.setClientRequest(clientRequest2);
        assertEquals(clientRequest2, game.getClientRequest());
    }
}