package cliente;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

/**
 *
 * @author Cleiton Neri
 */
public class ClienteGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel painelTexto, entradaPainel;
    private JTextField textField;
    private String nome, menssagem;
    private final Font meiryoFont = new Font("Meiryo", Font.PLAIN, 14);
    private final Border borda = BorderFactory.createEmptyBorder(10, 10, 20, 10);
    private Cliente chatCliente;
    private JList<String> lista;
    private DefaultListModel<String> listaModel;

    protected JTextArea campoTexto, camopoUsuario;
    protected JFrame frame;
    protected JButton botaoMensagemPV, botaoIniciar, botaoEnviar;
    protected JPanel painelCliente, painelUsuario;

    /**
     * Main method to start client GUI app.
     *
     * @param args
     */
    public static void main(String args[]) {
        //set the look and feel to 'Nimbus'
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }
        new ClienteGUI();
    }//end main

    /**
     * GUI Constructor
     */
    public ClienteGUI() {

        frame = new JFrame(" Chat Cliente");

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                if (chatCliente != null) {
                    try {
                        enviarMensagem("Tchau, estou saindo" + " saiu");
                        chatCliente.serverStatic.sairChat(nome);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });

        Container c = getContentPane();
        JPanel outerPanel = new JPanel(new BorderLayout());

        outerPanel.add(getEntradaPainel(), BorderLayout.CENTER);
        outerPanel.add(getPainelTexto(), BorderLayout.NORTH);

        c.setLayout(new BorderLayout());
        c.add(outerPanel, BorderLayout.CENTER);
        c.add(getPainelUsuario(), BorderLayout.WEST);

        frame.add(c);
        frame.pack();
        frame.setAlwaysOnTop(true);
        frame.setLocation(150, 150);
        textField.requestFocus();

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Method to set up the JPanel to display the chat text
     *
     * @return
     */
    public JPanel getPainelTexto() {
        String bem_vindo = "Bem-vindo, digite seu nome e pressione Iniciar para começar\n";
        campoTexto = new JTextArea(bem_vindo, 14, 35);
        campoTexto.setMargin(new Insets(10, 10, 10, 10));
        campoTexto.setFont(meiryoFont);

        campoTexto.setLineWrap(true);
        campoTexto.setWrapStyleWord(true);
        campoTexto.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(campoTexto);
        painelTexto = new JPanel();
        painelTexto.add(scrollPane);

        painelTexto.setFont(new Font("Meiryo", Font.PLAIN, 14));
        return painelTexto;
    }

    /**
     * Method to build the panel with input field
     *
     * @return entradaPainel
     */
    public JPanel getEntradaPainel() {
        entradaPainel = new JPanel(new GridLayout(1, 1, 5, 5));
        entradaPainel.setBorder(borda);
        textField = new JTextField();
        textField.setFont(meiryoFont);
        entradaPainel.add(textField);
        return entradaPainel;
    }

    /**
     * Method to build the panel displaying currently connected users with a
     * call to the button panel building method
     *
     * @return
     */
    public JPanel getPainelUsuario() {

        painelUsuario = new JPanel(new BorderLayout());
        String userStr = " Usuarios Conectados      ";

        JLabel userLabel = new JLabel(userStr, JLabel.CENTER);
        painelUsuario.add(userLabel, BorderLayout.NORTH);
        userLabel.setFont(new Font("Meiryo", Font.PLAIN, 16));

        String[] noClientsYet = {"Outros usuarios"};
        setPainelCliente(noClientsYet);

        painelCliente.setFont(meiryoFont);
        painelUsuario.add(botoesPainel(), BorderLayout.SOUTH);
        painelUsuario.setBorder(borda);

        return painelUsuario;
    }

    /**
     * Populate current user panel with a selectable lista of currently
     * connected users
     *
     * @param clientesConectados
     */
    public void setPainelCliente(String[] clientesConectados) {
        painelCliente = new JPanel(new BorderLayout());
        listaModel = new DefaultListModel<>();

        for (String s : clientesConectados) {
            listaModel.addElement(s);
        }
        if (clientesConectados.length > 1) {
            botaoMensagemPV.setEnabled(true);
        }

        //Create the lista and put it in a scroll pane.
        lista = new JList<>(listaModel);
        lista.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lista.setVisibleRowCount(8);
        lista.setFont(meiryoFont);
        JScrollPane listScrollPane = new JScrollPane(lista);

        painelCliente.add(listScrollPane, BorderLayout.CENTER);
        painelUsuario.add(painelCliente, BorderLayout.CENTER);
    }

    public JPanel botoesPainel() {
        botaoEnviar = new JButton("Enviar ");
        botaoEnviar.addActionListener(this);
        botaoEnviar.setEnabled(false);

        botaoMensagemPV = new JButton("Enviar PV");
        botaoMensagemPV.addActionListener(this);
        botaoMensagemPV.setEnabled(false);

        botaoIniciar = new JButton("Iniciar ");
        botaoIniciar.addActionListener(this);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        buttonPanel.add(botaoMensagemPV);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(botaoIniciar);
        buttonPanel.add(botaoEnviar);

        return buttonPanel;
    }

   
    
    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            //get connected to chat service
            if (e.getSource() == botaoIniciar) {
                nome = textField.getText();
                if (nome.length() != 0) {
                    frame.setTitle(nome + "'s console ");
                    textField.setText("");
                    campoTexto.append("nome de ususario : " + nome + " conectando ao chat...\n");
                    getConectado(nome);
                    if (!chatCliente.problemaConexao) {
                        botaoIniciar.setEnabled(false);
                        botaoEnviar.setEnabled(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Digite seu nome para Iniciar");
                }
            }

            //get text and clear textField
            if (e.getSource() == botaoEnviar) {
                menssagem = textField.getText();
                textField.setText("");
                enviarMensagem(menssagem);
                System.out.println("Sending message : " + menssagem);
            }

            //send a private menssagem, to selected users
            if (e.getSource() == botaoMensagemPV) {
                int[] privateList = lista.getSelectedIndices();

                for (int i = 0; i < privateList.length; i++) {
                    System.out.println("selecionado index :" + privateList[i]);
                }
                menssagem = textField.getText();
                textField.setText("");
                enviarMensagemPV(privateList);
            }

        } catch (RemoteException remoteExc) {
            remoteExc.printStackTrace();
        }

    }//end actionPerformed

    // --------------------------------------------------------------------
    /**
     * Send a menssagem, to be relayed to all chatters
     *
     * @param chatMessage
     * @throws RemoteException
     */
    private void enviarMensagem(String chatMessage) throws RemoteException {
        chatCliente.serverStatic.updateChat(nome, chatMessage);
    }

 
    /**
     * Send a menssagem, to be relayed, only to selected chatters
     *
     * @param chatMessage
     * @throws RemoteException
     */
    private void enviarMensagemPV(int[] privateList) throws RemoteException {
        String privateMessage = "[PV para " + nome + "] :" + menssagem + "\n";
        chatCliente.serverStatic.enviarPV(privateList, privateMessage);
    }

    /**
     * Make the connection to the chat server
     *
     * @param userName
     * @throws RemoteException
     */
    private void getConectado(String userName) throws RemoteException {
        //remove whitespace and non word characters to avoid malformed url
        String limparNomeUser = userName.replaceAll("\\s+", "_");
        limparNomeUser = userName.replaceAll("\\W+", "_");
        try {
            chatCliente = new Cliente(this, limparNomeUser);
            chatCliente.IniciarCliente();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
