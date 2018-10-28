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

/**
 * Игровой многопоточный сервер
 */
public class GameServer extends Thread {

    // сокет
    Socket socket;
    // номер нити
    int threadNumber = 0;
    // текущий номер записи в истории событий аккаунтов
    AtomicInteger historyId = new AtomicInteger(0);

    // таблица аккаунтов игроков. Если игрок обращается к серверу впервые - создаём аккаунт и даёт 100 игровых денежных единиц
    ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    // таблица событий аккаунтов. Записываем только события создания аккаунта при первом обращении игрока к серверу
    // и события, когда сервер допустил запрос игрока до игрового раунда (gameRound())
    // события, когда пришла отрицательная ставка или ставка больше остатка на счёте не приводят к игровому раунду и
    // в историю не пишутся
    ConcurrentHashMap<String, AccountHistoryQuantum> accountsHistory = new ConcurrentHashMap<>();

    // запуск игрового сервера
    public static void main(String args[])
    {
        try
        {
            int i = 1; // счётчик подключений

            // локалхост, порт 3001
            ServerSocket server = new ServerSocket(3001, 0, InetAddress.getByName("localhost"));

            System.out.println("server has been started");

            // слушаем порт
            while(true)
            {
                // ждём нового подключения (реквеста), после чего запускаем обработку
                // запроса клиента в отдельном потоке и увеличиваем счётчик на единицу
                new GameServer(server.accept(), i);
                i++;
            }
        }
        catch(Exception e)
            { e.printStackTrace(); } // вывод исключений
    }


    /**
     * @param socket сокет
     * @param threadNumber номер потока
     */
    public GameServer(Socket socket, int threadNumber)
    {
        this.threadNumber = threadNumber;
        this.socket = socket;

        // запуск потока
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    /**
     * реализация алгоритма работы отдельного потока
     */
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

            // нормально распознан id клиента
            if (clientRequest.getUserId() > 0) {

                // попытка прочитать данные счёта из базы счетов
                Account account = accounts.get(String.valueOf(clientRequest.getUserId()));

                // не найден счёт. Создаём и кладём на него 100 игровых денежных единиц
                if (account == null) {
                    Account newAccount = new Account(clientRequest.getUserId(), clientRequest.getUserName(),100); // создание счёта
                    accounts.put(String.valueOf(newAccount.getUserId()), newAccount); // запись в базу счетов
                    historyId.addAndGet(1); // инкремент счётчика базы истории
                    AccountHistoryQuantum accountHistoryQuantum = new AccountHistoryQuantum(historyId.get(),0.0F, System.currentTimeMillis(), clientRequest.getCoinSide(), account); // создаём событие истории
                    accountsHistory.put(String.valueOf(accountHistoryQuantum.getHistoryId()), accountHistoryQuantum); // кладём событие истории в базу истории
                    account = newAccount; // запоминаем ссылку на данные аккаунта
                }

                // берём ставку из клиентского запроса
                float bet = clientRequest.getBet();

                // проверяем достаточно ли средств на счёте игрока для заявленной ставки
                if (account.getScore() >= bet) {
                    // ставка не корректна. Формируем ответ об ошибке для клиента
                    if (bet <= 0) {
                        serverResponse.setClientRequest(clientRequest);
                        serverResponse.setStatus(-1);
                        serverResponse.setMessage("Your bet is " + bet + ". It is negative bet or zero bet.");
                        serverResponse.setWin(0.0F);

                    }
                    else { // ставка корректна и денег хватает
                        Game game = new Game(clientRequest);

                        // запускаем раунд игры
                        float win = game.gameRound();

                        // формируем ответ клиенту о результатах проведённого раунда игры
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
                    // ответ о нехватке средств для ставки
                    serverResponse.setClientRequest(clientRequest);
                    serverResponse.setStatus(-1);
                    serverResponse.setMessage("Your bet is " + clientRequest.getBet() + ". Your account score is " + account.score + ". You dont have enough money for this bet.");
                    serverResponse.setWin(0.0F);
                }


             }
            else {
                // плохой запрос
                serverResponse.setClientRequest(clientRequest);
                serverResponse.setStatus(-1);
                serverResponse.setMessage("Bad userId. Cant understand this request");
                serverResponse.setWin(0.0F);

            }

            String responseString = serverResponse.toJson();

            // шлём ответ клиенту
            outputStream.write(responseString.getBytes());

            // завершаем соединение
            socket.close();
        }
        catch(Exception e)
            {e.printStackTrace();} // вывод исключений
    }
}
