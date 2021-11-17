package Conection;

import Pong.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
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
            /*
             * Criação do servidor
             */
            server = new ServerSocket(port);
            System.out.printf("Servidor iniciado na porta %d\n", port);


            client = server.accept();

            /*
             * Criação dos players e da bola.
             * O server é o Player1 pois principal = true
             */
            Player p1 = new Player(40,300,true);
            Player p2 = new Player(1100, 300,false);
            Bola ball = new Bola(400,300, 3);
            Canvas pongScreen = new Canvas(p1,p2, ball, 0,0);
            pongScreen.setVisible(true);




            /*
             * Envio de dados para o cliente
             */
            do {
                out = new PrintWriter(client.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));

                out.println("player;" + p1.getY());
                out.println("bola;" + ball.getX() + ";" + ball.getY());

                String response = input.readLine();

                if(response != null ){
                    String[] data = response.split(";");

                    if(data[0].equals("player")){
                        p2.setY(Integer.parseInt(data[1]));
                    }
                }

                /*
                * Colisão com o Player1
                *
                * */
                if (ball.getX()== 90 && ((ball.getY() > p1.getY()) && (ball.getY() < p1.getY() + 65))){
                    ball.setDy(-1);
                    ball.setDirecao(true);
                }
                if (ball.getX()== 90 && ((ball.getY() > p1.getY() + 65) && (ball.getY() < p1.getY() + 130) )){
                    ball.setDirecao(true);
                    ball.setDy(0);
                }
                if (ball.getX() == 90 && (ball.getY() > p1.getY() + 130 && (ball.getY() < p1.getY() + 200))){
                    ball.setDy(1);
                    ball.setDirecao(true);
                }
                /*
                 * Colisão com o Player2
                 *
                 * */
                if (ball.getX()== 1000 && ((ball.getY() > p2.getY()) && (ball.getY() < p2.getY() + 65))){
                    ball.setDy(-1);
                    ball.setDirecao(false);
                }
                if (ball.getX()== 1000  && ((ball.getY() > p2.getY() + 65) && (ball.getY() < p2.getY() + 130))){
                    ball.setDirecao(false);
                    ball.setDy(0);
                }
                if (ball.getX() == 1000  && ((ball.getY() > p2.getY() + 130) && (ball.getY() < p2.getY() + 200))){
                    ball.setDy(1);
                    ball.setDirecao(false);
                }
                /*
                * pontuação Player1 e Player2
                * */
                if (ball.getX() < 80){
                    pongScreen.board.setPontuacaop1(1);
                    ball.setX(400);
                    ball.setY(300);

                }
                if (ball.getX() > 1100){
                    pongScreen.board.setPontuacaop2(1);
                    ball.setX(400);
                    ball.setY(300);
                }
            }
            while(isGameRunning);

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void stopGame(){
        isGameRunning = false;
    }

    public void sendData(String data){
        out.println(data);
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
