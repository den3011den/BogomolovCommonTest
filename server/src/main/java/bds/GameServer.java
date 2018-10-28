package bds;

import bds.communication.ClientRequest;
import bds.communication.ServerResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameServer extends Thread {

    Socket socket;
    int activeThreadCount = 0;
    AtomicInteger historyId = new AtomicInteger(0);

    ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, AccountHistoryQuantum> accountsHistory = new ConcurrentHashMap<>();

    public static void main(String args[])
    {
        try
        {
            int i = 1; // счётчик подключений

            // привинтить сокет на локалхост, порт 3001
            ServerSocket server = new ServerSocket(3001, 0, InetAddress.getByName("localhost"));

            System.out.println("server has been started");

            // слушаем порт
            while(true)
            {
                // ждём нового подключения, после чего запускаем обработку клиента
                // в новый вычислительный поток и увеличиваем счётчик на единичку
                new GameServer(server.accept(), i);
                i++;
            }
        }
        catch(Exception e)
        {System.out.println("init error: "+e);} // вывод исключений
    }


    public GameServer(Socket socket, int activeThreadCount)
    {
        this.activeThreadCount = activeThreadCount;
        this.socket = socket;

        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    public void run()
    {
        try
        {
            // поток данных от клиента
            InputStream inputStream = socket.getInputStream();
            // поток данных к клиенту
            OutputStream outputStream = socket.getOutputStream();

            // буффер данных в 64 килобайта
            byte buf[] = new byte[64*1024];
            // читаем 64кб от клиента, результат - кол-во реально принятых данных
            int realBytesCount = inputStream.read(buf);

            // создаём строку, содержащую полученную от клиента информацию
            String gotString = new String(buf, 0, realBytesCount);

            ClientRequest clientRequest = new ClientRequest();
            clientRequest.fromJson(gotString);

            ServerResponse serverResponse = new ServerResponse();

            if (clientRequest.getUserId() > 0) {

                Account account = accounts.get(String.valueOf(clientRequest.getUserId()));

                if (account == null) {
                    Account newAccount = new Account(clientRequest.getUserId(), clientRequest.getUserName(),100);
                    accounts.put(String.valueOf(newAccount.getUserId()), newAccount);
                    historyId.addAndGet(1);
                    AccountHistoryQuantum accountHistoryQuantum = new AccountHistoryQuantum(historyId.get(),0.0F, System.currentTimeMillis(), clientRequest.getCoinSide(), account);
                    accountsHistory.put(String.valueOf(accountHistoryQuantum.getHistoryId()), accountHistoryQuantum);
                    account = newAccount;
                }

                float bet = clientRequest.getBet();


                if (account.getScore() >= bet) {
                    if (bet <= 0) {
                        serverResponse.setClientRequest(clientRequest);
                        serverResponse.setStatus(-1);
                        serverResponse.setMessage("Your bet is " + bet + ". It is negative bet or zero bet.");
                        serverResponse.setWin(0.0F);

                    }
                    else {
                        Game game = new Game(clientRequest);
                        float win = game.gameRound();

                        serverResponse.setClientRequest(clientRequest);
                        serverResponse.setStatus(1);
                        String message;
                        if (win <= 0)
                            message = "You lost " + win + ". Lets try one more time!";
                        else
                            message = "You win " + win + "!. Try to increase your income!";
                        serverResponse.setMessage(message);
                        serverResponse.setWin(win);

                        account.changeScore(win);

                        accounts.put(String.valueOf(account.getUserId()), account);
                        historyId.addAndGet(1);
                        AccountHistoryQuantum accountHistoryQuantum = new AccountHistoryQuantum(historyId.get(), win, System.currentTimeMillis(), clientRequest.getCoinSide(), account);
                        accountsHistory.put(String.valueOf(accountHistoryQuantum.getHistoryId()), accountHistoryQuantum);

                    }

                }
                else {

                    serverResponse.setClientRequest(clientRequest);
                    serverResponse.setStatus(-1);
                    serverResponse.setMessage("Your bet is " + clientRequest.getBet() + ". Your account score is " + account.score + ". You dont have enough money for this bet.");
                    serverResponse.setWin(0.0F);
                }


             }
            else {

                serverResponse.setClientRequest(clientRequest);
                serverResponse.setStatus(-1);
                serverResponse.setMessage("Bad userId. Cant understand this request");
                serverResponse.setWin(0.0F);


            }

            String responseString = serverResponse.toJson();

            // выводим данные
            outputStream.write(responseString.getBytes());

            // завершаем соединение
            socket.close();
        }
        catch(Exception e)
            {e.printStackTrace();} // вывод исключений
    }
}
