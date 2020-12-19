package demo;

import lejos.nxt.*;
import lejos.util.Delay;
import java.util.Arrays;

/**
 * @author Vicente Ardiles Silva.
 */
public class JerfDemo {
  //Revoluciones por minuto
  private int RPM = 110;
  //Sensor de luz Derecha
  private LightSensor luzDer = new LightSensor(SensorPort.S1);
  //Sensor de luz Izquierda
  private LightSensor luzIzq = new LightSensor(SensorPort.S4);
  //Sensor de luz Central
  private LightSensor luzCentral = new LightSensor(SensorPort.S2);
  private UltrasonicSensor usonic = new UltrasonicSensor(SensorPort.S3);
  //Direccion actual del robot
  private int Dir=3;
  //Posicion inicial
  private int X=2;
  private int Y=0;
  //Posicion Salida
  private int Xsalida = 0;
  private int Ysalida = 2;
  // Valores iniciales de los sensores que luego iran cambiando
  private int der = 0;
  private int izq = 0;
  private int NegroDer=0;
  private int BlancoDer=0;
  private int NegroIzq=0;
  private int BlancoIzq=0;
  private int BlancoAliado=0;
  private int NegroEnemy=0;
  // Contador de Intersecciones
  private int ContadorInter=0;
  //Rango de sensores de luz para medir blanco
  private int Rango=10; //10
  //Rango de sensores de luz para medir negro
  private int RangoNegro=7; //7
  private boolean inicio = false;
  private boolean Final = false;
  private boolean movimiento_Y = true;
  private boolean movimiento_X = true;
  private boolean Demo = true;
  // Arreglos que guardan la informacion de cada bloque
  private int[] punto1 = new int[4];
  private int[] punto2 = new int[4];

  public void Calibrar(){
    boolean Calibrar = true;
    while (Calibrar){
      //Calibrar Sensor 1
      System.out.println("Calibrar NegroDer");
      Button.ENTER.waitForPressAndRelease();
      this.NegroDer = luzDer.readValue();
      System.out.println("Negro derecha: " + NegroDer);
      System.out.println("Calibrar BlancoIzq");
      Button.ENTER.waitForPressAndRelease();
      this.BlancoDer = luzDer.readValue();
      System.out.println("Blanco derecha: " + BlancoDer);
      //Calibrar Sensor 2
      System.out.println("Calibrar NegroIzq");
      Button.ENTER.waitForPressAndRelease();
      this.NegroIzq = luzIzq.readValue();
      System.out.println("Negro izquierda: " + NegroIzq);
      System.out.println("Calibrar BlancoIzq");
      Button.ENTER.waitForPressAndRelease();
      this.BlancoIzq = luzIzq.readValue();
      System.out.println("Blanco izquierda: " + BlancoIzq);
      System.out.println("Calibracion Completada");
      Button.ENTER.waitForPress();
      Calibrar = false;
    }
  }

  //Metodo para actualizar la posicion en X
  private int ActX(int X, int Dir) {
    //Direccion: 1=izquierda, 2=derecha, 3=arriba, 4= abajo
    if (Dir == 1) {
      return X - 1;
    } else if (Dir == 2) {
      return X + 1;
    } else {
      return X;
    }
  }

  //Metodo para actualizar la posicion en Y
  private int ActY(int Y, int Dir) {
    //Direccion: 1=izquierda, 2=derecha, 3=arriba, 4= abajo
    if (Dir == 4) {
      return Y - 1;
    } else if (Dir == 3) {
      return Y + 1;
    } else {
      return Y;
    }
  }

  //Metodo para doblar a la derecha
  private void giroDerecha() {
    Motor.B.forward();
    Motor.C.forward();
    Delay.msDelay(1200);
    Motor.B.stop(true);
    Motor.C.stop();
    Motor.B.rotate(-175,true);
    Motor.C.rotate(175);
    if (Dir == 1) {
      Dir = 3;
    } else if (Dir == 2) {
      Dir = 4;
    } else if (Dir == 3) {
      Dir = 2;
    } else {
      Dir =1;
    }
  }

  //	Metodo para doblar a la izquierda
  private void giroIzquierda() {
    Motor.B.forward();
    Motor.C.forward();
    Delay.msDelay(1200);
    Motor.B.stop(true);
    Motor.C.stop();
    Motor.B.rotate(175,true);
    Motor.C.rotate(-175);
    if (Dir == 1) {
      Dir = 4;
    } else if (Dir == 2) {
      Dir = 3;
    } else if (Dir == 3) {
      Dir = 1;
    } else {
      Dir = 2;
    }
  }

