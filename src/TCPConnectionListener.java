public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection); //соединение запущено

    void onReceiveString(TCPConnection tcpConnection, String value); //принята строка соединения

    void onDisconnect(TCPConnection tcpConnection); //обрыв соединения

    void onException(TCPConnection tcpConnection, Exception e);
}