package rompecabezasDeOcho;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class RompecabezasDeOcho {

	final int ARRIBA = 1;
	final int ABAJO = -1;
	final int IZQUIERDA = -2;
	final int DERECHA = 2;

	private int mat[][];
	private int peso;
	private int casilleroVacioFila;
	private int casilleroVacioColumna;

	public RompecabezasDeOcho(int mat[][]) {
		this.mat = mat;
		this.peso = this.calcularPeso();
	}

	public void mostrar() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(this.mat[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void grabarPuzzle(PrintWriter w) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				w.print(this.mat[i][j] + "  ");
			}
			w.println("                          ");
		}

		w.println("                          ");
		w.println("--------------------------");
		w.println("                          ");
	}

	public RompecabezasDeOcho(RompecabezasDeOcho puzz) {
		this.mat = puzz.mat;
		this.peso = puzz.peso;
		this.casilleroVacioFila = puzz.casilleroVacioFila;
		this.casilleroVacioColumna = puzz.casilleroVacioColumna;
	}

	public int getPeso() {
		return this.peso;
	}

	private int calcularPeso() {
		int peso = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (mat[i][j] != 0) {
					peso += Math.sqrt(Math.pow(i - (mat[i][j] - 1) / 3, 2))
							+ Math.sqrt(Math.pow(j - (mat[i][j] - 1 - ((mat[i][j] - 1) / 3) * 3), 2));
				} else {
					this.casilleroVacioFila = i;
					this.casilleroVacioColumna = j;
				}
			}
		}
		return peso;
	}

	private int[] conocerPosicionesPermitidas() {
		int posPerIDArAb[] = new int[4];

		if (this.casilleroVacioColumna != 0)
			posPerIDArAb[0] = 1;

		if (this.casilleroVacioColumna != 2)
			posPerIDArAb[1] = 1;

		if (this.casilleroVacioFila != 0)
			posPerIDArAb[2] = 1;

		if (this.casilleroVacioFila != 2)
			posPerIDArAb[3] = 1;

		return posPerIDArAb;
	}

	private int mover(int pos) {

		if (pos == this.ARRIBA) {
			this.mat[this.casilleroVacioFila][this.casilleroVacioColumna] = this.mat[this.casilleroVacioFila
					- 1][this.casilleroVacioColumna];
			this.mat[this.casilleroVacioFila - 1][this.casilleroVacioColumna] = 0;
			this.casilleroVacioFila -= 1;
		}

		else if (pos == this.ABAJO) {
			this.mat[this.casilleroVacioFila][this.casilleroVacioColumna] = this.mat[this.casilleroVacioFila
					+ 1][this.casilleroVacioColumna];
			this.mat[this.casilleroVacioFila + 1][this.casilleroVacioColumna] = 0;
			this.casilleroVacioFila += 1;
		}

		else if (pos == this.IZQUIERDA) {
			this.mat[this.casilleroVacioFila][this.casilleroVacioColumna] = this.mat[this.casilleroVacioFila][this.casilleroVacioColumna
					- 1];
			this.mat[this.casilleroVacioFila][this.casilleroVacioColumna - 1] = 0;
			this.casilleroVacioColumna -= 1;
		}

		else if (pos == this.DERECHA) {
			this.mat[this.casilleroVacioFila][this.casilleroVacioColumna] = this.mat[this.casilleroVacioFila][this.casilleroVacioColumna
					+ 1];
			this.mat[this.casilleroVacioFila][this.casilleroVacioColumna + 1] = 0;
			this.casilleroVacioColumna += 1;
		}

		this.peso = this.calcularPeso();
		return pos;
	}

	public boolean evaluarPuzzle() {
		int vec[] = new int[9];
		int inversion = 0;
		for (int i = 0; i < 3; i++) {
			vec[i] = this.mat[0][i];
		}

		for (int i = 0; i < 3; i++) {
			vec[3 + i] = this.mat[1][i];
		}

		for (int i = 0; i < 3; i++) {
			vec[6 + i] = this.mat[2][i];
		}

		for (int i = 0; i < 9 - 1; i++) {
			for (int j = i + 1; j < 9; j++) {
				if (vec[i] != 0 && vec[j] != 0 && vec[i] > vec[j])
					inversion++;
			}
		}

		return inversion % 2 == 0;

	}

	private int mejorMovimiento(int vec[], int movimientoAnterior) {
		RompecabezasDeOcho aux1 = new RompecabezasDeOcho(this);
		int pesos[] = new int[] { -1, -1, -1, -1 };
		int mejor = 1000;
		int mejorMov = 0;
		if (vec[0] == 1 && movimientoAnterior != DERECHA) {
			aux1.mover(IZQUIERDA);
			pesos[0] = aux1.calcularPeso();
			aux1.mover(DERECHA);
			// aux1.mat=this.mat;
		}
		if (vec[1] == 1 && movimientoAnterior != IZQUIERDA) {
			aux1.mover(DERECHA);
			pesos[1] = aux1.calcularPeso();
			aux1.mover(IZQUIERDA);
			// aux1.mat=this.mat;
		}
		if (vec[2] == 1 && movimientoAnterior != ABAJO) {
			aux1.mover(ARRIBA);
			pesos[2] = aux1.calcularPeso();
			aux1.mover(ABAJO);
			// aux1.mat=this.mat;
		}
		if (vec[3] == 1 && movimientoAnterior != ARRIBA) {
			aux1.mover(ABAJO);
			pesos[3] = aux1.calcularPeso();
			aux1.mover(ARRIBA);
			// aux1.mat=this.mat;
		}

		for (int i = 0; i < 4; i++) {
			if (pesos[i] < mejor && pesos[i] >= 0) {
				mejor = pesos[i];
				if (i == 0)
					mejorMov = IZQUIERDA;
				else if (i == 1)
					mejorMov = DERECHA;
				else if (i == 2)
					mejorMov = ARRIBA;
				else if (i == 3)
					mejorMov = ABAJO;
			}
		}

		return mejorMov;
	}

	public boolean resolver() throws IOException {

		PrintWriter w = new PrintWriter(new FileWriter("Movimientos.out"));
		PrintWriter w2 = new PrintWriter(new FileWriter("MovimientosVisual.out"));
		int posPerIDArAb[] = this.conocerPosicionesPermitidas();
		int movimientoAnterior = 0;
		int i = 0;
		boolean res;
		if (res = this.evaluarPuzzle()) {
			while (this.peso != 0 && i < 500) {
				w.println(this.mejorMovimiento(posPerIDArAb, movimientoAnterior) + "\n");
				this.grabarPuzzle(w2);
				movimientoAnterior = this.mover(this.mejorMovimiento(posPerIDArAb, movimientoAnterior));

				posPerIDArAb = this.conocerPosicionesPermitidas();
				i++;
			}

		} else {
			w.println("No tiene solucion");
			System.out.println("No tiene solucion");
		}
		w2.println("Cantidad de pasos = " + i);
		w.close();
		w2.close();
		return res;
	}

	public static void main(String[] args) throws IOException {

		int mat[][] = new int[][] { { 8, 7, 6 }, { 5, 1, 4 }, { 2, 3, 0 } };
		RompecabezasDeOcho puzz = new RompecabezasDeOcho(mat);
		System.out.println(puzz.getPeso());
		
		if (puzz.resolver()) {
			
			puzz.mostrar();
			System.out.println(puzz.peso);
		}
	}

}