  // Metodo para cambiar el sentido que mira el robot
  // Direccion: 1=izquierda, 2=derecha, 3=arriba, 4=abajo
  private void setDir(int Sen) {
    Motor.B.stop(true);
    Motor.C.stop();
    if (Sen == 1) {
      if (Dir == 1) {
        Dir = 1;
      } else if (Dir == 2) {
        Motor.C.rotate(-360,true);
        Motor.B.rotate(360);
        Dir = 1;
      } else if (Dir == 3) {
        Motor.B.rotate(180,true);
        Motor.C.rotate(-180);
        Dir = 1;
      } else {
        Motor.C.rotate(180,true);
        Motor.B.rotate(-180);
        Dir = 1;
      }
    } else if (Sen == 2) {
      if (Dir == 1) {
        Motor.C.rotate(360,true);
        Motor.B.rotate(-360);
        Dir = 2;
      } else if (Dir == 2) {
        Dir = 2;
      } else if (Dir == 3) {
        Motor.B.rotate(-180,true);
        Motor.C.rotate(180);
        Dir = 2;
      } else {
        Motor.B.rotate(180,true);
        Motor.C.rotate(-180);
        Dir = 2;
      }
    } else if (Sen == 3) {
      if (Dir == 1) {
        Motor.C.rotate(180,true);
        Motor.B.rotate(-180);
        Dir = 3;
      } else if (Dir == 2) {
        Motor.C.rotate(-180,true);
        Motor.B.rotate(180);
        Dir = 3;
      } else if (Dir == 3) {
        Dir = 3;
      } else {
        Motor.B.rotate(360,true);
        Motor.C.rotate(-360);
        Dir = 3;
      }
    } else {
      if (Dir == 1) {
        Motor.C.rotate(-180,true);
        Motor.B.rotate(180);
        Dir = 4;
      } else if (Dir == 2) {
        Motor.C.rotate(180,true);
        Motor.B.rotate(-180);
        Dir = 4;
      } else if (Dir == 3) {
        Motor.C.rotate(360,true);
        Motor.B.rotate(-360);
        Dir = 4;
      } else {
        Dir = 4;
      }
    }
  }

  // Metodo que permite al robot moverse en el eje X al punto que uno quiera
  private void desplazamientoX(int Xf) {
    movimiento_X = true;
    Motor.B.setSpeed(RPM);
    Motor.C.setSpeed(RPM);
    Motor.B.stop(true);
    Motor.C.stop();
    // Si el X dado es mayor, mira a la derecha
    if (Xf>X) {
      setDir(2);
    }
    // Si el X dado es menor , mira a la izquierda
    else if (Xf<X) {
      setDir(1);
    }
    else {
      movimiento_X = false;
    }
    while (movimiento_X) {
      int der = luzDer.readValue();
      int izq = luzIzq.readValue();
      // Si los dos sensores leen blanco, avanzar...
      if(der <=BlancoDer+Rango && der>=BlancoDer-Rango && izq <=BlancoIzq+Rango && izq>=BlancoIzq-Rango){
        System.out.println("Recto...");
        Motor.B.forward();
        Motor.C.forward();
      }

      //Si el robot se encuentra desviado hacia el lado izquierdo del camino
      else if(der <=NegroDer+RangoNegro && der>=NegroDer-RangoNegro && izq <=BlancoIzq+Rango && izq>=BlancoIzq-Rango){
        System.out.println("Desviando...");
        Motor.B.stop(true);
        Motor.C.stop();
        Motor.C.rotate(15);
      }

      //Si el robot se encuentra desviado hacia el lado derecho del camino
      else if(der <=BlancoDer+Rango && der>=BlancoDer-Rango && izq <=NegroIzq+RangoNegro && izq>=NegroIzq-RangoNegro){
        System.out.println("Desviando...");
        Motor.B.stop(true);
        Motor.C.stop();
        Motor.B.rotate(15);
      }

      //Si el robot se encuentra en una interseccion
      else if(der <= NegroDer+RangoNegro && der >= NegroDer-RangoNegro && izq <= NegroIzq+RangoNegro && izq>= NegroIzq-RangoNegro){
        //Se actualiza la posicion
        System.out.println("Interseccion...");
        X = ActX(X, Dir);
        Y = ActY(Y, Dir);
        System.out.println("(" + X + "," + Y + ")");
        if (Xf>X) {
          if (ContadorInter==Xf-X-1) {
            Motor.B.stop(true);
            Motor.C.stop();
            Motor.B.forward();
            Motor.C.forward();
            Delay.msDelay(1200);
            Motor.B.stop(true);
            Motor.C.stop();
            System.out.println("ContadorInter" + ContadorInter);
            ContadorInter=0;
            movimiento_X=false;
          }
          else {
            ContadorInter++;
            Motor.B.forward();
            Motor.C.forward();
            System.out.println("ContadorInter" + ContadorInter);
            Delay.msDelay(1200);
          }
        }
        else if (Xf<X) {
          if (ContadorInter==X-Xf-1) {
            Motor.B.stop(true);
            Motor.C.stop();
            Motor.B.forward();
            Motor.C.forward();
            Delay.msDelay(1200);
            Motor.B.stop(true);
            Motor.C.stop();
            System.out.println("ContadorInter" + ContadorInter);
            ContadorInter=0;
            movimiento_X=false;
          }
          else {
            ContadorInter++;
            Motor.B.forward();
            Motor.C.forward();
            System.out.println("ContadorInter" + ContadorInter);
            Delay.msDelay(1200);
          }
        }
        else {
          Motor.B.stop(true);
          Motor.C.stop();
          Motor.B.forward();
          Motor.C.forward();
          Delay.msDelay(1400);
          Motor.B.stop(true);
          Motor.C.stop();
          movimiento_X=false;
        }
      }
    }
  }

