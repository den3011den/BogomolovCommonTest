package bds;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Scanner;

public class ClientMainRun {

    // текущий номер записи в истории событий аккаунтов
    static AtomicInteger userId = new AtomicInteger(0);

    // таблица игроков
    static ConcurrentHashMap<String, Gamer> gamers = new ConcurrentHashMap<>();

    static final int BET = 10;

    public static void main(String[] args) throws InterruptedException {

        InetAddress inetAddress = null;

        try {
            inetAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        int gamerCount = 0;
        int requestInterval = 0;
        int requestCount = 0;

        if (args.length>=3) {
            gamerCount = Integer.valueOf(args[0]);
            requestInterval = Integer.valueOf(args[1]);
            requestCount = Integer.valueOf(args[2]);
        }
        else {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter gamerCount (int): ");
            gamerCount = in.nextInt();
            System.out.println("Enter requestInterval (int) in millisec: ");
            requestInterval = in.nextInt();
            System.out.println("Enter requestCount (int): ");
            requestCount = in.nextInt();
        }

        System.out.println("Input data");
        System.out.println("----------");

        System.out.println("gamerCount = " + gamerCount);
        System.out.println("requestInterval = " + requestInterval + "(millisec)");
        System.out.println("requestCount = " + requestCount);

        System.out.println("Start test");

        while (true) {

            if (Client.threadCount() < gamerCount) {

                int localUserId = userId.addAndGet(1);

                Gamer gamer = new Gamer();
                gamer.setUserId(localUserId);
                gamer.setUserName("User " + localUserId);
                gamer.changeScore(100);
                gamer.changeBadRequestCount(0);
                gamer.changeGoodRequestCount(0);
                gamer.changeAllRequestTime(0);

                gamers.put(String.valueOf(gamer.getUserId()) ,gamer);

                new Client(inetAddress, gamer, gamers, requestInterval, requestCount);

            } else
                Thread.currentThread().sleep(100);
        }
    }


}
