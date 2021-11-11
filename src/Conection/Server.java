package Conection;

import Pong.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket server;
    private Socket client;
    private PrintWriter out;
    private BufferedReader input;
    private Boolean isGameRunning;
    private String[] position;

    public Server(){
        isGameRunning = true;
    }

    public void createServer(int port){

        try{
            server = new ServerSocket(port);
            System.out.printf("Servidor iniciado na porta %d\n", port);


            client = server.accept();
            int x = 0;

            Player p1 = new Player(40,300);
            Player p2 = new Player(1100, 300);
            Bola bola = new Bola(400,300);
            Canvas pongScreen = new Canvas(p1,p2, bola);
            pongScreen.setVisible(true);


            Bola ball = pongScreen.getBoard().getBola();

            do {

                out = new PrintWriter(client.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));

                ball.move();

                out.println("player;" + p1.getY());
                out.println("bola;" + ball.getX() + ";" + ball.getY());

                //receiveData(input.readLine());


            }
            while(isGameRunning);



//            do{
//                String data = "player;" + p1.getY();
//
//                if(!data.equals(sentData)){
//                    sentData = data;
//                    out.print(data);
//                }
//
//            }
//            while(isGameRunning);
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void stopGame(){
        isGameRunning = false;
    }

    public void receiveData(String data){
        System.out.println(data);

        if ("hello server".equals(data)) {
            out.println("hello client");
        }
        else {
            out.println("unrecognised greeting");
        }
    }

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);

        System.out.print("Digite a porta para rodar o servidor: ");
        int port = Integer.parseInt(input.nextLine());

        Server pongServer = new Server();
        pongServer.createServer(port);
    }

}