  // Metodo que permite al r+obot moverse en el eje Y al punto que uno quiera
  private void desplazamientoY(int Yf) {
    movimiento_Y=true;
    Motor.B.setSpeed(RPM);
    Motor.C.setSpeed(RPM);
    Motor.B.stop(true);
    Motor.C.stop();
    // Si el Y dado es mayor, mirar hacia arriba
    if (Yf>Y) {
      setDir(3);
    }
    // Si el Y dado es menor, mirar hacia abajo
    else if (Yf<Y) {
      setDir(4);
    }
    else {
      movimiento_Y=false;
    }
    while (movimiento_Y) {
      int der = luzDer.readValue();
      int izq = luzIzq.readValue();
      if(der <=BlancoDer+Rango && der>=BlancoDer-Rango && izq <=BlancoIzq+Rango && izq>=BlancoIzq-Rango){
        System.out.println("Recto...");
        Motor.B.forward();
        Motor.C.forward();
      }

      //Si el robot se encuentra desviado hacia el lado izquierdo del camino
      else if(der <=NegroDer+RangoNegro && der>=NegroDer-RangoNegro && izq <=BlancoIzq+Rango && izq>=BlancoIzq-Rango){
        System.out.println("Desviando...");
        Motor.B.stop(true);
        Motor.C.stop();
        Motor.C.rotate(15);
      }

      //Si el robot se encuentra desviado hacia el lado derecho del camino
      else if(der <=BlancoDer+Rango && der>=BlancoDer-Rango && izq <=NegroIzq+RangoNegro && izq>=NegroIzq-RangoNegro){
        System.out.println("Desviando...");
        Motor.B.stop(true);
        Motor.C.stop();
        Motor.B.rotate(15);
      }

      //Si el robot se encuentra en una interseccion
      else if(der <= NegroDer+RangoNegro && der >= NegroDer-RangoNegro && izq <= NegroIzq+RangoNegro && izq>= NegroIzq-RangoNegro){
        //Se actualiza la posicion
        System.out.println("Interseccion...");
        X = ActX(X, Dir);
        Y = ActY(Y, Dir);
        System.out.println("(" + X + "," + Y + ")");
        if (Yf>Y) {
          if (ContadorInter==Yf-Y-1) {
            Motor.B.stop(true);
            Motor.C.stop();
            Motor.B.forward();
            Motor.C.forward();
            Delay.msDelay(1200);
            Motor.B.stop(true);
            Motor.C.stop();
            System.out.println("ContadorInter" + ContadorInter);
            ContadorInter=0;
            movimiento_Y=false;
          }
          else {
            ContadorInter++;
            Motor.B.forward();
            Motor.C.forward();
            System.out.println("ContadorInter" + ContadorInter);
            Delay.msDelay(1200);
          }
        }
        else if (Yf<Y) {
          if (ContadorInter==Y-Yf-1) {
            Motor.B.stop(true);
            Motor.C.stop();
            Motor.B.forward();
            Motor.C.forward();
            Delay.msDelay(1200);
            Motor.B.stop(true);
            Motor.C.stop();
            System.out.println("ContadorInter" + ContadorInter);
            ContadorInter=0;
            movimiento_Y=false;
          }
          else {
            ContadorInter++;
            Motor.B.forward();
            Motor.C.forward();
            System.out.println("ContadorInter" + ContadorInter);
            Delay.msDelay(1200);
          }
        }
        else {
          Motor.B.forward();
          Motor.C.forward();
          Delay.msDelay(1400);
          Motor.B.stop(true);
          Motor.C.stop();
          movimiento_Y=false;
        }
      }
    }
  }

