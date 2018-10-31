package bds;

import bds.communication.ClientRequest;
import bds.communication.ServerResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * игровой сервер
 */
public class GameServer extends Thread {

    // номер нити
    int threadNumber = 0;

    // ссылка на таблицу аккаунтов игроков
    ConcurrentHashMap<String, Account> accounts;

    // ссылка на таблицу событий аккаунтов
    ConcurrentHashMap<String, AccountHistoryQuantum> accountsHistory;

    // ссылка на текущий номер записи в истории событий аккаунтов
    AtomicInteger historyId;

    // сокет
    Socket socket;

    /**
     * @param socket сокет
     * @param threadNumber номер потока
     */
    public GameServer(Socket socket, int threadNumber, AtomicInteger historyId, ConcurrentHashMap<String, Account> accounts,
                      ConcurrentHashMap<String, AccountHistoryQuantum> accountsHistory)
    {
        this.threadNumber = threadNumber;
        this.socket = socket;
        this.accounts = accounts;
        this.accountsHistory = accountsHistory;
        this.historyId = historyId;

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

            System.out.println(Thread.currentThread().getName() + " : Got client connection. Started thread. ");
            // поток данных от клиента
            InputStream inputStream = socket.getInputStream();

            // поток данных к клиенту
            OutputStream outputStream = socket.getOutputStream();

            // буффер данных в 64 килобайта
            byte buf[] = new byte[64*1024];
            // читаем 64кб от клиента, результат - кол-во реально принятых данных
            int realBytesCount = 0;
            try {
                realBytesCount = inputStream.read(buf);
            }
            catch (IOException e) {
                realBytesCount = 0;
            }

            if (realBytesCount > 0) {
                // создаём строку, содержащую полученную от клиента информацию
                String gotString = new String(buf, 0, realBytesCount);

                System.out.println(Thread.currentThread().getName() + " : Got client request : " + gotString);

                ClientRequest clientRequest = new ClientRequest();
                clientRequest.fromJson(gotString);

//                if (clientRequest.getUserName().equals("STOP")) {
//                    System.out.println("Stop");
//                }


                ServerResponse serverResponse = new ServerResponse();

                // нормально распознан id клиента
                if (clientRequest.getUserId() > 0) {

                    // попытка прочитать данные счёта из базы счетов
                    Account account = accounts.get(String.valueOf(clientRequest.getUserId()));

                    // не найден счёт. Создаём и кладём на него 100 игровых денежных единиц
                    if (account == null) {

                        Account newAccount = new Account(clientRequest.getUserId(), clientRequest.getUserName(), 100); // создание счёта
                        accounts.put(String.valueOf(newAccount.getUserId()), newAccount); // запись в базу счетов
                        historyId.addAndGet(1); // инкремент счётчика базы истории
                        AccountHistoryQuantum accountHistoryQuantum = new AccountHistoryQuantum(historyId.get(), 0.0F, System.currentTimeMillis(), clientRequest.getCoinSide(), newAccount); // создаём событие истории
                        accountsHistory.put(String.valueOf(accountHistoryQuantum.getHistoryId()), accountHistoryQuantum); // кладём событие истории в базу истории
                        account = newAccount; // запоминаем ссылку на данные аккаунта

                        System.out.println(Thread.currentThread().getName() + " : Created gamer account. userId : " + account.getUserId());
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
                            System.out.println(Thread.currentThread().getName() + " : UserId = " + clientRequest.getUserId() +
                                    ". Wrong bet = " + clientRequest.getBet());
                        } else { // ставка корректна и денег хватает
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

                            //??? accounts.put(String.valueOf(account.getUserId()), account);
                            historyId.addAndGet(1);
                            AccountHistoryQuantum accountHistoryQuantum = new AccountHistoryQuantum(historyId.get(), win, System.currentTimeMillis(), clientRequest.getCoinSide(), account);
                            accountsHistory.put(String.valueOf(accountHistoryQuantum.getHistoryId()), accountHistoryQuantum);

                        }

                    } else {
                        System.out.println(Thread.currentThread().getName() + " : UserId = " + clientRequest.getUserId() +
                                ". Not enought money for bet = " + clientRequest.getBet() + " (score = " + account.getScore() + ")");

                        // ответ о нехватке средств для ставки
                        serverResponse.setClientRequest(clientRequest);
                        serverResponse.setStatus(-1);
                        serverResponse.setMessage("Your bet is " + clientRequest.getBet() + ". Your account score is " + account.score + ". You dont have enough money for this bet.");
                        serverResponse.setWin(0.0F);
                    }


                } else {
                    // плохой запрос
                    serverResponse.setClientRequest(clientRequest);
                    serverResponse.setStatus(-1);
                    serverResponse.setMessage("Bad userId. Cant understand this request");
                    serverResponse.setWin(0.0F);

                }

                String responseString = serverResponse.toJson();

                System.out.println(Thread.currentThread().getName() + " : UserId = " + clientRequest.getUserId() +
                        ". Sending response string : " + responseString);

                // шлём ответ клиенту
                outputStream.write(responseString.getBytes());
            }
            // завершаем соединение
            outputStream.close();
            inputStream.close();
            socket.close();
        }
        catch(Exception e)
            {e.printStackTrace();} // вывод исключений

        System.out.println(Thread.currentThread().getName() + " : closing thread");
    }
}
