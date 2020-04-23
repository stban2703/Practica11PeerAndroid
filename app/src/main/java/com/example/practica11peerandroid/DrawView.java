package com.example.practica11peerandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DrawView extends View {

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int width;
    private int height;
    private int[][] matriz;
    private ArrayList<Jugada> jugadas;
    private MainActivity activity;
    private String turno;

    // Control + O
    // Sucede cuando la aplicacion inicia
    // Equivalente a settings
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        setup();
    }

    // Equivalente al setup en processing
    private void setup() {
        matriz = new int[3][3];
        jugadas = new ArrayList<Jugada>();
        turno = "Esperando a jugador 1...";
    }

    // Equivalente al draw
    // Control + O
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(255, 255, 255, 255);

        // Estilo del texto
        Paint pTurno = new Paint();
        pTurno.setTextSize(50);
        pTurno.setColor(Color.rgb(0, 0, 0));
        pTurno.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(turno, width / 2, 100, pTurno);

        // Estilo del borde
        Paint pBorder = new Paint();
        pBorder.setStyle(Paint.Style.STROKE);
        pBorder.setColor(Color.rgb(255, 255, 255));

        // Estilo del relleno
        Paint pFill = new Paint();
        pFill.setStyle(Paint.Style.FILL);
        pFill.setColor(Color.rgb(0, 50, 150));

        // Pintar tablero
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 3; col++) {
                int posX = col * width / 3;
                int posY = fila * width / 3 + 200;
                int widthX = posX + width / 3;
                int heightY = posY + width / 3;
                canvas.drawRect(posX, posY, widthX, heightY, pFill);
                canvas.drawRect(posX, posY, widthX, heightY, pBorder);
            }
        }

        // Pintar jugada
        Paint pJugada = new Paint();
        pJugada.setTextSize(200);
        pJugada.setColor(Color.rgb(255, 255, 255));
        pJugada.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < jugadas.size(); i++) {
            float x = jugadas.get(i).getPosX();
            float y = jugadas.get(i).getPosY();
            String simbolo = jugadas.get(i).getSimbolo();

            canvas.drawText(simbolo, x * width / 3 + ((width / 3f) / 2f), y * width / 3 + 275 + ((width / 3f) / 2f), pJugada);

        }


        // Derrota
        if ((matriz[0][0] == 1 && matriz[0][1] == 1 && matriz[0][2] == 1) ||
                (matriz[1][0] == 1 && matriz[1][1] == 1 && matriz[1][2] == 1) ||
                (matriz[2][0] == 1 && matriz[2][1] == 1 && matriz[2][2] == 1) ||
                (matriz[0][0] == 1 && matriz[1][0] == 1 && matriz[2][0] == 1) ||
                (matriz[0][1] == 1 && matriz[1][1] == 1 && matriz[2][1] == 1) ||
                (matriz[0][2] == 1 && matriz[1][2] == 1 && matriz[2][2] == 1) ||
                (matriz[0][0] == 1 && matriz[1][1] == 1 && matriz[2][2] == 1) ||
                (matriz[0][2] == 1 && matriz[1][1] == 1 && matriz[2][0] == 1)) {
            turno = "Perdiste";
            activity.setFinJuego(true);

            // Victoria
        } else if ((matriz[0][0] == 2 && matriz[0][1] == 2 && matriz[0][2] == 2) ||
                (matriz[1][0] == 2 && matriz[1][1] == 2 && matriz[1][2] == 2) ||
                (matriz[2][0] == 2 && matriz[2][1] == 2 && matriz[2][2] == 2) ||
                (matriz[0][0] == 2 && matriz[1][0] == 2 && matriz[2][0] == 2) ||
                (matriz[0][1] == 2 && matriz[1][1] == 2 && matriz[2][1] == 2) ||
                (matriz[0][2] == 2 && matriz[1][2] == 2 && matriz[2][2] == 2) ||
                (matriz[0][0] == 2 && matriz[1][1] == 2 && matriz[2][2] == 2) ||
                (matriz[0][2] == 2 && matriz[1][1] == 2 && matriz[2][0] == 2)) {
            turno = "Ganaste";
            activity.setFinJuego(true);

            // Empate
        } else if ((matriz[0][0] != 0 && matriz[0][1] != 0 && matriz[0][2] != 0)
                && (matriz[1][0] != 0 && matriz[1][1] != 0 && matriz[1][2] != 0)
                && (matriz[2][0] != 0 && matriz[2][1] != 0 && matriz[2][2] != 0)
                && (matriz[0][0] != 0 && matriz[1][0] != 0 && matriz[2][0] != 0)
                && (matriz[0][1] != 0 && matriz[1][1] != 0 && matriz[2][1] != 0)
                && (matriz[0][2] != 0 && matriz[1][2] != 0 && matriz[2][2] != 0)
                && (matriz[0][0] != 0 && matriz[1][1] != 0 && matriz[2][2] != 0)
                && (matriz[0][2] != 0 && matriz[1][1] != 0 && matriz[2][0] != 0)) {
            turno = "Empate";
            activity.setFinJuego(true);
        }


        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:
                // Enviar jugada
                for (int col = 0; col < 3; col++) {
                    for (int fila = 0; fila < 3; fila++) {

                        int posX = fila * width / 3;
                        int posY = col * width / 3 + 200;
                        int widthX = posX + width / 3;
                        int heightY = posY + width / 3;

                        // Areas sensibles
                        if (event.getX() > posX && event.getX() < widthX && event.getY() > posY
                                && event.getY() < heightY && !activity.isTerminaTurno()
                                && matriz[fila][col] == 0 && !activity.isFinJuego()) {
                            Jugada juego = new Jugada(fila, col, "O");
                            jugadas.add(juego);
                            Gson gson = new Gson();
                            String json = gson.toJson(juego);
                            turno = "Esperando a jugador 1...";
                            activity.enviarMensaje(json);
                            matriz[fila][col] = 2;
                            activity.setTerminaTurno(true);
                        }
                    }
                }

                break;

        }
        return true;
    }

    //Getters y setters
    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public ArrayList<Jugada> getJugadas() {
        return jugadas;
    }

    public int cambiarValorMatriz(int fila, int col, int valor) {
        return matriz[fila][col] = valor;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