  // Metodo que le permite al robot saber el punto por el cual entro a la matriz
  // y a partir de eso generar toda la matriz de puntos.
  private void initialPosition() {
    desplazamientoX(2);
    desplazamientoY(0);
  }

  // Metodo que le permite al robot ir a la salida y dejar al Aliado sano y salvo
  private void goToEnd() {
    desplazamientoY(2);
    desplazamientoX(2);
    setDir(3);
    Motor.B.forward();
    Motor.C.forward();
    Delay.msDelay(1500);
    Motor.B.stop(true);
    Motor.C.stop();
    Motor.A.rotate(-200);
    setDir(4);
    Motor.B.forward();
    Motor.C.forward();
    Delay.msDelay(1500);
  }

  // Metodo que entraga la suma de los elementos de un array de enteros
  private int suma(int[] pocisiones){
    int suma=0;
    for(int i=0;i<pocisiones.length;i++){
      suma+=pocisiones[i];
    }
    return suma;
  }

  // Metodo que junta en este caso 4 arrays.
  // Notar que hay que importar System.
  private int[] JuntarArray(int[] array1, int[] array2,int[] array3,int[] array4){
    int[] ret1 = new int[array1.length + array2.length];
    System.arraycopy(array1, 0, ret1, 0, array1.length);
    System.arraycopy(array2, 0, ret1, array1.length, array2.length);
    int[] ret2 = new int[ret1.length + array3.length];
    System.arraycopy(ret1, 0, ret2, 0, ret1.length);
    System.arraycopy(array3, 0, ret2, ret1.length, array3.length);
    int[] total = new int[ret2.length + array4.length];
    System.arraycopy(ret2, 0, total, 0, ret2.length);
    System.arraycopy(array4, 0, total, ret2.length, array4.length);
    return total;
  }

  // Metodo que verifica si un elemento se encuentra en un array
  private boolean contener(int array[], int num){
    boolean respuesta = false;
    for (int i=0; i<array.length; i++){
      if (array[i]==num){
        respuesta = true;
      }
    }
    return respuesta;
  }

  // Metodo para atacar
  // Basicamente rota a la izquierda para bajar la garra primero y no pasar a llevar al enemigo en frente por temas de espacio
  // luego rota a la derecha de nuevo para golpear al objetivo levantando la garra
  public static void Attack(){
    Motor.B.rotate(90,true);
    Motor.C.rotate(-90);
    Delay.msDelay(400);
    Motor.A.rotate(230);
    Motor.B.rotate(-90,true);
    Motor.C.rotate(90);
    Delay.msDelay(400);
    Motor.A.rotate(-230);
  }

  // Metodo para defender al aliado
  // El robot abraza al aliado con la garra
  private void Defense(){
    Motor.B.forward();
    Motor.C.forward();
    Delay.msDelay(1300);
    Motor.C.stop(true);
    Motor.B.stop();
    Motor.A.rotate(240);
    Motor.B.backward();
    Motor.C.backward();
    Delay.msDelay(1300);
    Motor.C.stop(true);
    Motor.B.stop();

  }

