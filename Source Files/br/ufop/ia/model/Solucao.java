/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufop.ia.model;

import static br.ufop.ia.view.Cenario.casaUsada;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author davidson
 */
public class Solucao implements Comparable<Solucao>{

    public static double DROGON = 1.5;
    public static double RHAEGAL = 1.3;
    public static double VISERION = 1.1;
	public static int BRAAVOS=60;
	public static int HIGHGARDEN=70;
	public static int CASTERLY_ROCK=80;
	public static int WINTERFELL=90;
	public static int KINGSLANDING=100;
    public static double REINO = 400;
	public static Solucao best;
	public long tempoExec;
	public String houseNames[] = new String[]{"Braavos", "Highgarden", "Casterly Rock", "Winterfell", "King's Landing"};

    public static int DRAGOES = 3;
    public static int CASAS = 5;
    public double valor;
    public double vx[][] = new double[CASAS][DRAGOES];
    private double drags[][] = new double[DRAGOES][2];
    private int casas[] = new int[CASAS];
    private int zx[] = new int[DRAGOES * CASAS];
//    private int incremento;
	private Solucao anterior;
	private double distancia;
	public Solucao(int t){
        distancia = Utils.INFINITO;
        drags[0][0] = 3;//vidas
        drags[0][1] = VISERION; //poder
        drags[1][0] = 3;//vidas
        drags[1][1] = RHAEGAL; //poder
        drags[2][0] = 3;//vidas
        drags[2][1] = DROGON; //poder    
        casas[0] = BRAAVOS;
        casas[1] = HIGHGARDEN;
        casas[2] = CASTERLY_ROCK;
        casas[3] = WINTERFELL;
        casas[4] = KINGSLANDING;
	}
    public Solucao() {
        distancia = Utils.INFINITO;
        drags[0][0] = 3;//vidas
        drags[0][1] = VISERION; //poder
        drags[1][0] = 3;//vidas
        drags[1][1] = RHAEGAL; //poder
        drags[2][0] = 3;//vidas
        drags[2][1] = DROGON; //poder    
        casas[0] = BRAAVOS;
        casas[1] = HIGHGARDEN;
        casas[2] = CASTERLY_ROCK;
        casas[3] = WINTERFELL;
        casas[4] = KINGSLANDING;
    }

    /*
    Solu��o incrementada, gerada a partir de uma situa��o inicial com um incremento de n unidades
     */
    public Solucao(int inc, Solucao q) {
        distancia = Utils.INFINITO;
        drags[0][0] = 3;//vidas
        drags[0][1] = VISERION; //poder
        drags[1][0] = 3;//vidas
        drags[1][1] = RHAEGAL; //poder
        drags[2][0] = 3;//vidas
        drags[2][1] = DROGON; //poder    
        casas[0] = BRAAVOS;
        casas[1] = HIGHGARDEN;
        casas[2] = CASTERLY_ROCK;
        casas[3] = WINTERFELL;
        casas[4] = KINGSLANDING;
        for (int i = 0; i < vx.length; i++) {
            for (int j = 0; j < vx[0].length; j++) {
                vx[i][j] = q.vx[i][j];
            }
        }
		inc--;
        int linha = inc/ 3;
        int coluna = inc% 3;
        vx[linha][coluna] = 1;
    }
	
	public double custoLinha(int x){ //Retorna o tempo gasto para derrotar uma casa.
		double casa = casas[x];
		double totalDragoes=0;
			totalDragoes+= vx[x][0]*VISERION;
			totalDragoes+= vx[x][1]*RHAEGAL;
			totalDragoes+= vx[x][2]*DROGON;
		return casa/totalDragoes;
	}
    /**
     * A dist�ncia heur�stica � dada pelo. Quanto tempo estimamos gastar
     */
	private double getPoderRestante(){ // Retorna o quanto de poder restante de dragoes tem.
		double power = DROGON*3 + RHAEGAL*3 + VISERION*3;
		double pesoDragoes[] = new double[]{VISERION, RHAEGAL, DROGON};
		for(int x=0 ; x < CASAS ; x++){
			for(int y=0 ; y < DRAGOES ; y++){
				power-=(vx[x][y]*pesoDragoes[y]);
			}
		}
		return power;
	}
	public double custoHeuristico(){
		int totalCasasRestantes=0;
		int totalLinha=0;
		int dragRestantes[] = new int[]{3,3,3};
		int pesoCasas[] = new int[]{BRAAVOS, HIGHGARDEN, CASTERLY_ROCK, WINTERFELL, KINGSLANDING};
		for(int x=0 ; x < CASAS ; x++){
			for(int y=0 ; y < DRAGOES ; y++){
				totalLinha+=vx[x][y]; // Calcula o total de dragoes gastos.
				dragRestantes[y]-=vx[x][y]; // Remove as vidas dos dragoes usados.
			}
			if(totalLinha==0)
				totalCasasRestantes+=pesoCasas[x];
			else
				totalLinha=0;
		}
		if(dragRestantes[0] <= 0 || dragRestantes[1] <= 0 || dragRestantes[2] <= 0)
			return Utils.INFINITO; // Se nao houverem dragoes vivos, retorna um valor muito alto.
		dragRestantes[0] += dragRestantes[1] +dragRestantes[2];
		//Retorna Total de casas restantes divididos pela
		//Média ponderada da força restante dos dragões
		return (totalCasasRestantes*dragRestantes[0])/getPoderRestante(); 
	}

