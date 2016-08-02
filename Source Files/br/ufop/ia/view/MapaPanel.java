/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufop.ia.view;

import br.ufop.ia.model.Solucao;
import br.ufop.ia.model.Utils;
import br.ufop.ia.view.Cenario.mmap;
import got_01.GoT_01;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *	Classe de Interface gráfica, responsável por mostrar e atualizar os dados de execução no painel.
 * @author Maycon Miranda, Davidson Nunes
 */
public class MapaPanel extends BGPanel {

	Cenario c;
	boolean playing=false;
	int passos = 0, custoTotal = 0, matualX = 0, matualY = 0, mdrogon = 3, mrhaegal = 3, mviserion = 3;
	String derrotadas = "";
	double totalValue=0;
	int tempo = 200;
	Timer timer;
	private int cont=0;
	ActionListener listener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			nextStep();
		}
	};

	/**
	 * Construtor incializa os compenentes, carrega o mapa escolhido e inicia as heuristicas.
	 * Creates new form Mapa
	 */
	public MapaPanel() {
		String path = "";
		initComponents();
		JOptionPane.showMessageDialog(this, "Bem vindo a Westeros!\nPor favor, selecione o seu mapa na para iniciar.\nOu clique em \"Cancelar\", para desistir.", "Game of Thrones", JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/br/ufop/ia/view/icons/dragons.png")));
		FileDialog loadMap = new FileDialog(GoT_01.fm, "Selecione o mapa");
		loadMap.setAlwaysOnTop(true);
		loadMap.setFile("*.txt");
		loadMap.setMode(FileDialog.LOAD);
        loadMap.setLocationRelativeTo(this);
		boolean stay = true;
		while(stay){
			String dir="", arq="";
			try{
				loadMap.setVisible(true);
				c = new Cenario(42, 42, 15);
				dir = loadMap.getDirectory();
				arq = loadMap.getFile();
				path = dir + Utils.SEP + arq;
				c.carregarMapa(path);
				stay = false;
				JOptionPane.showMessageDialog(null, "Mapa Carregado!", "Game of Thrones", JOptionPane.ERROR_MESSAGE, GoT_01.fm.infoIcon);
			}catch(IllegalArgumentException e){
				if(dir==null || dir.equals("null") || arq==null || arq.equals("null") || dir.isEmpty() || arq.isEmpty())
					System.exit(0);
				JOptionPane.showMessageDialog(null, "Mapa não encontrado. Por favor, selecione um arquivo de mapa válido, ou selecione Cancelar, para sair da aplicação.  ", "Game of Thrones", JOptionPane.ERROR_MESSAGE);
			}
		}
		c.distanciaManhatanTodos();
		c.buscaAstarAll();

		repaint();
	}
	
	/**
	 * Chama o construtor padrão e em seguida seta o tamanho e posição da janela.
	 * @param width Largura da janela.
	 * @param height Altura da Janela.
	 */
	public MapaPanel(int width, int height) {
		this();
		setBounds(0, 0, width, height);
	}
	
	/**
	 * Atualiza as caixas de texto ta interface gráfica de acordo com o ponto em execução,
	 * aumentando o numero de passos, o custo total, e a posição correspondente.
	 * Chama as outras funções de update.
	 * @param ponto Ponto que deve ser atualizado.
	 */
	private void atualizaGUI(mmap ponto) {
		passos++;
		custoTotal += ponto.peso;
		matualX = ponto.x;
		matualY = ponto.y;
		atualizaCasas(ponto);
		atualizaLabels();
	}
	
	/**
	 * Chamado ao fim do trajeto mostrado, libera o painel/botão de informações,
	 * E insere nele todos os dados da execução do algoritmo, como tempo de execução,
	 * e valores de custo relacionados à solução encontrada.
	 */
	private void generateMapAndHouseInfos(){
		StringBuffer infoMap = new StringBuffer();
		infoMap.append("Informações Mapa:\n");
		infoMap.append("Ordem visita:");
		String aux;
		StringBuffer infoCasas = new StringBuffer();
		infoCasas.append("Informações Casa:\n");
		infoCasas.append("Custos Casas:\n");
		infoCasas.append("Braavos:\t" + Solucao.BRAAVOS+"\n");
		infoCasas.append("Highgarden:\t" + Solucao.HIGHGARDEN+"\n");
		infoCasas.append("Casterly Rock:\t" + Solucao.CASTERLY_ROCK+"\n");
		infoCasas.append("Wintefell:\t" + Solucao.WINTERFELL+"\n");
		infoCasas.append("King's Landing:\t" + Solucao.KINGSLANDING+"\n\n");
		infoCasas.append("Poder Dragões:\n");
		infoCasas.append("Drogon:\t" + Solucao.DROGON+"\n");
		infoCasas.append("Rhaegal:\t" + Solucao.RHAEGAL+"\n");
		infoCasas.append("Viserion:\t"+ Solucao.VISERION+"\n\n");
		infoCasas.append("Solução Encontrada:\n");
		aux = destroyed.getText();
		infoMap.append(aux.substring(0, aux.length()-1) + "\n");
		infoMap.append("Custo total:\t" + custoTotal + "\n");
		infoMap.append("Passos:\t" + passos + "\n");
		infoMap.append("Tempo A*:\t" + c.aStar_time + "ms.");
		infoCasas.append(Solucao.best+"\n");
		infoCasas.append(Solucao.best.custoHeuristico()+"\n");
		infoCasas.append("Tempo A*:\t" + Solucao.best.tempoExec + "ms.");
		GoT_01.fm.setInfoMap(infoMap.toString());
		GoT_01.fm.setInfoCasas(infoCasas.toString());
	
	}
	
	/**
	 * Verifica se o ponto recebido é uma "Casa" e se for, atualiza seus dados.
	 * Como, casas derrotadas, tempo gasto em batalhas, dragões usados, etc.
	 * @param ponto Ponto que deve ser verificado para atualização.
	 */
	private void atualizaCasas(mmap ponto) {
		int x = c.isHouse(ponto);
		if (x >= 0 && !ponto.destroyed) {
			destroyed.setText(destroyed.getText() + ponto.letra + " ");
			mdrogon -= Solucao.best.vx[x - 1][2];
			drogon.setText(mdrogon + "");
			mrhaegal -= Solucao.best.vx[x - 1][1];
			rhaegal.setText(mrhaegal + "");
			mviserion -= Solucao.best.vx[x - 1][0];
			viserion.setText(mviserion + "");
			totalValue += Solucao.best.custoLinha(x-1);
			double total = Utils.duasCasas(totalValue);
			tempoCombate.setText(""+total);
			ponto.destroyed=true;
		}
	}
	
	/**
	 * Atualiza as labels da GUI de acordo com os novos valores.
	 */
	private void atualizaLabels() {
		passosC.setText("" + passos);
		custoT.setText("" + custoTotal);
		drogon.setText("" + mdrogon);
		rhaegal.setText("" + mrhaegal);
		viserion.setText("" + mviserion);
		atualX.setText("" + matualX);
		atualY.setText("" + matualY);
	}
	
	/**
	 * Invoca o próximo passo, ou seja, o próximo ponto a ser atualizado.
	 */
	private void nextStep() {
		if (cont < c.rota.size()) {
			mmap ponto = c.rota.get(cont);
			cont++;
			atualizaGUI(ponto);
			ponto.cor = ponto.cor.darker();
			repaint();
			timer.setDelay(tempo);
		} else {
			timer.stop();
			generateMapAndHouseInfos();
			GoT_01.fm.ativarBotao();
		}
	}
	
	/**
	 * Inicia o processo de mostrar o trajeto encontrado na Interface Gráfica.
	 */
	public void correrMapa() {
		Solucao.best = Solucao.aStar();
		timer = new Timer(tempo, listener);
		timer.setRepeats(true);
		timer.start();
	}
	
	/**
	 * Sobre-escrita do método para pintar o componente,
	 * Modificado para realizar a impressão correta do mapa na GUI.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int offset = controles.getWidth() + 10;
		Color corx = g.getColor();
		int x = 0, y = 0, t = c.getSize();
		int ny = 0, nx = 0;
		y = 5;
		x = offset + 5;
		mmap pnt;
		for (int i = 0; i < c.getLinhas(); i++) {
			for (int j = 0; j < c.getColunas(); j++) {
				pnt = c.getGround()[i][j];
				g2.setPaint(pnt.cor);
				g2.fill(new Rectangle2D.Double(x + 1, y + 1, t - 1, t - 1));
				if (c.isHouse(pnt) >= 0) {
					g2.setPaint(Color.WHITE);
					g2.drawString(pnt.letra + "", x + 3, y + 13);
				}
				g2.setPaint(corx);
				x += t;
				if (x > nx) {
					nx = x;
				}
			}
			y += t;
			if (y > ny) {
				ny = y;
			}
			x = offset + 5;
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold default state="collapsed" desc="GeneratedCode">//GEN-BEGIN:initComponents
    private void initComponents() {

        controles = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        passosC = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        custoT = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        atualX = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        tempoSlot = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        destroyed = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        drogon = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        rhaegal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        viserion = new javax.swing.JTextField();
        atualY = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tempoCombate = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        setPreferredSize(new java.awt.Dimension(705, 640));

        controles.setOpaque(false);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Passos");
        jLabel1.setAlignmentX(0.5F);

        passosC.setEditable(false);
        passosC.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        passosC.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passosC.setText("0");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Custo Total");

        custoT.setEditable(false);
        custoT.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        custoT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        custoT.setText("0");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Posição Atual");
        jLabel3.setAlignmentX(0.5F);

        atualX.setEditable(false);
        atualX.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        atualX.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        atualX.setText("0");

        jButton1.setText("Play");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tempoSlot.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tempoSlot.setText("200");
        tempoSlot.setToolTipText("Tempo em milissegundos para prosseguir o trajeto.");
        tempoSlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempoSlotActionPerformed(evt);
            }
        });

        jButton3.setText("Tempo");
        jButton3.setToolTipText("Tempo em milissegundos para prosseguir o trajeto.");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Casas Destruidas");

        destroyed.setEditable(false);
        destroyed.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        destroyed.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("1 - Drogon");

        drogon.setEditable(false);
        drogon.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        drogon.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        drogon.setText("3");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("2 - Rhaegal");

        rhaegal.setEditable(false);
        rhaegal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rhaegal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        rhaegal.setText("3");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("3 - Viserion");

        viserion.setEditable(false);
        viserion.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        viserion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        viserion.setText("3");

        atualY.setEditable(false);
        atualY.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        atualY.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        atualY.setText("0");

        jLabel9.setText("Tempo Batalhas");

        tempoCombate.setEditable(false);
        tempoCombate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tempoCombate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tempoCombate.setText("0");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("VIDAS");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("TRAJETO");

        javax.swing.GroupLayout controlesLayout = new javax.swing.GroupLayout(controles);
        controles.setLayout(controlesLayout);
        controlesLayout.setHorizontalGroup(
            controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(controlesLayout.createSequentialGroup()
                .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(custoT, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(controlesLayout.createSequentialGroup()
                            .addComponent(atualX, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(atualY, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tempoSlot, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(destroyed)
                    .addComponent(rhaegal, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(drogon, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viserion)
                    .addComponent(tempoCombate, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(passosC)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        controlesLayout.setVerticalGroup(
            controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passosC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(custoT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(atualX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(atualY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(destroyed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(drogon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rhaegal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viserion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tempoCombate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tempoSlot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(controles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(603, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(controles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor- fold>//GEN-END:initComponents
	
	
	private void alteraTempo(){
		String x = tempoSlot.getText();
		try {
			tempo = Integer.parseInt(x);
		} catch (RuntimeException e) {
		}
	}
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		alteraTempo();
    }//GEN-LAST: event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		if(playing){
			JOptionPane.showMessageDialog(null, "O trajeto ja foi iniciado!", "Game of Thrones", JOptionPane.ERROR_MESSAGE);
			return;
		}
		playing=true;
		correrMapa();
    }//GEN-LAST: event_jButton1ActionPerformed

    private void tempoSlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempoSlotActionPerformed
        alteraTempo();
    }//GEN-LAST: event_tempoSlotActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField atualX;
    private javax.swing.JTextField atualY;
    private javax.swing.JPanel controles;
    private javax.swing.JTextField custoT;
    private javax.swing.JTextField destroyed;
    private javax.swing.JTextField drogon;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField passosC;
    private javax.swing.JTextField rhaegal;
    private javax.swing.JTextField tempoCombate;
    private javax.swing.JTextField tempoSlot;
    private javax.swing.JTextField viserion;
    // End of variables declaration//GEN-END:variables
}