  // Funcion que ya en el centro de los cuatro cuadrados, rota sobre si mismo hasta la posicion del objetivo (aliado o pecador)
  // y lo salva o mata segun lo que uno le pida previamente.
  // En caso de que haya que defender solo 1 personas en un punto, luego de defender ,  el robot volvera  a ese punto en vez de pasar a otro directamente
  // queda propuesto agregar las condiciones para que esto no pase.
  private void accion(int indice,int objetivo){
    if (indice==0){
      System.out.println("Objetivo en "+indice);
      Motor.C.rotate(90,true);
      Motor.B.rotate(-90);
      if (objetivo==1){
        Defense();
        Motor.C.rotate(-90,true);
        Motor.B.rotate(90);
        goToEnd();
      }
      if (objetivo==-1){
        Attack();
        Motor.C.rotate(-90,true);
        Motor.B.rotate(90);
      }
    }
    if (indice==1){
      System.out.println("Objetivo en "+indice);
      Motor.C.rotate(270,true);
      Motor.B.rotate(-270);
      if (objetivo==1){
        Defense();
        Motor.C.rotate(-270,true);
        Motor.B.rotate(270);
        goToEnd();
      }
      if (objetivo==-1){
        Attack();
        Motor.C.rotate(-270,true);
        Motor.B.rotate(270);
      }

    }
    if (indice==2){
      System.out.println("Objetivo en "+indice);
      Motor.B.rotate(270,true);
      Motor.C.rotate(-270);
      if (objetivo==1){
        Defense();
        Motor.B.rotate(-270,true);
        Motor.C.rotate(270);
        goToEnd();
      }
      if (objetivo==-1){
        Attack();
        Motor.B.rotate(-270,true);
        Motor.C.rotate(270);
      }
    }
    if (indice==3){
      System.out.println("Objetivo en "+indice);
      Motor.B.rotate(90,true);
      Motor.C.rotate(-90);
      if (objetivo==1){
        Defense();
        Motor.B.rotate(-90,true);
        Motor.C.rotate(90);
        goToEnd();
      }
      if (objetivo==-1){
        Attack();
        Motor.B.rotate(-90,true);
        Motor.C.rotate(90);
      }
    }
  }

  // Metodo que detecta donde estan los objetivos y va a su ubicacion realizando la accion correspondiente
  // Con ubicacion nos referimos a uno de los cuatro puntos principales donde se escanea. Luego mira al cuadrado correspondiente
  private void Deteccion(int[] punto, int objetivo){
    for(int i=0;i<4;i++){
      if (punto[i]==objetivo){
        accion(i,objetivo);
        punto[i]=0;
      }
    }
  }

  private void DerrotarMalvados(int[] lugares){
    System.out.println("Derrotar Malvados >:c");
    Deteccion(lugares,-1);
  }

  private void PazYAmor(int[] lugares){
    System.out.println("Salvar a los fieles uwu");
    Deteccion(lugares,1);

  }

  private void Decision(int[] ubicaciones){
    System.out.println(suma(ubicaciones));
    if (suma(ubicaciones)<=0){
      PazYAmor(ubicaciones);
    }
    else {
      DerrotarMalvados(ubicaciones);
    }
  }

  // Metodo que escanea los 4 cuadrados alrededor de un punto y los guarda en un array
  private int[] Escanear(){
    int[] punto = new int[4];
    Motor.C.rotate(97,true);
    Motor.B.rotate(-97);
    for (int i=0; i<4;i++){
      int objetivo = usonic.getDistance();
      if (objetivo <20){
        Motor.C.forward();
        Motor.B.forward();
        Delay.msDelay(1100);
        Motor.C.stop(true);
        Motor.B.stop();
        int color = luzCentral.readValue();
        if (color<=30){
          punto[i] = -1;
        }
        if  (color>30){
          punto[i] = 1;
        }
        Motor.C.backward();
        Motor.B.backward();
        Delay.msDelay(1100);
        Motor.C.stop(true);
        Motor.B.stop();
      }
      else{
        punto[i]=0;
      }
      // Como se puede ver aqui hay un error ya que hay un if,else,if,else, pero de todas formas funciona
      if (i<3){
        Motor.C.rotate(183,true);
        Motor.B.rotate(-183);
      }
      else{
        Motor.C.rotate(93,true);
        Motor.B.rotate(-93);
      }
    }
    System.out.println(Arrays.toString(punto));
    return punto;
  }

  /**
   * Funcion que une todo y lo que hace Jerf
   */
  public void run(){
    Button.ENTER.waitForPress();
    while (Demo) {
      Calibrar();
      //Motor Derecho
      Motor.B.setSpeed(RPM);
      //Motor Izquierdo
      Motor.C.setSpeed(RPM);
      desplazamientoX(3);
      desplazamientoY(1);
      punto1 = Escanear();
      Decision(punto1);
      desplazamientoX(2);
      desplazamientoY(1);
      desplazamientoX(1);
      desplazamientoY(1);
      setDir(3);
      punto2 = Escanear();
      Decision(punto2);
      desplazamientoX(2);
      desplazamientoY(1);
      setDir(3);
      Demo=false;
    }
  }

  /**
   * Main Function
   */
  public static void main (String[] args) {
    JerfDemo jerf = new JerfDemo();
    jerf.run();
  }
}