    /**
     * Quanto tempo ainda precisamos gastar para resolver o problema
     * @return 
     */
	
    public double custoReal() {
        double power = 0;
        double destruir = 0;
        int d[] = new int[3];
        double poderDragoes[] = new double[]{VISERION, RHAEGAL, DROGON};
        for(int x=0 ; x < CASAS ; x++){
        	for(int y=0 ; y < DRAGOES ; y++){
        		power += vx[x][y]*poderDragoes[y];
        		d[y]+=vx[x][y];
        	}
        	if(power>0)
        		destruir+=casas[x]/power;
        	else
        		destruir+=REINO;
        	power=0;
        }
        if(d[0] > 2 || d[1] > 2 || d[2] > 2)
        	return Utils.INFINITO;
        return destruir;
//        return 0;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
		double cl=0;
		s.append(" V | R | D | Casas\n");
        for (int i = 0; i < CASAS; i++) {
			s.append(" ");
            for (int j = 0; j < DRAGOES; j++) {
               s.append(String.valueOf((int) vx[i][j]) + " | ");
            }
			cl = Utils.duasCasas(custoLinha(i));
            s.append(houseNames[i]+"\t|  " + cl + "\n");
        }
		s.append("Custo total: " + Utils.duasCasas(custoReal()));
        return s.toString();
    }


    public boolean preenchido(int posicao) {
        posicao -= 1;
        int linha = posicao / 3;
        int coluna = posicao % 3;
        return vx[linha][coluna] == 1;
    }

    /**
     * A partir de uma solu��o inicial cria todas as poss�veis solu��es vizinhas
     *
     * @param s
     * @return
     */
    public ArrayList<Solucao> expandeNo() {
        ArrayList<Solucao> s = new ArrayList<>();
        for (int i = 1; i < 15; i++) {
            if (preenchido(i)) {
                continue;
            }
            Solucao q = new Solucao(i, this);
            s.add(q);
        }

        return s;
    }
	public boolean dragMortos(){
		boolean b=false;
		for(int x=0 ; x < 3 ; x++){
			if(drags[x][0]<=0)
				b=true;
		}
		return b;
	}
	public static Solucao aStar(){
		Solucao origem = new Solucao();
		Solucao atual=null;
		Solucao old=null; // Melhor solucao ate o momento.
		origem.distancia=0;
		double novaDistancia;
		PriorityQueue<Solucao> restantes = new PriorityQueue<>(); // Fila de soluções com a Heuristica.
		restantes.add(origem); // Lista de Solucoes encontradas.
		origem.anterior=null; 
		boolean end = false; // Se ja encontrou uma solucao valida.
		long inicio, fim; // Contador de tempo.
		inicio = new Date().getTime();
		while(!restantes.isEmpty()){
			atual = restantes.poll(); // Remove a "melhor" solucao encontrada ate o momento.
			if(end) // Verifica se já tem uma solucao completa.
				break;
			if(atual.solved()){ // Se a solucao atual for uma solucao válida, indica que já pode finalizar.
				old = atual; // Guarda a solução.
				end = true;
			}
			for(Solucao prox : atual.expandeNo()){ // Expande os vizinhos da solução atual.
				novaDistancia = atual.distancia + prox.custoReal(); // Calcula o "valor" da nova solução.
				if(!prox.dragMortos()){ // Se a solucao for válida (Não houver dragões mortos).
					if(prox.distancia >= Utils.INFINITO || novaDistancia < prox.distancia){ // Verifica se a solucao do vizinho é melhor que a atual.
						prox.distancia = novaDistancia; //Insere o valor na nova solucao.
						prox.anterior = atual;
						restantes.add(prox); // Insere a solução na lista de soluções encontradas.
					}
				}
			}
		}
		fim = new Date().getTime(); // Marca o fim do cronometro.
		atual.tempoExec = fim-inicio; // Calcula a duração do algoritmo.
		if(old.custoReal() > atual.custoReal() && atual.solved()) // Se a ultima solução valida for melhor que a penultima.
			return atual; // Retorna a ultima.
		return old; // Retorna a penultima.
	}
	
	public int compareTo(Solucao s){ //Compara duas solucoes atraves da soma do custoReal + custoHeuristico.
		return (int)Math.round((this.custoReal()+this.custoHeuristico())-(s.custoReal()+s.custoHeuristico()));
	}
	boolean solved(){ //Retorna se todas as casas foram derrotadas, e todos dragoes estao vivos.
		int total=0;
		for(int i=0 ; i < CASAS ; i++){
			for(int j=0 ; j < DRAGOES ; j++){
				if(vx[i][j] == 1){
					total++;
					break;
				}
			}
		}
		if(dragMortos())
			return false;
		return total >=5;
	}

}
