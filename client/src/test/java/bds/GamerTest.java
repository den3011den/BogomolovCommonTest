package bds;

import org.junit.Test;

import static org.junit.Assert.*;

public class GamerTest {

    private int userId = 333;

    private String userName = "User 333";

    private float score = 120;

    private int goodRequestCount = 20;

    private int badRequestCount = 10;

    private long allRequestTime = 10L;

    //private long averageRequestTime = 3L;


    @Test
    public void getUserId() {
        Gamer gamer = new Gamer();
        gamer.setUserId(userId);
        assertEquals(userId, gamer.getUserId());
    }

    @Test
    public void setUserId() {
        Gamer gamer = new Gamer();
        gamer.setUserId(5545);
        gamer.setUserId(userId);
        assertEquals(userId, gamer.getUserId());
    }

    @Test
    public void getUserName() {
        Gamer gamer = new Gamer();
        gamer.setUserName(userName);
        assertEquals(userName, gamer.getUserName());
    }

    @Test
    public void setUserName() {
        Gamer gamer = new Gamer();
        gamer.setUserName("eeerrr");
        gamer.setUserName(userName);
        assertEquals(userName, gamer.getUserName());

    }

    @Test
    public void getScore() {
        Gamer gamer = new Gamer();
        gamer.changeScore(score);
        assertEquals(score, gamer.getScore(), 0.001);
    }

    @Test
    public void changeScore() {
        Gamer gamer = new Gamer();
        gamer.changeScore(score);
        gamer.changeScore(score);
        assertEquals(2*score, gamer.getScore(), 0.001);

    }

    @Test
    public void getGoodRequestCount() {
        Gamer gamer = new Gamer();
        gamer.changeGoodRequestCount(goodRequestCount);
        assertEquals(goodRequestCount, gamer.getGoodRequestCount());
    }

    @Test
    public void changeGoodRequestCount() {
        Gamer gamer = new Gamer();
        gamer.changeGoodRequestCount(goodRequestCount);
        gamer.changeGoodRequestCount(goodRequestCount);
        assertEquals(2*goodRequestCount, gamer.getGoodRequestCount());

    }

    @Test
    public void getBadRequestCount() {
        Gamer gamer = new Gamer();
        gamer.changeBadRequestCount(badRequestCount);
        assertEquals(badRequestCount, gamer.getBadRequestCount());
    }

    @Test
    public void changeBadRequestCount() {
        Gamer gamer = new Gamer();
        gamer.changeBadRequestCount(badRequestCount);
        gamer.changeBadRequestCount(badRequestCount);
        assertEquals(2*badRequestCount, gamer.getBadRequestCount());
    }

    @Test
    public void getAllRequestTime() {
        Gamer gamer = new Gamer();
        gamer.changeAllRequestTime(allRequestTime);
        assertEquals(allRequestTime, gamer.getAllRequestTime());
    }

    @Test
    public void changeAllRequestTime() {
        Gamer gamer = new Gamer();
        gamer.changeAllRequestTime(allRequestTime);
        gamer.changeAllRequestTime(allRequestTime);
        assertEquals(2*allRequestTime, gamer.getAllRequestTime());
    }

    @Test
    public void getAverageRequestTime() {
        Gamer gamer = new Gamer();
        gamer.changeBadRequestCount(badRequestCount);
        gamer.changeGoodRequestCount(goodRequestCount);
        gamer.changeAllRequestTime(allRequestTime);
        assertEquals((long)allRequestTime/(badRequestCount+goodRequestCount), gamer.getAverageRequestTime());
    }
}