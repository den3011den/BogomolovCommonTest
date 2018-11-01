package bds;

import bds.communication.ClientRequest;
import bds.communication.ServerResponse;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Клиент сервера. Содержит одного игрока в одном потоке со всеми его попытками
 */
public class Client extends Thread {


    // сокет клиента
    Socket socket;
    // поток данных от сервера
    InputStream inputStream;
    // поток данных к серверу
    OutputStream outputStream;

    // вермя ожидания ответа от сервера в миллисекундах
    // после этого клиент считает, что неудачное обращение к серверу
    // и перестаёт ждать и совершает новую попытку. Даже если сервер
    // пришлёт ответ через 2 секунды с выигрышем
    private static final int WAITING_TIME_IN_MILLISECONDS = 1_000;

    // общее количество потоков - одновременно играющих игроков- клиентов
    private static int threadcount = 0;
    // адрес
    InetAddress addr;

    // id игрока
    int userId;

    // ссылка на игрока
    Gamer gamer;
    // ссылка на базу игроков
    ConcurrentHashMap<String, Gamer> gamers;

    // интервал между запросами игрока в миллисекундах
    int requestInterval;
    // количество запросов к серверу одного игрока
    int requestCount;

    /**
     * Получить количество одновременно играющих игроков-потоков
     *
     * @return количество одновременно играющих игроков-потоков
     */
    public static int threadCount() {
        return threadcount;
    }


    /**
     * Параметризованный конструктор
     *
     * @param addr            адрес подключения
     * @param userId          код игрока
     * @param gamers          ссылка на базу (список) игроков
     * @param requestInterval интервал между запросами игрока в миллисекундах
     * @param requestCount    количество запросов к серверу одного игрока
     */
    public Client(InetAddress addr, int userId, ConcurrentHashMap<String, Gamer> gamers, int requestInterval, int requestCount) {

        this.addr = addr;
        this.userId = userId;
        this.gamers = gamers;
        this.requestCount = requestCount;
        this.requestInterval = requestInterval;

        if (socketStart(1)) {
            //setDaemon(true);
            setPriority(NORM_PRIORITY);
            start();
        }

    }


    /**
     * Подключение к серверу и получение потоков входящего и исходящего
     *
     * @return true - поключение к серверу прошло успешно, false - поключение к серверу провалилось
     */
    private boolean socketStart(int mode) {

        boolean okFlag = true;

        long startTime = System.currentTimeMillis();

        try {
            this.socket = new Socket(addr, ServerMainRun.PORT);
        } catch (IOException e) {
            okFlag = false;
            long endTime = System.currentTimeMillis();
            // засчитываем попытку неудачной и добавляем её время
            if (mode == 2) {
                gamer.changeBadRequestCount(1);
                gamer.changeAllRequestTime(endTime - startTime);
            }
            System.out.println(Thread.currentThread().getName() + " : cannot create socket");
            e.printStackTrace();
        }

        if (okFlag) { // сокет удалось получить
            try {

                // поток данных от сервера
                this.inputStream = this.socket.getInputStream();

                // поток данных к серверу
                this.outputStream = this.socket.getOutputStream();

                // запуск потока

            } catch (IOException e) {
                okFlag = false;
                long endTime = System.currentTimeMillis();
                // засчитываем попытку неудачной
                if (mode == 2) {
                    gamer.changeBadRequestCount(1);
                    gamer.changeAllRequestTime(endTime - startTime);
                }
                System.out.println(Thread.currentThread().getName() + " : cannot create streams");

                try {
                    this.socket.close();
                } catch (IOException e2) {
                    e.printStackTrace();
                }
            }
        }

        return okFlag;
    }


    /**
     * Алгоритм работы нити игрока
     */
    public void run() {

        gamer = gamers.get(String.valueOf(userId));
        // кол-во одновременно играющих
        threadcount++;

        // цикл по количеству запросов, которые должен сделать игрок
        for (int i = 0, requestNumber = 0; i < requestCount; i++, requestNumber++) {

            int badFlag = 0;

            requestNumber++;

            ClientRequest clientRequest = new ClientRequest(requestNumber, gamer.getUserId(), gamer.getUserName(),
                    ClientMainRun.BET, "FRONTSIDE");

            String stringToSend = clientRequest.toJson();

            System.out.println(Thread.currentThread().getName() + " : sending client request: " + stringToSend);

            // буффер данных в 64 килобайта
            byte bufResive[] = new byte[64 * 1024];

            byte bufSend[] = stringToSend.getBytes();

            long startTime = System.currentTimeMillis();

            if (socketStart(2)) {

                //System.out.println(outputStream.toString());

                try {
                    this.outputStream.write(bufSend);
                    outputStream.flush();
                } catch (IOException e) {
                    // не смогли послать запрос
                    badFlag = 1;
                    long endTime = System.currentTimeMillis();
                    gamer.changeBadRequestCount(1);
                    gamer.changeAllRequestTime(endTime - startTime);
                    System.out.println(Thread.currentThread().getName() + " : cannot send request to server. Bad requests + 1");

                }

                // ждём ответ от сервера на свой запрос 1 секунду
                try {
                    socket.setSoTimeout(WAITING_TIME_IN_MILLISECONDS);
                } catch (SocketException e) {
                    e.printStackTrace();
                }

                int realBytesCount = 0;

                if (badFlag == 0) {
                    try {
                        realBytesCount = this.inputStream.read(bufResive);
                    } catch (IOException e) {
                        // не получили ответ от сервера за 1 секунду - считаем неудачной попытку
                        badFlag = 1;
                        long endTime = System.currentTimeMillis();
                        gamer.changeBadRequestCount(1);
                        gamer.changeAllRequestTime(endTime - startTime);
                        System.out.println(Thread.currentThread().getName() + " : cannot read server response. Bad requests + 1");
                    }
                }

                long endTime = System.currentTimeMillis();

                long diffTime = endTime - startTime;

                if (badFlag == 0) {
                    if (realBytesCount > 0) {
                        // создаём строку, содержащую полученную от сервера информацию
                        String gotString = new String(bufResive, 0, realBytesCount);
                        ServerResponse serverResponse = new ServerResponse();
                        serverResponse.fromJson(gotString);
                        System.out.println(Thread.currentThread().getName() + " : server response : ");
                        System.out.println(gotString);
                        try {
                            // шлём подтверждение о получении информации
                            this.outputStream.write("GOT".getBytes());
                            outputStream.flush();

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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    // в интрервале между запросами грубо пытаемся учесть время самого запроса
                    if (diffTime < requestInterval) {
                        try {
                            sleep(requestInterval - diffTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        this.outputStream.close();
                        this.inputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                try {
                    sleep(requestInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        // уменьшаем кол-во играющих
        System.out.println(Thread.currentThread().getName() + " : thread finished");
        threadcount--;
        /// ??? ClientMainRun.threads.remove(gamer.getUserId());
    }
}


