package bds;

import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    final int userId = 555;
    final String userName = "user555";
    final float score = 100;
    final float difference = 20.0F;

    @Test
    public void getUserId() {
        Account account = new Account(userId, userName, score);
        assertEquals(userId, account.getUserId());
    }

    @Test
    public void setUserId() {
        Account account = new Account(333, userName, score);
        account.setUserId(userId);
        assertEquals(userId, account.getUserId());
    }

    @Test
    public void getUserName() {
        Account account = new Account(userId, userName, score);
        assertEquals(userName, account.getUserName());
    }

    @Test
    public void setUserName() {
        Account account = new Account(333, "333", score);
        account.setUserName(userName);
        assertEquals(userName, account.getUserName());

    }

    @Test
    public void getScore() {
        Account account = new Account(333, "333", score);
        assertEquals(score, account.getScore(), 0.01);
    }

    @Test
    public void setScore() {
        Account account = new Account(333, "333", 333);
        account.setScore(score);
        assertEquals(score, account.getScore(), 0.01);

    }

    @Test
    public void changeScore() {
        Account account = new Account(userId, userName, score);
        account.changeScore(difference);
        assertEquals(score + difference, account.getScore(), 0.01);
    }
}