import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;

/**
 * Created by Roxas on 2/10/2016.
 */
public class Main extends JPanel implements ActionListener {
    public ButtonPanel panel_buttons = new ButtonPanel(this);
    public OutputPanel panel_output = new OutputPanel();
    public ControlPanel panel_controls = new ControlPanel(this);
    public ScorePanel panel_score = new ScorePanel();
    public DrawPanel panel_draw = new DrawPanel();

    public Main(){
        this.setLayout(new BorderLayout(5, 5));
        this.add(panel_output, BorderLayout.SOUTH);
        this.add(panel_buttons, BorderLayout.NORTH);
        this.add(panel_controls, BorderLayout.WEST);
        this.add(panel_draw, BorderLayout.CENTER);

        System.out.println("Started Game");
    }

    public void actionPerformed(ActionEvent e){
        String action = e.getActionCommand();
        System.out.println("Action");
        if(action.startsWith("BP:")){
            int button = Integer.parseInt(action.split(":")[1]);

            System.out.println("Button " + button);
        }
    }

    static class ButtonPanel extends JPanel{
        public JButton[] buttons = new JButton[12];
        public ButtonPanel(ActionListener listener){
            this.setLayout(new GridLayout(2, 6, 5, 5));
            for(int i = 0; i < 12; i++){
                buttons[i] = new JButton(intToString(i + 1));
                buttons[i].setActionCommand("BP:" + (i + 1));
                buttons[i].addActionListener(listener);

                this.add(buttons[i]);
            }
        }

        public static String intToString(int i){
            switch(i){
                case 1:
                    return "One";
                case 2:
                    return "Two";
                case 3:
                    return "Three";
                case 4:
                    return "Four";
                case 5:
                    return "Five";
                case 6:
                    return "Six";
                case 7:
                    return "Seven";
                case 8:
                    return "Eight";
                case 9:
                    return "Nine";
                case 10:
                    return "Ten";
                case 11:
                    return "Eleven";
                case 12:
                    return "Twelve";
                default:
                    return "null";
            }
        }
    }

    static class ControlPanel extends JPanel{
        public JButton roll, rules, restart;

        public ControlPanel(ActionListener listener){
            roll = new JButton("Roll");
            rules = new JButton("Rules");
            restart = new JButton("Reset");

            roll.setActionCommand("roll");
            rules.setActionCommand("rules");
            restart.setActionCommand("reset");

            roll.addActionListener(listener);
            rules.addActionListener(listener);
            restart.addActionListener(listener);

            setLayout(new GridLayout(3, 1, 5, 5));

            add(roll);
            add(rules);
            add(restart);
        }
    }

    static class ScorePanel extends JPanel{

    }

    static class OutputPanel extends JPanel{
        public JScrollPane scroll;
        public JTextArea area;

        public OutputPanel(){
            area = new JTextArea();
            area.setEditable(false);

            scroll = new JScrollPane(area);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            scroll.setMinimumSize(new Dimension(Integer.MIN_VALUE, 150));
            scroll.setPreferredSize(new Dimension(600, 150));

            DefaultCaret caret = (DefaultCaret)area.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            System.setOut(new JTextAreaPrintStream(area));
            setLayout(new BorderLayout());
            add(scroll, BorderLayout.CENTER);
        }

        static class JTextAreaPrintStream extends PrintStream{
            public JTextAreaPrintStream(JTextArea area){
                super(new JTextAreaOutputStream(area));
            }
            public void println(String s){
                super.println("> " + s);
            }
        }

        static class JTextAreaOutputStream extends OutputStream{
            private JTextArea area;

            public JTextAreaOutputStream(JTextArea area){
                this.area = area;
            }

            public void write(int b){
                area.append(((char) b) + "");
            }
        }
    }

    static class DrawPanel extends JPanel{
        public static final int DIE_SIZE = 128;
        public static final int DOT_SIZE = DIE_SIZE / 4;
        public static final int GEN_TIMES = 10;
        public static final int DIE_RADIUS = DIE_SIZE / 8;

        private Random ran = new Random();
        private int num1, num2;

        private int iterations;

        public DrawPanel(){

        }

        public void roll(final ActionListener listener){
            Timer t = new Timer(20, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    iterations++;

                    num1 = ran.nextInt(6) + 1;
                    num2 = ran.nextInt(6) + 1;

                    repaint();


                    if(iterations == GEN_TIMES){
                        iterations = 0;
                        ((Timer) e.getSource()).stop();
                        listener.actionPerformed(null);
                    }
                }
            });
            t.start();
        }

        public int[] getNums(){
            return new int[]{num1, num2};
        }

        public void paint(Graphics g){
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            int d1Cx = cx - (int)(DIE_SIZE * 0.75F);
            int d2Cx = cx + (int)(DIE_SIZE * 0.75F);

            int dCy = cy - (int)(DIE_SIZE / 2F);

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLACK);
            g.drawRoundRect(d1Cx - (int)(DIE_SIZE / 2F), dCy, DIE_SIZE, DIE_SIZE, DIE_RADIUS, DIE_RADIUS);
        }

        public void paintDie(Graphics g, int cx, int cy){

        }
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Main());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
