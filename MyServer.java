import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


//public class MyServer {
//    private static final String TAG = "MyServer";
//    private String content = "";
//    public static void main(String args[]){
//        new MyServer();
//    }
//    public  MyServer(){
//        try{
//            InetAddress addr = InetAddress.getLocalHost();
//            System.out.println("local host:" + addr);
//
//            //创建server socket
//            ServerSocket serverSocket = new ServerSocket(9999);
//            System.out.println("listen port 9999");
//
//            while(true){
//                System.out.println("waiting client connect");
//                Socket socket = serverSocket.accept();
//                System.out.println("accept client connect" + socket);
//                new Thread(new Service(socket)).start();
//            }
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }
//
//    class Service implements Runnable{
//        private Socket socket;
//        private BufferedReader in = null;
//
//        public Service(Socket socket){
//            this.socket = socket;
//            try{
//                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//            }catch (IOException ex){
//                ex.printStackTrace();
//            }
//        }
//
//
//        @Override
//        public void run() {
//            System.out.println("wait client message " );
//            try {
//                while ((content = in.readLine()) != null) {
//
//                    if(content.equals("bye")){
//                        System.out.println("disconnect from client,close socket");
//                        socket.shutdownInput();
//                        socket.shutdownOutput();
//                        socket.close();
//                        break;
//                    }else {
//                        this.sendMessge(socket);
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        public void sendMessge(Socket socket) {
//            PrintWriter pout = null;
//            try{
//                int score = 22;
//                String message = score+","+1;
//                System.out.println("messge to client:" + message);
//                pout = new PrintWriter(new BufferedWriter(
//                        new OutputStreamWriter(socket.getOutputStream(),"utf-8")),true);
//                pout.println(message);
//            }catch (IOException ex){
//                ex.printStackTrace();
//            }
//        }
//    }
//}


import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MyServer {
    private static final String TAG = "MyServer";
    private String content = "";

    // 用于存储连接的客户端套接字
    private static final LinkedBlockingQueue<Socket> waitingPlayers = new LinkedBlockingQueue<>();

    public static void main(String args[]) {
        new MyServer();
    }

    public MyServer() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host: " + addr);

            // 创建 server socket
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("listen port 9999");

            // 等待匹配，匹配上了就启动 service 线程
            while (true) {
                System.out.println("waiting client connect");
                Socket socket = serverSocket.accept();
                System.out.println("accept client connect " + socket);

                // 将新的客户端添加到等待队列
                waitingPlayers.add(socket);

                // 如果有两个客户端在等待队列中，则启动匹配线程
                if (waitingPlayers.size() >= 2) {
                    Socket player1 = waitingPlayers.poll();
                    Socket player2 = waitingPlayers.poll();
                    new Thread(new Service(player1, player2)).start();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class Service implements Runnable {
        private Socket player1;
        private Socket player2;
        private BufferedReader in1 = null;
        private BufferedReader in2 = null;

        // 这里是接收信息
        public Service(Socket player1, Socket player2) {
            this.player1 = player1;
            this.player2 = player2;
            try {
                in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
                in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {

            System.out.println("match successful, start game session");
            sendMessage(player1,"match success");
            sendMessage(player2,"match success");

            try {
                while (true) {
                    if (in1.ready()) {
                        content = in1.readLine();
                        if (content.equals("bye")) {
                            disconnect(player1);
                            break;
                        } else {
                            sendMessage(player2, content);
                            System.out.println("send to p2"+content);
                        }
                    }

                    if (in2.ready()) {
                        content = in2.readLine();
                        System.out.println("player2: " + content);
                        if (content.equals("bye")) {
                            disconnect(player2);
                            break;
                        } else {
                            sendMessage(player1, content);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void sendMessage(Socket socket, String message) {
            PrintWriter pout = null;
            try {
                System.out.println("message to client: " + message);
                pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8")), true);
                pout.println(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void disconnect(Socket socket) {
            try {
                System.out.println("disconnect from client, close socket");
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}