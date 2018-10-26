package bds.communication;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClientRequestTest {

    @Test
    public void getRequestNumber() {
        ClientRequest testClientRequest = new ClientRequest();
        testClientRequest.setRequestNumber(12);
        assertEquals(12, testClientRequest.getRequestNumber());
    }

    @Test
    public void setRequestNumber() {
        ClientRequest testClientRequest = new ClientRequest();
        testClientRequest.setRequestNumber(12);
        assertEquals(12, testClientRequest.getRequestNumber());

    }

    @Test
    public void getUserId() {
        ClientRequest testClientRequest = new ClientRequest();
        testClientRequest.setUserId(111);
        assertEquals(111, testClientRequest.getUserId());

    }

    @Test
    public void setUserId() {
        ClientRequest testClientRequest = new ClientRequest();
        testClientRequest.setUserId(111);
        assertEquals(111, testClientRequest.getUserId());
    }

    @Test
    public void getUserName() {
        ClientRequest testClientRequest = new ClientRequest();
        testClientRequest.setUserName("Will Smith");
        assertEquals("Will Smith", testClientRequest.getUserName());
    }

    @Test
    public void setUserName() {
        ClientRequest testClientRequest = new ClientRequest();
        testClientRequest.setUserName("Will Smith");
        assertEquals("Will Smith", testClientRequest.getUserName());
    }

    @Test
    public void getBet() {
        ClientRequest testClientRequest = new ClientRequest();
        testClientRequest.setBet(20);
        assertEquals(20, testClientRequest.getBet());
    }

    @Test
    public void setBet() {
        ClientRequest testClientRequest = new ClientRequest();
        testClientRequest.setBet(20);
        assertEquals(20, testClientRequest.getBet());
    }

    @Test
    public void getCoinSide() {
        ClientRequest testClientRequest = new ClientRequest();
        testClientRequest.setCoinSide("BACKSIDE");
        assertEquals("BACKSIDE", testClientRequest.getCoinSide());
    }

    @Test
    public void setCoinSide() {
        ClientRequest testClientRequest = new ClientRequest();
        testClientRequest.setCoinSide("BACKSIDE");
        assertEquals("BACKSIDE", testClientRequest.getCoinSide());
    }

    @Test
    public void toJson() {
        String expectedString = "{\"requestNumber\":12," +
                "\"userId\":111," +
                "\"userName\":\"Will Smith\"," +
                "\"bet\":20," +
                "\"coinSide\":\"BACKSIDE\"}";
        ClientRequest testClientRequest = new ClientRequest(12, 111, "Will Smith", 20, "BACKSIDE");
        assertEquals(expectedString, testClientRequest.toJson());
    }

    @Test
    public void fromJson() {
        int requestNumber = 12;
        int userId = 111;
        String userName = "Will Smith";
        int bet = 20;
        String coinSide = "BACKSIDE";
        ClientRequest testClientRequest = new ClientRequest(requestNumber, userId, userName, bet, coinSide);
        String jsonString = testClientRequest.toJson();

        assertTrue(testClientRequest.fromJson(jsonString));
        assertEquals(requestNumber, testClientRequest.getRequestNumber());
        assertEquals(userId, testClientRequest.getUserId());
        assertEquals(userName, testClientRequest.getUserName());
        assertEquals(bet, testClientRequest.getBet());
        assertEquals(coinSide, testClientRequest.getCoinSide());
    }
}