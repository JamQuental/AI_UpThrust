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
import java.util.Arrays;

public class UpThrustAnswer implements Runnable {

    private Thread t;
    private Socket s;
    private final int port = 0xBAC0;
    private boolean run;
    private final JFrame jf;
    private final JPanel jp;
    private final JTextField jt;
    private final JTextField jv;
    private final JTextField jc;
    private BufferedReader in = null;
    private BufferedWriter out = null;

    public UpThrustAnswer() {

        jf = new JFrame("UP - IA - UpThrustAB");
        new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int res = confirmExit();
                if (res == JOptionPane.YES_OPTION) { // confirm?
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
        jn.add(new JLabel("Count: "));
        jc = new JTextField("      ", 24);
        jn.add(jc);
        jn.add(new JLabel("Time: "));
        jv = new JTextField("  ", 3);
        jn.add(jv);
        JPanel js = new JPanel();
        jp.add(js, BorderLayout.SOUTH);
        JButton jok = new JButton("  OK  ");
        jok.addActionListener(e -> {
            String st = jt.getText();
            try {
                out.write( st+"\n");    // sends contents of text-field
                out.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("Wrote "+st);
            jt.setBackground(Color.white);
            jt.setText("");
            jt.setEnabled(false);
            jc.setText("");
        });
        js.add(jok);
        JButton jEnd = new JButton("  Exit  ");
        js.add(jEnd, BorderLayout.SOUTH);
        jEnd.addActionListener(e -> {
            int res = confirmExit();
            if (res == JOptionPane.YES_OPTION) { // confirm?
                System.exit(0);
            }
        });
        run = false;
        jf.pack();
        jf.setVisible(true);
    }

    public void start() {

        run = true;
        jt.setEnabled(false);
        t = new Thread(this);
        t.start();

    }

    // thread that runs in parallel with the rest of the program
    public void run() {
        try {
            s = new Socket("127.0.0.1", port);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Unable to open socket");
            System.exit(0);
        }
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Unable to open socket");
            System.exit(0);
        }

        System.out.println("Start run");
        // in principle, it runs without stopping
        while (run) {
            String st;
            try {
                if (in.ready()) {   // is there data to be read?
                    jt.setText("");
                    jc.setText("");
                    st = in.readLine(); // read a line (until you get "\n")
                    System.out.println("readLine " + st);
                    if (st.length() < 4) { // if short, the number of player (1 or 2)
                        jv.setText(st);
                        NodeGameAB.setTurn(st);
                        continue;   // ends this cycle and starts the while again
                    }
                    jt.setBackground(Color.yellow); // to let the player know it is his turn to play
                    jt.setEnabled(true);
                    UpThrustGame initial = new UpThrustGame(st); //processState(st));
                    String res = initial.processAB(jc);
                    jt.setText(res);
//                        System.out.println(initial.toString());

                    String str = jt.getText();
                    try {
                        out.write(str + "\n");   // sends content of text-field
                        out.flush();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.exit(0);
                    }
                    System.out.println("Self-written " + str);
                    jt.setBackground(Color.white);
                    jt.setEnabled(false);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(0);
            }
            try {
                Thread.yield();// suspends to free the CPU
            } catch (Exception e) {
            }
        }
    }

    public String[][] processState(String st) {
        String[] v = st.trim().split(" ");
        String[][] s = new String[11][4];
        for (int l = 0; l < 11; l++) {
            for (int c = 0; c < 4; c++) {
                try {
                    s[l][c] = v[l * 4 + c];
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("tab " + Arrays.toString(v) + "  l " + l + "  c " + c);
                }
            }
        }
        return s;
    }

    public void stop() {
        run = false;
    }

    /**
     * Dialog asking for confirmation to end the application
     *
     * Return an int indicating the option selected by the user
     */
    protected int confirmExit() {
        return JOptionPane.showConfirmDialog(
                null,
                " Do you confirm the end of the program? ",
                " UP - IA - Dao ",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        UpThrustAnswer r = new UpThrustAnswer();
        r.start();

    }


}
