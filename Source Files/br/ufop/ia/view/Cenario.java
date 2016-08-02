/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufop.ia.view;

import br.ufop.ia.model.Solucao;
import br.ufop.ia.model.Utils;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author davidson
 */
public class Cenario {
	// Constantes que indicam a posicao de cada casa no vetor "CASAS"
	public final int DAENERYS=0;
	public final int BRAAVOS=1;
	public final int HIGH_GARDEN=2;
	public final int CASTERLY_ROCK=3;
	public final int KINGS_LANDING=4;
	public final int WINTERFELL=5;
	public final int FIM=6;
	public final int NUMERO_CASAS = 7;
	public boolean grid = false;
	public enum TERRENO {
		AGUA,
		TERRA,
		GELO,
		GRAMA,
		CONCRETO,
		MURO,
		BRAAVOS,
		HIGHGARDEN,
		CASTERLY_ROCK,
		KINGS_LANDING,
		WINTERFELL,
		DAENERYS,
		FIM;
	}

	public class mmap implements Comparable<mmap> {

		public TERRENO t;
		public Color cor;
		public mmap anterior[] = new mmap[NUMERO_CASAS];
		public int peso;
		public char letra;
		public int restante;
		public int heuristica[] = new int[NUMERO_CASAS];
		public int distancia[] = new int[NUMERO_CASAS];
		public boolean inundado = false;
		public int x, y;
		public boolean destroyed = false;
		public boolean equals(mmap m){ // Verifica se um ponto é igual ao outro.
			if(super.equals(m))
				return true;
			
			if(this.x == m.x)
				return this.y == m.y;
			return false;
		}
		public void setTerreno(TERRENO tt) {// Seta o peso e o terreno para o ponto.
			this.t = tt;
			peso = Utils.getPeso(tt);
		}

		public int compareTo(mmap m) { //Compara dois pontos usando como parametro distancia atual + heuristica.
			return (distancia[casaUsada]+heuristica[casaUsada]) - (m.distancia[casaUsada]+m.heuristica[casaUsada]);
		}

		public String toString() {
			return "[" + x + "]" + "[" + y + "]" ;
		}
	}

	private mmap[][] ground;
	private mmap[] casas;
	private int size;
	private int linhas, colunas;
	private int[] order;
	public static int casaUsada;
	public List<mmap> rota;
	long aStar_time;

	public Cenario(int linhas, int colunas, int tamanho) {
		this.size = tamanho;
		this.linhas = linhas;
		this.colunas = colunas;
		init();
	}

