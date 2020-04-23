package com.example.practica11peerandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private DatagramSocket socket;
    private InetAddress eclipse;
    private boolean terminaTurno;
    private boolean finJuego;
    private DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = findViewById(R.id.drawView);
        terminaTurno = true;
        finJuego = false;
        drawView.setActivity(this);

        new Thread(
                () -> {
                    try {
                        eclipse = InetAddress.getByName("192.168.0.5");
                        socket = new DatagramSocket(5000);

                        while (true) {
                            //Si es un Datagram de recepcipn, solo le ponemos dos parametros
                            byte[] buffer = new byte[100]; //-> Entero que puede representar 256, ASCII, 8 bit, bit -> -0 o 1
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                            //Esta linea, se queda esperando por paquetes
                            socket.receive(packet);

                            //El mensaje queda en el paquete, despues de la recepcion
                            String json = new String(packet.getData()).trim();

                            if(json.startsWith("{") && terminaTurno && !finJuego) {
                                //Recibir jugada
                                Gson gson = new Gson();
                                Jugada jugada = gson.fromJson(json, Jugada.class);
                                drawView.getJugadas().add(jugada);
                                drawView.setTurno("Es tu turno, eres la O");

                                int fila = (int) jugada.getPosX();
                                int col = (int) jugada.getPosY();

                                drawView.cambiarValorMatriz(fila, col, 1);
                                terminaTurno = false;
                            }
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    // Hilo para enviar mensaje
    public void enviarMensaje(String mensaje) {
        new Thread(
                () -> {
                    try {
                        byte[] buffer = mensaje.getBytes();
                        //El paquete tiene 4 parametros
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, eclipse, 5000);
                        socket.send(packet);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    // Getters y setters
    public boolean isTerminaTurno() {
        return terminaTurno;
    }

    public void setTerminaTurno(boolean terminaTurno) {
        this.terminaTurno = terminaTurno;
    }

    public boolean isFinJuego() {
        return finJuego;
    }

    public void setFinJuego(boolean finJuego) {
        this.finJuego = finJuego;
    }
}
