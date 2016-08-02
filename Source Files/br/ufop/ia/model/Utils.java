/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufop.ia.model;

import br.ufop.ia.view.Cenario.TERRENO;

/**
 * Classe com funções utilitárias.
 * @author Maycon Miranda, Davidson Nunes
 */
public final class Utils {
	//Constantes referente ao peso dos terrenos.
	public static final int RES_TERRA=1;
	public static final int RES_AGUA=10;
	public static final int RES_GELO=176400;
	public static final int RES_GRAMA=5;
	public static final int RES_CONCRETO=100;
	public static final int RES_MURO=176400;
	public static final int INFINITO = 268435456;
	public static final String SEP = System.getProperty("file.separator");
	
	/**
	 * Transforma um número em um double com duas casas de precisão.
	 * @param number Double para fazer a conversão.
	 * @return Double com apenas duas casas decimais.
	 */
	public static double duasCasas(double number){
		double retorno = (int)(number*100);
		return retorno/100;
	}
	/**
	 * Obtém qual o peso de determinado terreno.
	 * @param t Terreno para fazer a conversão.
	 * @return Inteiro contendo o peso do terreno.
	 */
	public static int getPeso(TERRENO t){
		if(t == null)
			return 0;
		switch(t){
			case TERRA:
				return RES_TERRA;
			case AGUA:
				return RES_AGUA;
			case GELO:
				return RES_GELO;
			case GRAMA:
				return RES_GRAMA;
			case CONCRETO:
				return RES_CONCRETO;
			case MURO:
				return RES_MURO;
			default:
				return 0;
		}
	}
	
}
