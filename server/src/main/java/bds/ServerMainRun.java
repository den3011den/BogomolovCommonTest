package bds;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Стартовый класс для игрового сервера. Работает в бесконечном цикле.
 * Заканчивает работу только при снятии процесса в системе
 */
public class ServerMainRun {

//    static AtomicInteger testScore = new AtomicInteger(0);

    // максимальный номер записи в истории событий аккаунтов
    static AtomicInteger historyId = new AtomicInteger(0);

    // таблица аккаунтов игроков. Если игрок обращается к серверу впервые - создается аккаунт и дается 100 игровых денежных единиц
    static ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    // таблица событий аккаунтов. Записываем только события создания аккаунта при первом обращении игрока к серверу
    // и события, когда сервер допустил запрос игрока до игрового раунда (gameRound())
    // события, когда пришла отрицательная ставка или ставка больше остатка на счёте не приводят к игровому раунду и
    // в историю не пишутся
    static ConcurrentHashMap<String, AccountHistoryQuantum> accountsHistory = new ConcurrentHashMap<>();

    // адрес сервера (localhost)
    static InetAddress inetAddress;

    static {
        try {
            inetAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // порт сервера
    static final int PORT = 3158;

    /**
     * Запуск игрового сервера
     * @param args параметры командной строки
     */
    public static void main(String args[])
    {
        try
        {
            int i = 1; // счётчик подключений

            // локалхост
            ServerSocket server = new ServerSocket(PORT, 0, inetAddress);

            System.out.println(Thread.currentThread().getName() + " : server has been started");

            // слушаем порт
            while(true)
            {
                System.out.println(Thread.currentThread().getName() + " : waiting for client connection ...");
                // ждём нового подключения (реквеста), после чего запускаем обработку
                // запроса клиента в отдельном потоке и увеличиваем счётчик на единицу
                new GameServer(server.accept(), i, historyId, accounts, accountsHistory);
                i++;

            }
        }
        catch(Exception e)
        { e.printStackTrace(); } // вывод исключений
    }

}
