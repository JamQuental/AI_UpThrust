import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class UpThrustAnswer implements Runnable {

    private Thread t;
    private Socket s;
    private int porta = 0xBAC0;
    private boolean run;
    private JFrame jf;
    private JPanel jp;
    private JTextField jt;
    private JTextField jv;
    private JTextField jc;
    private BufferedReader in = null;
    private BufferedWriter out = null;

    public UpThrustAnswer() {

        jf = new JFrame("UP - IA - MomentumAB");
        new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int res = saiOuNao();
                if (res == JOptionPane.YES_OPTION) { // confirmado ?
                    System.exit(0);
                }
            }
        };

        jp = new JPanel();
        jp.setLayout(new BorderLayout());
        jf.add(jp);
        JPanel jn = new JPanel();
        jp.add(jn, BorderLayout.NORTH);
        jt = new JTextField("    ", 24);
        jn.add(jt);
        jn.add(new JLabel("Contagem: "));
        jc = new JTextField("      ", 24);
        jn.add(jc);
        jn.add(new JLabel("Vez: "));
        jv = new JTextField("  ", 3);
        jn.add(jv);
        JPanel js = new JPanel();
        jp.add(js, BorderLayout.SOUTH);
        JButton jok = new JButton("  OK  ");
        jok.addActionListener( new ActionListener( ) {
            public void actionPerformed(ActionEvent e) {
                String st = jt.getText();
                try {
                    out.write( st+"\n");    // envia conte�do do textfield
                    out.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.out.println("Escreveu "+st);
                jt.setBackground(Color.white);
                jt.setText("");
                jt.setEnabled(false);
                jc.setText("");
            }
        });
        js.add(jok);
        JButton jfim = new JButton("  Sair  ");
        js.add(jfim, BorderLayout.SOUTH);
        jfim.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int res = saiOuNao();
                if (res == JOptionPane.YES_OPTION) { // confirmado ?
                    System.exit(0);
                }
            }
        });
        run = false;
        jf.pack();
        jf.setVisible(true);
    }

    public void inicia() {

        run = true;
        jt.setEnabled(false);
        t = new Thread(this);
        t.start();

    }

    // thread que corre em paralelo com o resto do programa
    public void run() {
        try {
            s = new Socket("10.180.5.81", porta);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Impossível abrir socket");
            System.exit(0);
        }
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Impossível abrir socket");
            System.exit(0);
        }
        ;
        System.out.println("Start run");
        while (run) {
            String st = null;
            try {
                if (in.ready()) {   // h� dados para serem lidos?
                    jt.setText("");
                    jc.setText("");
                    st = in.readLine(); // l� uma linha (at� receber "\n")
                    System.out.println("readLine " + st);
                    if (st.length() < 4) { // se � curta, � o n�mero de jogador (1 ou 2)
                        jv.setText(st);
                        NodeGameAB.setTurn(st);
                        continue;   // termina este ciclo e recome�a o while
                    }
                    jt.setBackground(Color.yellow); // para avisar o jogador que � a vez dele jogar
                    jt.setEnabled(true);
                    UpThrustGame inicial = new UpThrustGame(st); //processaEstado(st));
                    String res = inicial.processAB(jc);
                    jt.setText(res);
//                        System.out.println(inicial.toString());

                    String str = jt.getText();
                    try {
                        out.write(str + "\n");   // envia conteúdo do textfield
                        out.flush();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.exit(0);
                    }
                    System.out.println("Escreveu auto " + str);
                    jt.setBackground(Color.white);
                    jt.setEnabled(false);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(0);
            }
            try {
                Thread.yield();// suspende para libertar o CPU
            } catch (Exception e) {
            }
        }
    }

    public String[][] processaEstado(String st) {
        String[] v = st.trim().split(" ");
        String[][] s = new String[11][4];
        for (int l = 0; l < 11; l++) {
            for (int c = 0; c < 4; c++) {
                try {
                    s[l][c] = v[l * 4 + c];
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("tab " + v + "  l " + l + "  c " + c);
                }
            }
        }
        return s;
    }

    public void stop() {
        run = false;
    }

    /**
     * Dilogo que pede confirmao para terminar a aplicao
     *
     * @return um int indicando a opo seleccionada pelo utilizador
     */
    protected int saiOuNao() {
        return JOptionPane.showConfirmDialog(
                null,
                " Confirma o fim do programa ? ",
                " UP - IA - Dao ",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        UpThrustAnswer answer = new UpThrustAnswer();
        answer.inicia();

    }


}
