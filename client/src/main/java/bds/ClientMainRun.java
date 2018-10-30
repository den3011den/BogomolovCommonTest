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

    // активные потоки
    static ConcurrentHashMap<String, Thread> threads = new ConcurrentHashMap<>();

    // считаем, что ставят всегда по 10 игровых денежных единиц
    static final int BET = 10;

    // общее время теста ~
    static final long TEST_TIME = 5_000;

    // main
    public static void main(String[] args) throws InterruptedException {

        System.out.println(Thread.currentThread().getName() + " : starting client test ");

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

                System.out.println(Thread.currentThread().getName() + " : created gamer. userId =  " + gamer.getUserId());

                // делаем клиента для сервера для этого игрока
                threads.put(String.valueOf(gamer.getUserId()), new Client(inetAddress, gamer, gamers, requestInterval, requestCount));


            } else {
                System.out.println(Thread.currentThread().getName() + " : pause 100 ms ");
                // если превышено количество играющих - ждём 100 ms
                Thread.currentThread().sleep(100);
            }
        }

        System.out.println(Thread.currentThread().getName() + " : waiting for finishing all gamers threads");

        // ждём завершения всех потоков
        for (String key : threads.keySet()) {
            threads.get(key).join();
        }

        System.out.println(Thread.currentThread().getName() + " : all gamers threads has been finished");

        String lineString = String.format("%95s"," ").replace(' ','-');

        System.out.println(lineString);
        System.out.println(String.format("|%20s|%20s|%20s|%30s|","Пользователь", "Успешные запросы", "Неуспешные запросы", "Среднее время запроса"));
        System.out.println(lineString);

        // таблица результатов
        long i = 0;
        Gamer gamer;
        for (String key : gamers.keySet()) {
            gamer = gamers.get(key);
            System.out.println(String.format("|%20s|%20s|%20s|%30s|",
                    gamer.getUserName(), gamer.getGoodRequestCount(), gamer.getBadRequestCount(), gamer.getAverageRequestTime()));
            System.out.println(lineString);
        }

        System.out.println(Thread.currentThread().getName() + " : client test finished");

    }


}
