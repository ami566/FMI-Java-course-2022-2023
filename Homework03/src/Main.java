import bg.sofia.uni.fmi.mjt.news.client.ClientInterface;
import bg.sofia.uni.fmi.mjt.news.server.ServerRequest;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerRequest serverRequest = new ServerRequest();
        ClientInterface clientInterface = new ClientInterface(serverRequest);
        clientInterface.start();
    }
}