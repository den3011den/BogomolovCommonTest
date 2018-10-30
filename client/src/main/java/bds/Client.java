package bds;

import bds.communication.ClientRequest;
import bds.communication.ServerResponse;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Клиент сервера. Содержит одного игрока в одном потоке со всеми его попытками
 */
public class Client extends Thread{

    // cjrtn
    private Socket socket;
    // поток данных от сервера
    private InputStream inputStream;
    // поток данных к серверу
    private OutputStream outputStream;
    // общее количество потоков - одновременно играющих игроков- клиентов
    private static int threadcount = 0;
    // адрес
    InetAddress addr;
    // ссылка на игрока
    Gamer gamer;
    // ссылка на базу игроков
    ConcurrentHashMap<String, Gamer> gamers;

    // интервал запросов в миллисекундах
    int requestInterval;
    // количество запросов к серверу одного игрока
    int requestCount;

    /**
     * @return количество одновременно играющих игроков-потоков
     */
    public static int threadCount() {
        return threadcount;
    }


    /**
     * @param addr адрес подключения
     * @param gamer ссылка на игрока
     * @param gamers ссылка на базу (список) игроков
     * @param requestInterval интервал между запросами в миллисекундах
     * @param requestCount количество запросов к серверу одного игрока
     */
    public Client(InetAddress addr, Gamer gamer, ConcurrentHashMap<String, Gamer> gamers, int requestInterval, int requestCount) {

        this.addr = addr;
        this.gamer = gamer;
        this.gamers = gamers;
        this.requestCount = requestCount;
        this.requestInterval = requestInterval;

        long startTime = System.currentTimeMillis();

        try {
            socket = new Socket(addr, ServerMainRun.PORT);
        }
        catch (IOException e) {
            long endTime = System.currentTimeMillis();
            // засчитываем попытку неудачной и добавляем её время
            gamer.changeBadRequestCount(1);
            gamer.changeAllRequestTime(endTime - startTime);
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();

        try {

            // поток данных от сервера
            inputStream = socket.getInputStream();

            // поток данных к серверу
            outputStream = socket.getOutputStream();

            start();
        }
        catch (IOException e) {
            long endTime = System.currentTimeMillis();
            // засчитываем попытку неудачной
            gamer.changeBadRequestCount(1);
            gamer.changeAllRequestTime(endTime - startTime);

            try {
                socket.close();
            }
            catch (IOException e2) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Алгоритм работы нити игрока
     */
    public void run() {
        try {

            // кол-во одновременно играющих
            threadcount++;

            //
            for (int i = 0, requestNumber = 0; i < requestCount; i++, requestNumber++) {

                requestNumber++;

                ClientRequest clientRequest = new ClientRequest(requestNumber, gamer.getUserId(), gamer.getUserName(),
                    ClientMainRun.BET, "FRONTSIDE");

                String stringToSend = clientRequest.toJson();

                System.out.println(Thread.currentThread().getName() + " : sending client request: " + stringToSend);

                // буффер данных в 64 килобайта
                byte bufResive[] = new byte[64*1024];

                byte bufSend[] = stringToSend.getBytes();

                long startTime = System.currentTimeMillis();

                outputStream.write(bufSend);

                // ждём ответ от сервера на свой запрос 10 секунд
                socket.setSoTimeout(10_000);

                int realBytesCount = 0;

                try {
                    realBytesCount = inputStream.read(bufResive);
                }
                catch (SocketTimeoutException e) {
                    // не получили ответ от сервера за 10 секунд - считаем неудачной попытку
                    long endTime = System.currentTimeMillis();
                    gamer.changeBadRequestCount(1);
                    gamer.changeAllRequestTime(endTime - startTime);
                    System.out.println(Thread.currentThread().getName() + " : server response timeout : 10s");
                }

                long endTime = System.currentTimeMillis();

                long diffTime = endTime - startTime;

                if (realBytesCount > 0) {
                    // создаём строку, содержащую полученную от клиента информацию
                    String gotString = new String(bufResive, 0, realBytesCount);
                    ServerResponse serverResponse = new ServerResponse();
                    serverResponse.fromJson(gotString);
                    System.out.println(Thread.currentThread().getName() + " : " + gotString);
                    if (serverResponse.getStatus() == 1) {
                        // раунд игры состоялся, считаем попытку удачной
                        gamer.changeGoodRequestCount(1);
                        gamer.changeScore(serverResponse.getWin());
                        gamer.changeAllRequestTime(diffTime);
                    }
                    if (serverResponse.getStatus() == -1) {
                        // раунд игры Не состоялся, но ответ от сервера получен
                        // по условию задания считаем попытку удачной
                        gamer.changeGoodRequestCount(1);
                        gamer.changeAllRequestTime(diffTime);
                    }

                }

                // в интрервале между запросами грубо пытаемся учесть время самого запроса
                if (diffTime < requestInterval)
                    sleep(requestInterval - diffTime);

            }
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            // Всегда закрывает:
            try {
                socket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            // уменьшаем кол-во играющих
            System.out.println(Thread.currentThread().getName() + " : thread finished");
            threadcount--;
            /// ??? ClientMainRun.threads.remove(gamer.getUserId());
        }
    }

}
