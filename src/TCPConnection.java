import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {
    private final Socket socket;
    private final Thread thread;
    private final TCPConnectionListener tcpConnectionListener;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;

    public TCPConnection(TCPConnectionListener tcpConnectionListener, String ipAdress, int port) throws IOException {
        this(tcpConnectionListener, new Socket(ipAdress, port));
    }

    public TCPConnection(TCPConnectionListener tcpConnectionListener, Socket socket) throws IOException {
        this.tcpConnectionListener = tcpConnectionListener;
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    tcpConnectionListener.onConnectionReady(TCPConnection.this);
                    while (!thread.isInterrupted()) {
                        tcpConnectionListener.onReceiveString(TCPConnection.this, bufferedReader.readLine());
                    }
                } catch (IOException e) {
                    tcpConnectionListener.onException(TCPConnection.this, e);
                } finally {
                    tcpConnectionListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        thread.start();
    }

    public synchronized void sendMessage(String value) {
        try {
            bufferedWriter.write(value + "\r\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            tcpConnectionListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            tcpConnectionListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection : " + socket.getInetAddress() + ": " + socket.getPort();
    }
}