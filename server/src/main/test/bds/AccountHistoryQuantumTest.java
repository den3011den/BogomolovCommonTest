package bds;

import org.junit.Test;

import static org.junit.Assert.*;

public class AccountHistoryQuantumTest {

    private final int historyId = 1001;
    private final String coinSide = "BACKSIDE";
    private final long time = 11111122222L;
    private final int userId = 555;
    private final String userName = "user555";
    private final float score = 100;
    private final float difference = 20.0F;
    private final Account account = new Account(userId, userName, score);

    @Test
    public void getDifference() {
        AccountHistoryQuantum accountHistoryQuantum = new  AccountHistoryQuantum(historyId, difference, time, coinSide, account);
        assertEquals(difference, accountHistoryQuantum.getDifference(), 0.001);
    }

    @Test
    public void setDifference() {
        AccountHistoryQuantum accountHistoryQuantum = new  AccountHistoryQuantum(historyId, 8989.90F, time, coinSide, account);
        accountHistoryQuantum.setDifference(difference);
        assertEquals(difference, accountHistoryQuantum.getDifference(), 0.001);
    }

    @Test
    public void getTime() {
        AccountHistoryQuantum accountHistoryQuantum = new  AccountHistoryQuantum(historyId, difference, time, coinSide, account);
        assertEquals(time, accountHistoryQuantum.getTime());
    }

    @Test
    public void setTime() {
        AccountHistoryQuantum accountHistoryQuantum = new  AccountHistoryQuantum(historyId, difference, 99999L, coinSide, account);
        accountHistoryQuantum.setTime(time);
        assertEquals(time, accountHistoryQuantum.getTime());
    }

    @Test
    public void getHistoryId() {
        AccountHistoryQuantum accountHistoryQuantum = new  AccountHistoryQuantum(historyId, difference, time, coinSide, account);
        assertEquals(historyId, accountHistoryQuantum.getHistoryId());
    }

    @Test
    public void setHistoryId() {
        AccountHistoryQuantum accountHistoryQuantum = new  AccountHistoryQuantum(8989, difference, time, coinSide, account);
        accountHistoryQuantum.setHistoryId(historyId);
        assertEquals(historyId, accountHistoryQuantum.getHistoryId());
    }

    @Test
    public void getCoinSide() {
        AccountHistoryQuantum accountHistoryQuantum = new  AccountHistoryQuantum(historyId, difference, time, coinSide, account);
        assertEquals(coinSide, accountHistoryQuantum.getCoinSide());
    }

    @Test
    public void setCoinSide() {
        AccountHistoryQuantum accountHistoryQuantum = new  AccountHistoryQuantum(historyId, difference, time, "FRONTSIDE", account);
        accountHistoryQuantum.setCoinSide(coinSide);
        assertEquals(coinSide, accountHistoryQuantum.getCoinSide());
    }
}