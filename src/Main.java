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

    public boolean canUseButton = false;

    public Main(){
        this.setLayout(new BorderLayout(5, 5));
        this.add(panel_output, BorderLayout.SOUTH);
        this.add(panel_buttons, BorderLayout.NORTH);
        this.add(panel_controls, BorderLayout.WEST);
        this.add(panel_score, BorderLayout.EAST);
        this.add(panel_draw, BorderLayout.CENTER);

        System.out.println("Started Game");
    }

    public void actionPerformed(ActionEvent e){
        String action = e.getActionCommand();
        if(action.startsWith("BP:") && canUseButton){
            int button = Integer.parseInt(action.split(":")[1]);
            System.out.println("Button " + button + " pressed");

            int[] nums = panel_draw.getNums();
            if(button == nums[0] && panel_draw.getEnabled(0)){
                ((JButton) e.getSource()).setEnabled(false);
                panel_draw.setDisabled(0);
                panel_controls.roll.setEnabled(true);
            } else if(button == nums[1] && panel_draw.getEnabled(1)){
                ((JButton) e.getSource()).setEnabled(false);
                panel_draw.setDisabled(1);
                panel_controls.roll.setEnabled(true);
            } else if(button == (nums[1] + nums[0]) && panel_draw.getEnabled(1) && panel_draw.getEnabled(0)){
                ((JButton) e.getSource()).setEnabled(false);

                panel_draw.setDisabled(0);
                panel_draw.setDisabled(1);

                panel_controls.roll.setEnabled(true);
            }

            panel_draw.repaint();
            System.out.println("Score: " + panel_buttons.getScore());
        } else if(action.equals("roll")){
            canUseButton = false;
            panel_controls.roll.setEnabled(false);
            panel_draw.roll(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int[] nums = panel_draw.getNums();
                    System.out.println(nums[0] + ":" + nums[1]);

                    canUseButton = true;

                    if(!panel_buttons.canGoAgain(nums[0], nums[1])){
                        int score = panel_buttons.getScore();
                        panel_controls.roll.setEnabled(false);

                        if(score == 0){
                            JOptionPane.showMessageDialog(null, "YOU'RE WINNER!!1", "Elimination",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Sorry you lost. Your final score was :"
                                    + score, "Elimination", JOptionPane.WARNING_MESSAGE);
                        }

                        panel_draw.reset();
                        panel_draw.repaint();
                    }
                }
            });
        } else if(action.equals("reset")){
            panel_buttons.reset();
            panel_draw.reset();
            panel_score.reset();

            panel_controls.roll.setEnabled(true);

            System.out.println("Game Reset");
        }

        panel_score.setScore(panel_buttons.getScore());
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

        public int getScore(){
            int ret = 0;
            for(int i = 0; i < 12; i++){
                ret += buttons[i].isEnabled() ? i + 1 : 0;
            }
            return ret;
        }

        public void reset(){
            for(int i = 0; i < 12; i++){
                buttons[i].setEnabled(true);
            }
        }

        public boolean canGoAgain(int n1, int n2){
            boolean ret = false;

            for(int i = 0; i < 12; i++){
                ret |= buttons[i].isEnabled() && (i + 1) == n1;
                ret |= buttons[i].isEnabled() && (i + 1) == n2;

                ret |= buttons[i].isEnabled() && (i + 1) == (n1 + n2);
            }

            return ret;
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
        public JTextField field;

        public ScorePanel(){
            field = new JTextField("78");
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setFont(new Font("Verdana", Font.PLAIN, 28));
            field.setEditable(false);
            field.setMinimumSize(new Dimension(100, 30));
            field.setPreferredSize(new Dimension(100, 30));

            setLayout(new BorderLayout());
            add(field, BorderLayout.CENTER);
        }

        public void reset(){
            field.setText("78");
        }

        public void setScore(int score){
            field.setText(score + "");
        }
    }

    static class OutputPanel extends JPanel{
        public JScrollPane scroll;
        public JTextArea area;

        public OutputPanel(){
            area = new JTextArea();
            area.setEditable(false);

            scroll = new JScrollPane(area);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            scroll.setMinimumSize(new Dimension(Integer.MIN_VALUE, 100));
            scroll.setPreferredSize(new Dimension(600, 100));

            DefaultCaret caret = (DefaultCaret)area.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            System.setOut(new JTextAreaPrintStream(area, scroll));
            setLayout(new BorderLayout());
            add(scroll, BorderLayout.CENTER);
        }

        static class JTextAreaPrintStream extends PrintStream{
            public JTextAreaPrintStream(JTextArea area, JScrollPane scroll){
                super(new JTextAreaOutputStream(area, scroll));
            }
            public void println(String s){
                super.println("> " + s);
            }
        }

        static class JTextAreaOutputStream extends OutputStream{
            private JTextArea area;
            private JScrollPane scroll;

            public JTextAreaOutputStream(JTextArea area, JScrollPane scroll){
                this.area = area;
                this.scroll = scroll;
            }

            public void write(int b){
                area.append(((char) b) + "");

                JScrollBar vertical = scroll.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            }
        }
    }

    static class DrawPanel extends JPanel{
        public static final int DIE_SIZE = 128;
        public static final int DOT_SIZE = DIE_SIZE / 4;
        public static final int GEN_TIMES = 10;
        public static final int DIE_RADIUS = DIE_SIZE / 8;

        private Random ran = new Random();
        private int num1 = 0, num2 = 0;
        private boolean e1, e2;

        private int iterations;

        public boolean getEnabled(int num){
            return num == 0 ? e1 : e2;
        }

        public void setDisabled(int num){
            e1 &= num != 0;
            e2 &= num != 1;
        }

        public void reset(){
            e1 = false;
            e2 = false;

            num1 = 0;
            num2 = 0;

            repaint();
        }

        public void roll(final ActionListener listener){
            Timer t = new Timer(50, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    iterations++;

                    num1 = ran.nextInt(6) + 1;
                    num2 = ran.nextInt(6) + 1;

                    if(iterations == GEN_TIMES){
                        iterations = 0;
                        ((Timer) e.getSource()).stop();
                        listener.actionPerformed(null);

                        e1 = true;
                        e2 = true;
                    }

                    repaint();
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

            g.setColor(e1 ? Color.WHITE : Color.GRAY);
            g.fillRoundRect(d1Cx - (int)(DIE_SIZE / 2F), dCy, DIE_SIZE, DIE_SIZE, DIE_RADIUS, DIE_RADIUS);

            g.setColor(e2 ? Color.WHITE : Color.GRAY);
            g.fillRoundRect(d2Cx - (int)(DIE_SIZE / 2F), dCy, DIE_SIZE, DIE_SIZE, DIE_RADIUS, DIE_RADIUS);

            g.setColor(Color.BLACK);
            g.drawRoundRect(d1Cx - (int)(DIE_SIZE / 2F), dCy, DIE_SIZE, DIE_SIZE, DIE_RADIUS, DIE_RADIUS);
            g.drawRoundRect(d2Cx - (int)(DIE_SIZE / 2F), dCy, DIE_SIZE, DIE_SIZE, DIE_RADIUS, DIE_RADIUS);

            g.setColor(Color.BLACK);
            paintDie(g, num1, d1Cx, cy);
            paintDie(g, num2, d2Cx, cy);
        }

        public void paintDie(Graphics g, int num, int cx, int cy){
            if(num == 1){
                g.fillOval(cx - (DOT_SIZE / 2), cy - (DOT_SIZE / 2), DOT_SIZE, DOT_SIZE);
            } else if(num == 2){
                g.fillOval(cx - (int)(DOT_SIZE * 1.5F), cy - (int)(DOT_SIZE * 1.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx + (int)(DOT_SIZE * 0.5F), cy + (int)(DOT_SIZE * 0.5F), DOT_SIZE, DOT_SIZE);
            } else if(num == 3){
                g.fillOval(cx - (DOT_SIZE / 2), cy - (DOT_SIZE / 2), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx - (int)(DOT_SIZE * 1.5F), cy - (int)(DOT_SIZE * 1.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx + (int)(DOT_SIZE * 0.5F), cy + (int)(DOT_SIZE * 0.5F), DOT_SIZE, DOT_SIZE);
            } else if(num == 4){
                g.fillOval(cx - (int)(DOT_SIZE * 1.5F), cy - (int)(DOT_SIZE * 1.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx + (int)(DOT_SIZE * 0.5F), cy + (int)(DOT_SIZE * 0.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx + (int)(DOT_SIZE * 0.5F), cy - (int)(DOT_SIZE * 1.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx - (int)(DOT_SIZE * 1.5F), cy + (int)(DOT_SIZE * 0.5F), DOT_SIZE, DOT_SIZE);
            } else if(num == 5){
                g.fillOval(cx - (DOT_SIZE / 2), cy - (DOT_SIZE / 2), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx - (int)(DOT_SIZE * 1.5F), cy - (int)(DOT_SIZE * 1.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx + (int)(DOT_SIZE * 0.5F), cy + (int)(DOT_SIZE * 0.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx + (int)(DOT_SIZE * 0.5F), cy - (int)(DOT_SIZE * 1.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx - (int)(DOT_SIZE * 1.5F), cy + (int)(DOT_SIZE * 0.5F), DOT_SIZE, DOT_SIZE);
            } else if(num == 6){
                g.fillOval(cx - (int)(DOT_SIZE * 1.5F), cy - (int)(DOT_SIZE * 1.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx + (int)(DOT_SIZE * 0.5F), cy + (int)(DOT_SIZE * 0.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx + (int)(DOT_SIZE * 0.5F), cy - (int)(DOT_SIZE * 1.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx - (int)(DOT_SIZE * 1.5F), cy + (int)(DOT_SIZE * 0.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx + (int)(DOT_SIZE * 0.5F), cy - (int)(DOT_SIZE * 0.5F), DOT_SIZE, DOT_SIZE);
                g.fillOval(cx - (int)(DOT_SIZE * 1.5F), cy - (int)(DOT_SIZE * 0.5F), DOT_SIZE, DOT_SIZE);
            }
        }
    }

    public static void main(String[] args){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){

        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Main());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
