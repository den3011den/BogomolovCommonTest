package bds;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Scanner;

/**
 * Создание игроков и клиентов для сервера (запуск теста)
 */
public class ClientMainRun {

    // номер игрока
    static AtomicInteger userId = new AtomicInteger(0);

    // таблица игроков
    static ConcurrentHashMap<String, Gamer> gamers = new ConcurrentHashMap<>();

    // считаем, что ставят всегда по 10 игровых денежных единиц
    static final int BET = 10;

    // общее время теста ~
    static final long TEST_TIME = 120_000;

    // main
    public static void main(String[] args) throws InterruptedException {

        // адрес подключения
        InetAddress inetAddress = null;

        try {
            inetAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // количество одновременно играющих пользователей
        int gamerCount = 0;
        // интервал между обращениями к серверной стороне одного пользователя
        int requestInterval = 0;
        // количество обращений к игровому серверу одного пользователя
        int requestCount = 0;

        // если входные заданы при запуске в качестве параметров коммандной строки
        if (args.length>=3) {
            gamerCount = Integer.valueOf(args[0]);
            requestInterval = Integer.valueOf(args[1]);
            requestCount = Integer.valueOf(args[2]);
        }
        else { // иначе вводим руками
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

        long starTestTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - starTestTime) < TEST_TIME) {

            if (Client.threadCount() < gamerCount) { // количество игроков позволяет запустить ещё одного

                int localUserId = userId.addAndGet(1);

                Gamer gamer = new Gamer();
                gamer.setUserId(localUserId);
                gamer.setUserName("User " + localUserId);
                gamer.changeScore(100);
                gamer.changeBadRequestCount(0);
                gamer.changeGoodRequestCount(0);
                gamer.changeAllRequestTime(0);

                // ложим игрока в базу геймеров
                gamers.put(String.valueOf(gamer.getUserId()) ,gamer);

                // делаем клиента для сервера для этого игрока
                new Client(inetAddress, gamer, gamers, requestInterval, requestCount);

            } else {
                // если превышено количество играющих - ждём 100 ms
                Thread.currentThread().sleep(100);
            }
        }

        System.out.println(String.format("%95s","-"));
        System.out.println(String.format("|%20s|%20s|%20s|%30s|","Пользователь", "Успешные запросы", "Неуспешные запросы", "Среднее время запроса"));
        System.out.println(String.format("%95s","-"));

        // таблица результатов
        long i = 0;
        Gamer gamer;
        for (String key : gamers.keySet()) {
            gamer = gamers.get(key);
            System.out.println(String.format("|%20s|%20s|%20s|%30s|",
                    gamer.getUserName(), gamer.getGoodRequestCount(), gamer.getBadRequestCount(), gamer.getAverageRequestTime()));
            System.out.println(String.format("%95s","-"));
        }

    }


}