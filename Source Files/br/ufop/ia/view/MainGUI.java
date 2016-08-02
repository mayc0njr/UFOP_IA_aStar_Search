/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufop.ia.view;

import java.awt.Container;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Classe da Interface Gráfica, janela principal contendo os outros componentes da aplicação.
 * @author Maycon Miranda, Davidson Nunes
 */
public class MainGUI extends javax.swing.JFrame {


	private String infoMap;
	private String infoCasas;

	MapaPanel mapa;
	DragonPanel dragoes;
	HousePanel casas;
	javax.swing.Icon infoIcon = new javax.swing.ImageIcon(getClass().getResource("/br/ufop/ia/view/icons/info.png"));
	InfoPanel infos;
	HomePanel home;
	int atualPanel, x, y;
	/**
	 * Construtor padrão, inicializando os componentes.
	 */
	public MainGUI() {
        super(".: DRACARYS!!! :.");
		initComponents();
		x = painelCanvas.getWidth();
		y = painelCanvas.getHeight();
		dragoes = new DragonPanel(x, y);
		casas = new HousePanel(x, y);
		home = new HomePanel(x, y);
		replacePanel(home);
		atualPanel=5;
        this.setLocationRelativeTo(null);
		java.awt.EventQueue.invokeLater(new Runnable(){
			public void run(){
				mapa = new MapaPanel(x, y);
			}
		});
	}
	
	/**
	 * Usado pelo MapaPanel para ativar o botão de informação após a execução do algoritmo.
	 */
	public void ativarBotao(){
		infoButton.setEnabled(true);
		infos = new InfoPanel(x, y, infoMap, infoCasas);
		JOptionPane.showMessageDialog(null, "Trajeto Executado! Clique no botão \"Info\", para mais detalhes.", "Game of Thrones", JOptionPane.INFORMATION_MESSAGE, infoIcon);
		
	}
	
	/**
	 * Função utilizada para realizar a troca do conteúdo dos painéis, ao clicar no botão.
	 * @param painel Novo painel a ser colocado.
	 */
	private void replacePanel(JPanel painel) {
		Container contain = painelCanvas;
		contain.removeAll();
		contain.add(painel);
		painel.validate();
		validate();
		repaint();
    }

	/**
	 * Obtem as informações do A* utilizado para encontrar o trajeto no mapa.
	 * @return String contendo dados de execução do algorítmo.
	 */
	public String getInfoMap(){
		return infoMap;
	}
	
	/**
	 * Obtém as informações do A* utilizado para resolver o problema das casas.
	 * @return String contendo dados de execução do algorítmo.
	 */
	public String getInfoCasas(){
		return infoCasas;
	}
	
	public void setInfoMap(String infoMap){
		this.infoMap = infoMap;
	}
	
	public void setInfoCasas(String infoCasas){
		this.infoCasas = infoCasas;
	}
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold default state="collapsed" desc="GeneratedCode">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        infoButton = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        painelCanvas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(930, 710));
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Menu Principal", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufop/ia/view/icons/map.png"))); // NOI18N
        jButton1.setText("Mapa");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufop/ia/view/icons/dragons.png"))); // NOI18N
        jButton3.setText("Dragões");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufop/ia/view/icons/houses.png"))); // NOI18N
        jButton2.setText("Casas");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        infoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufop/ia/view/icons/info.png"))); // NOI18N
        infoButton.setText("Info");
        infoButton.setEnabled(false);
        infoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoButtonActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufop/ia/view/icons/home.png"))); // NOI18N
        jButton4.setText("Home");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufop/ia/view/icons/help.png"))); // NOI18N
        jButton5.setText("Ajuda");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(infoButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(infoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        painelCanvas.setMinimumSize(new java.awt.Dimension(640, 640));
        painelCanvas.setPreferredSize(new java.awt.Dimension(750, 640));

        javax.swing.GroupLayout painelCanvasLayout = new javax.swing.GroupLayout(painelCanvas);
        painelCanvas.setLayout(painelCanvasLayout);
        painelCanvasLayout.setHorizontalGroup(
            painelCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 705, Short.MAX_VALUE)
        );
        painelCanvasLayout.setVerticalGroup(
            painelCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufop/ia/view/icons/targaryen3.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufop/ia/view/icons/gotMini.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(painelCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(painelCanvas, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(atualPanel == 1)
			return;
		replacePanel(mapa);
		atualPanel=1;
    }//GEN-LAST: event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(atualPanel == 2)
			return;
		replacePanel(dragoes);
		atualPanel=2;
    }//GEN-LAST: event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(atualPanel == 3)
			return;
		replacePanel(casas);
		atualPanel=3;
    }//GEN-LAST: event_jButton2ActionPerformed

    private void infoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoButtonActionPerformed
        if(atualPanel == 4)
			return;
		replacePanel(infos);
		atualPanel=4;
    }//GEN-LAST: event_infoButtonActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(atualPanel == 5)
			return;
		replacePanel(home);
		atualPanel=5;
    }//GEN-LAST: event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        JOptionPane.showMessageDialog(null, "Clique no botão \"Dragões\", caso deseja alterar o nivel de poder dos dragões."
									+ "\nClique no botão \"Casas\", caso deseje alterar a dificuldade das casas."
									+ "\nClique no botão \"Mapa\" Quando estiver pronto para rodar o algorítmo."
									+ "\nO botão \"Info\" é liberado após o trajeto terminar, exibindo dados da execução."
									+ "\nO botão \"Home\" retorna para a tela inicial. ", "Game of Thrones", JOptionPane.INFORMATION_MESSAGE, infoIcon);
    }//GEN-LAST: event_jButton5ActionPerformed

	/**
	 * @param args the command line arguments
	 */



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton infoButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel painelCanvas;
    // End of variables declaration//GEN-END:variables
}