	private void init() {//Inicializa as matrizes e vetores.
		this.ground = new mmap[linhas][colunas];
		this.casas = new mmap[NUMERO_CASAS];
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				ground[i][j] = new mmap();
			}
		}
	}

	public void carregarMapa(String m) { //Le o arquivo de texto e carrega o mapa.
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File(m)));
			String linha;
			char c;
			Color DARK_GREEN = new Color(127,191,95);
			Color MURALHA = new Color(32,48,64);
			Color CONCRETO = new Color(128,144,160);
			Color FAKE_WHITE = Color.white;
			Color DARK_BLUE = new Color(63,95,191);
			Color MARROM = new Color(151,109,63);
			int lin = 0;
			while ((linha = bf.readLine()) != null) {
				for (int i = 0; i < colunas; i++) {
					c = linha.charAt(i);
					ground[lin][i].x = lin;
					ground[lin][i].y = i;
					switch (c) {
						case 'A':
							ground[lin][i].setTerreno(TERRENO.AGUA);
							ground[lin][i].cor = DARK_BLUE;
							ground[lin][i].letra = ' ';
							break;
						case 'M':
							ground[lin][i].setTerreno(TERRENO.MURO);
							ground[lin][i].cor = MURALHA;
							ground[lin][i].letra = ' ';
							break;
						case 'P':
							ground[lin][i].setTerreno(TERRENO.CONCRETO);
							ground[lin][i].cor = CONCRETO;
							ground[lin][i].letra = ' ';
							break;
						case 'T':
							ground[lin][i].setTerreno(TERRENO.TERRA);
							ground[lin][i].cor = MARROM;
							ground[lin][i].letra = ' ';
							break;
						case 'G':
							ground[lin][i].setTerreno(TERRENO.GRAMA);
							ground[lin][i].cor = DARK_GREEN;
							break;
						case 'I':
							ground[lin][i].setTerreno(TERRENO.GELO);
							ground[lin][i].cor = FAKE_WHITE;
							ground[lin][i].letra = ' ';
							break;
						case 'K':
							ground[lin][i].setTerreno(TERRENO.KINGS_LANDING);
							ground[lin][i].cor = Color.BLACK;
							casas[KINGS_LANDING] = ground[lin][i];
							ground[lin][i].letra = 'K';
							break;
						case 'B':
							ground[lin][i].setTerreno(TERRENO.BRAAVOS);
							ground[lin][i].cor = Color.BLACK;
							casas[BRAAVOS] = ground[lin][i];
							ground[lin][i].letra = 'B';
							break;
						case 'C':
							ground[lin][i].setTerreno(TERRENO.CASTERLY_ROCK);
							ground[lin][i].cor = Color.BLACK;
							casas[CASTERLY_ROCK] = ground[lin][i];
							ground[lin][i].letra = 'C';
							break;
						case 'H':
							ground[lin][i].setTerreno(TERRENO.HIGHGARDEN);
							ground[lin][i].cor = Color.BLACK;
							casas[HIGH_GARDEN] = ground[lin][i];
							ground[lin][i].letra = 'H';
							break;
						case 'W':
							ground[lin][i].setTerreno(TERRENO.WINTERFELL);
							ground[lin][i].cor = Color.BLACK;
							casas[WINTERFELL] = ground[lin][i];
							ground[lin][i].letra = 'W';
							break;
						case 'F':
							ground[lin][i].setTerreno(TERRENO.FIM);
							ground[lin][i].cor = Color.ORANGE;
							casas[FIM] = ground[lin][i];
							ground[lin][i].letra = ' ';
							break;
						case 'D':
							ground[lin][i].setTerreno(TERRENO.DAENERYS);
							ground[lin][i].cor = Color.RED;
							casas[DAENERYS] = ground[lin][i];
							ground[lin][i].letra = ' ';
							break;
						default:
							ground[lin][i].setTerreno(TERRENO.DAENERYS);
							ground[lin][i].cor = Color.CYAN;
							break;

					}
				}
				lin++;
			}
			bf.close();
		} catch (FileNotFoundException e){
			throw new IllegalArgumentException();
		} catch (RuntimeException e){
			throw new IllegalArgumentException();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	//Verifica se o ponto é uma casa. Como Daenerys e o Fim não são casas,
	//Mas estao no vetor de casas, o vetor começa da posicao[1] e termina na posição [6].
	public int isHouse(mmap ponto){
		for(int x=1 ; x < casas.length-1 ; x++){
			if(ponto.equals(casas[x])){
				return x;
			}
		}
		return -1;
	}//Retorna o mapa.
	public mmap[][] getGround() {
		return ground;
	}

	//Seta um mapa.
	public void setGround(mmap[][] ground) {
		this.ground = ground;
	}

	//Retorna o tamanho visual de cada ponto.
	public int getSize() {
		return size;
	}
	//Seta o tamanho visual de cada ponto.
	public void setSize(int size) {
		this.size = size;
	}

	public int getLinhas() {
		return linhas;
	}

	public void setLinhas(int linhas) {
		this.linhas = linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public void setColunas(int colunas) {
		this.colunas = colunas;
	}
	//Obtem todos os vizinhos de um ponto especifico do mapa.
	List<mmap> getVizinhos(mmap ponto){
		mmap aux;
		List<mmap> bordas = new ArrayList<>();
		if (ponto.y > 0) {
			aux = ground[ponto.x][ponto.y - 1];
				bordas.add(aux);
		}
		if (ponto.x < ground.length - 1) {
			aux = ground[ponto.x + 1][ponto.y];
				bordas.add(aux);
		}
		if (ponto.y < ground[0].length - 1) {
			aux = ground[ponto.x][ponto.y + 1];
				bordas.add(aux);
		}
		if (ponto.x > 0) {
			aux = ground[ponto.x - 1][ponto.y];
				bordas.add(aux);
		}
		return bordas;
	}
	//Obtem todos os vizinhos de mapa que ainda nao foram encontrados.
	List<mmap> getBordasFlood(mmap ponto, int casa) {

		mmap aux;
		List<mmap> bordas = new ArrayList<>();
		if (ponto.y > 0) {
			aux = ground[ponto.x][ponto.y - 1];
			if (!aux.inundado) {
				bordas.add(aux);
			}
		}
		if (ponto.x < ground.length - 1) {
			aux = ground[ponto.x + 1][ponto.y];
			if (!aux.inundado) {
				bordas.add(aux);
			}
		}
		if (ponto.y < ground[0].length - 1) {
			aux = ground[ponto.x][ponto.y + 1];
			if (!aux.inundado) {
				bordas.add(aux);
			}
		}
		if (ponto.x > 0) {
			aux = ground[ponto.x - 1][ponto.y];
			if (!aux.inundado) {
				bordas.add(aux);
			}
		}
		for (mmap coord : bordas) {
				coord.anterior[casa] = ponto;
				coord.heuristica[casa] = ponto.heuristica[casa] + ponto.peso;
				coord.inundado = true;
		}
		Collections.sort(bordas);
		return bordas;
	}
	// Realiza uma busca em força bruta. -- nao utilizado.
	public void floodSearchAll(){
		for(int x=0 ; x < casas.length ; x++){
			floodSearch(x);
		}
	}
	// Dado um um array e um elemento, retorna sua posicao.
	public int indexOf(int[] ar, int el){
		for(int x=0 ; x < ar.length ; x++){
			if(ar[x]==el)
				return x;
		}
		return -1;
	}
	// Retorna o primeiro elemento do array ainda diferente to recebido que ainda nao tenha sido inundado.
	public int firstNotFlooded(mmap[] ar, mmap el){
		for(int x=0 ; x < ar.length ; x++){
			if(!ar[x].inundado && !ar[x].equals(el))
				return x;
		}
		
		return -1;
	}
	// Realiza uma busca Gulosa para traçar uma rota entre as casas.
	public int gulosaCasa(mmap origem){
		casas[0].inundado = true;
		int x = firstNotFlooded(casas, origem);
		for(int z=1 ; z < origem.distancia.length-1 ; z++){
			if(casas[z].inundado || casas[z].equals(origem))
				continue;
			if(origem.heuristica[z] < origem.heuristica[x])
				x=z;
		}
		return x;
	}
	//Realiza uma busca A* para encontrar a melhor rota do começo ao fim.
	public void buscaAstarAll(){
		restart();
		distanciaManhatanTodos();
		order = new int[NUMERO_CASAS-1];
		int y=0;
		for(int x=0 ; x < order.length-1 ; x++){
			order[y] = gulosaCasa(casas[y]);
			casas[order[y]].inundado = true;
			y = order[y];
		}
		order[y]=6;
		long y2 = new Date().getTime();
		for(int x=0 ; x < order.length ; x++){
			aStarSearch(casas[order[x]], x);
		}
		long y3 = new Date().getTime();
		aStar_time = y3-y2;
		rota = new ArrayList<>();
		int obj = 0;
		for(int x=order.length-1 ; x >= 0 ; x--){
			mmap aux = casas[obj];
			while(aux.anterior[obj] != null){
				rota.add(aux.anterior[obj]);
				aux = aux.anterior[obj];
			}
			obj = order[obj];
			if(obj == -1)
				break;
		}
	}
	//Recebe a distancia-Manhatan entre dois pontos.
	public int getDistManhatan(mmap origem, mmap destino){
		return Math.abs(origem.x - destino.x) + Math.abs(origem.y - destino.y);
	}
	//Seta a distancia Manhatan de todas os pontos do mapa para a casa "[FIM]".
	public void distanciaManhatanTodos(){
		mmap atual;
		for(int x=0 ; x < ground.length; x++){
			for(int y=0 ; y < ground[x].length ; y++){
				for(int z=0 ; z < casas.length ; z++){
					atual = ground[x][y];
					atual.heuristica[z] = getDistManhatan(atual, casas[z]);
				}
			}
		}
	}
	//Reinicia a heuristica, distancia, peso, e tudo mais.
	public void restart(){
		try{
			for(int x=0 ; x < ground.length ; x++){
				for(int y=0 ; y < ground[x].length ; y++){
					for(int z=0 ; z < casas.length ; z++){
						ground[x][y].anterior[z] = null;
						ground[x][y].heuristica[z] = Utils.INFINITO;
						ground[x][y].distancia[z] = Utils.INFINITO;
						ground[x][y].restante = ground[x][y].peso;
					}
				}
			}
		}catch(NullPointerException e){
			e.printStackTrace();
			System.exit(1);
		}

	}
	//Realiza a busca A* de um ponto do mapa, para uma casa.
	public void aStarSearch(mmap origem, int casa){
		casaUsada=casa;
		mmap atual;
		mmap objetivo = casas[casa];
		int novaDistancia;
		PriorityQueue<mmap> restantes = new PriorityQueue<>(); //frontier = PriorityQueue()
		restantes.add(origem); //frontier.put(start, 0)
		origem.anterior[casa]=null; //came_from = {}  - came_from[start] = None
		origem.distancia[casa] = 0; //cost_so_far = {} - cost_so_far[start] = 0
		while(!restantes.isEmpty()){ //while not frontier.empty():
			atual = restantes.poll(); //  current = frontier.get()
			if(atual.equals(objetivo)) //   if current == goal: - break
				break;
			for(mmap prox : getVizinhos(atual)){ //   for next in graph.neighbors(current):
				novaDistancia = atual.distancia[casa] + prox.peso; // new_cost = cost_so_far[current] + graph.cost(current, next)
				if(prox.distancia[casa] >= Utils.INFINITO || novaDistancia < prox.distancia[casa]){ //if next not in cost_so_far or new_cost < cost_so_far[next]:
					prox.distancia[casa] = novaDistancia; //	cost_so_far[next] = new_cost
					restantes.add(prox); //priority = new_cost + heuristic(goal, next) - frontier.put(next, priority)
					prox.anterior[casa] = atual; // came_from[next] = current
				}
			}
		}
	}
	//Faz uma busca de força bruta de uma casa até o FIM.
	public void floodSearch(int casa) {
		mmap origem = casas[casa];
		for (mmap[] ground1 : ground) {
			for (mmap item : ground1) {
				item.inundado = false;
				item.peso = Utils.getPeso(item.t);
				item.restante = item.peso;
			}
		}
		List<mmap> pontos = new LinkedList<>();
		mmap atual;
		origem.anterior[casa] = null;
		origem.heuristica[casa] = 0;
		origem.inundado = true;
		pontos.add(origem);
		while (!pontos.isEmpty()) {
			atual = pontos.remove(0);

			if (atual.restante > 0) {
				atual.restante--;
				pontos.add(atual);
			} else {
				pontos.addAll(getBordasFlood(atual, casa));
			}

		}
		
	}
}
