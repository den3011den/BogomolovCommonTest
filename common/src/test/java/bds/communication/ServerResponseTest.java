package bds.communication;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServerResponseTest {

    @Test
    public void getClientRequest() {
        ServerResponse testServerResponse = new ServerResponse();
        ClientRequest testClientRequest = new ClientRequest(12, 111, "Will Smith", 20, "BACKSIDE");
        testServerResponse.setClientRequest(testClientRequest);
        ClientRequest gotClientRequest = testServerResponse.getClientRequest();
        assertEquals(testClientRequest, gotClientRequest); // тот же объект вернулся, что и заносился - адреса совпадают
    }

    @Test
    public void setClientRequest() {
        ServerResponse testServerResponse = new ServerResponse();
        ClientRequest testClientRequest = new ClientRequest(12, 111, "Will Smith", 20, "BACKSIDE");
        testServerResponse.setClientRequest(testClientRequest);
        ClientRequest gotClientRequest = testServerResponse.getClientRequest();
        assertEquals(testClientRequest, gotClientRequest); // тот же объект вернулся, что и заносился - адреса совпадают
    }

    @Test
    public void getStatus() {
        ServerResponse testServerResponse = new ServerResponse();
        testServerResponse.setStatus(1);
        assertEquals(1, testServerResponse.getStatus());
    }

    @Test
    public void setStatus() {
        ServerResponse testServerResponse = new ServerResponse();
        testServerResponse.setStatus(1);
        assertEquals(1, testServerResponse.getStatus());
    }

    @Test
    public void getMessage() {
        ServerResponse testServerResponse = new ServerResponse();
        testServerResponse.setMessage("Everything gonna be alright");
        assertEquals("Everything gonna be alright", testServerResponse.getMessage());
    }

    @Test
    public void setMessage() {
        ServerResponse testServerResponse = new ServerResponse();
        testServerResponse.setMessage("Everything gonna be alright");
        assertEquals("Everything gonna be alright", testServerResponse.getMessage());
    }

    @Test
    public void getWin() {
        ServerResponse testServerResponse = new ServerResponse();
        testServerResponse.setWin(2.22F);
        assertEquals(2.22F, testServerResponse.getWin(), 0.0001);
    }

    @Test
    public void setWin() {
        ServerResponse testServerResponse = new ServerResponse();
        testServerResponse.setWin(2.22F);
        assertEquals(2.22F, testServerResponse.getWin(), 0.0001);
    }

    @Test
    public void toJson() {
        String expectedString = "{\"clientRequest\":{\"requestNumber\":12," +
                "\"userId\":111," +
                "\"userName\":\"Will Smith\"," +
                "\"bet\":20," +
                "\"coinSide\":\"BACKSIDE\"}," +
                "\"status\":1," +
                "\"message\":\"Everything gonna be alright\","+
                "\"win\":2.22}";

        ServerResponse testServerResponse = new ServerResponse();
        testServerResponse.setClientRequest(new ClientRequest(12, 111, "Will Smith", 20, "BACKSIDE"));
        testServerResponse.setStatus(1);
        testServerResponse.setMessage("Everything gonna be alright");
        testServerResponse.setWin(2.22F);
        assertEquals(expectedString, testServerResponse.toJson());
    }

    @Test
    public void fromJson() {
        int status = 1;
        String message = "Everything gonna be alright";
        float win = 2.22F;
        ClientRequest clientRequest = new ClientRequest(12, 111, "Will Smith", 20, "BACKSIDE");
        ServerResponse testServerResponse = new ServerResponse();
        testServerResponse.setClientRequest(clientRequest);
        testServerResponse.setStatus(status);
        testServerResponse.setMessage(message);
        testServerResponse.setWin(win);
        String jsonString = testServerResponse.toJson();

        assertTrue(testServerResponse.fromJson(jsonString));
        assertEquals(clientRequest, testServerResponse.getClientRequest());
        assertEquals(status, testServerResponse.getStatus());
        assertEquals(message, testServerResponse.getMessage());
        assertEquals(win, testServerResponse.getWin(), 0.0001);
    }
}