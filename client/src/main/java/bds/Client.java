package bds;

import bds.communication.ClientRequest;
import bds.communication.ServerResponse;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Client extends Thread{

    private Socket socket;
    // поток данных от сервера
    private InputStream inputStream;
    // поток данных к серверу
    private OutputStream outputStream;
    private static int threadcount = 0;
    InetAddress addr;
    Gamer gamer;
    ConcurrentHashMap<String, Gamer> gamers;

    int requestInterval;
    int requestCount;

    public static int threadCount() {
        return threadcount;
    }

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

    public void run() {
        try {

            threadcount++;

            for (int i = 0, requestNumber = 0; i < requestCount; i++, requestNumber++) {

                requestNumber++;

                ClientRequest clientRequest = new ClientRequest(requestNumber, gamer.getUserId(), gamer.getUserName(),
                    ClientMainRun.BET, "FRONTSIDE");

                String stringToSend = clientRequest.toJson();

                // буффер данных в 64 килобайта
                byte bufResive[] = new byte[64*1024];

                byte bufSend[] = stringToSend.getBytes();

                long startTime = System.currentTimeMillis();

                outputStream.write(bufSend);

                socket.setSoTimeout(5000);

                int realBytesCount = 0;

                try {
                    realBytesCount = inputStream.read(bufResive);
                }
                catch (SocketTimeoutException e) {
                    long endTime = System.currentTimeMillis();
                    gamer.changeBadRequestCount(1);
                    gamer.changeAllRequestTime(endTime - startTime);
                }

                long endTime = System.currentTimeMillis();

                long diffTime = endTime - startTime;

                if (realBytesCount > 0) {
                    // создаём строку, содержащую полученную от клиента информацию
                    String gotString = new String(bufResive, 0, realBytesCount);
                    ServerResponse serverResponse = new ServerResponse();
                    serverResponse.fromJson(gotString);
                    if (serverResponse.getStatus() == 1) {
                        gamer.changeGoodRequestCount(1);
                        gamer.changeScore(serverResponse.getWin());
                        gamer.changeAllRequestTime(diffTime);
                    }
                    if (serverResponse.getStatus() == -1) {
                        gamer.changeGoodRequestCount(1);
                        gamer.changeAllRequestTime(diffTime);
                    }

                }

                if (diffTime < requestInterval)
                    sleep(requestInterval);

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
            threadcount--; // Завершаем нить
        }
    }

}
