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


    // сокет
    Socket socket;
    // поток данных от сервера
    InputStream inputStream;
    // поток данных к серверу
    OutputStream outputStream;

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
     * @param addr            адрес подключения
     * @param gamer           ссылка на игрока
     * @param gamers          ссылка на базу (список) игроков
     * @param requestInterval интервал между запросами в миллисекундах
     * @param requestCount    количество запросов к серверу одного игрока
     */
    public Client(InetAddress addr, Gamer gamer, ConcurrentHashMap<String, Gamer> gamers, int requestInterval, int requestCount) {

        this.addr = addr;
        this.gamer = gamer;
        this.gamers = gamers;
        this.requestCount = requestCount;
        this.requestInterval = requestInterval;

        if (socketStart()) {
            //setDaemon(true);
            setPriority(NORM_PRIORITY);
            start();
        }


    }


    //
    private boolean socketStart() {

        boolean okFlag = true;

        long startTime = System.currentTimeMillis();

        try {
            this.socket = new Socket(addr, ServerMainRun.PORT);
        } catch (IOException e) {
            okFlag = false;
            long endTime = System.currentTimeMillis();
            // засчитываем попытку неудачной и добавляем её время
            gamer.changeBadRequestCount(1);
            gamer.changeAllRequestTime(endTime - startTime);
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();

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
            gamer.changeBadRequestCount(1);
            gamer.changeAllRequestTime(endTime - startTime);

            try {
                this.socket.close();
            } catch (IOException e2) {
                e.printStackTrace();
            }
        }

        return okFlag;
    }


    /**
     * Алгоритм работы нити игрока
     */
    public void run() {
                 // кол-во одновременно играющих
                threadcount++;

                //
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

                    socketStart();

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

                    // ждём ответ от сервера на свой запрос 10 секунд
                    try {
                        socket.setSoTimeout(10_000);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }

                    int realBytesCount = 0;

                    if (badFlag == 0) {
                        try {
                            realBytesCount = this.inputStream.read(bufResive);
                        } catch (IOException e) {
                            // не получили ответ от сервера за 10 секунд - считаем неудачной попытку
                            long endTime = System.currentTimeMillis();
                            gamer.changeBadRequestCount(1);
                            gamer.changeAllRequestTime(endTime - startTime);
                            System.out.println(Thread.currentThread().getName() + " : cannot read server response. Bad requests + 1");
                        } /*catch (IOException e) {
                            // не смогли прочитать ответ
                            long endTime = System.currentTimeMillis();
                            gamer.changeBadRequestCount(1);
                            gamer.changeAllRequestTime(endTime - startTime);
                            System.out.println(Thread.currentThread().getName() + " : cannot read server response. Bad requests + 1") ;

                        }*/
                    }

                    long endTime = System.currentTimeMillis();

                    long diffTime = endTime - startTime;

                    if (realBytesCount > 0) {
                        // создаём строку, содержащую полученную от сервера информацию
                        String gotString = new String(bufResive, 0, realBytesCount);
                        ServerResponse serverResponse = new ServerResponse();
                        serverResponse.fromJson(gotString);
                        System.out.println(Thread.currentThread().getName() + " : server response : ");
                        System.out.println(gotString);
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
                    if (diffTime < requestInterval) {
                        try {
                            sleep(requestInterval - diffTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
              try {
                  this.outputStream.close();
                  this.inputStream.close();
                  socket.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
            // уменьшаем кол-во играющих
            System.out.println(Thread.currentThread().getName() + " : thread finished");
            threadcount--;
            /// ??? ClientMainRun.threads.remove(gamer.getUserId());


    }
}

