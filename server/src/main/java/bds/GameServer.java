package bds;

import bds.communication.ClientRequest;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class GameServer extends Thread {

    Socket socket;
    int activeThreadCount = 0;


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

            // добавляем данные об адресе сокета:
            data = ""+num+": "+"\n"+data;

            // выводим данные:
            os.write(data.getBytes());

            // завершаем соединение
            s.close();
        }
        catch(Exception e)
        {System.out.println("init error: "+e);} // вывод исключений
    }
}
