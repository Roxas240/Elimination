import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by 5917002227 on 2/10/2016.
 */
public class Main extends JPanel {
    public ButtonPanel panel_buttons = new ButtonPanel();
    public ControlPanel panel_controls = new ControlPanel();
    public ScorePanel panel_score = new ScorePanel();
    public DrawPanel panel_draw = new DrawPanel();
    public OutputPanel panel_output = new OutputPanel();

    public Main(){
        this.setLayout(new BorderLayout(5, 5));
        this.add(panel_output, BorderLayout.CENTER);

        System.out.println("1Test");
        System.out.println("2Test");
        System.out.println("3Test");
        System.out.println("4Test");
        System.out.println("5Test");
        System.out.println("6Test");
        System.out.println("7Test");
        System.out.println("8Test");
        System.out.println("9Test");
        System.out.println("10Test");
        System.out.println("11Test");
        System.out.println("12Test");
        System.out.println("13Test");
        System.out.println("14Test");
    }

    static class ButtonPanel extends JPanel{

    }

    static class ControlPanel extends JPanel{

    }

    static class ScorePanel extends JPanel{

    }

    static class OutputPanel extends JPanel{
        public JTextArea area;

        public OutputPanel(){
            area = new JTextArea();
            area.setEditable(false);

            DefaultCaret caret = (DefaultCaret)area.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            System.setOut(new JTextAreaPrintStream(area));
            setLayout(new BorderLayout());
            add(area, BorderLayout.CENTER);
        }

        static class JTextAreaPrintStream extends PrintStream{
            public JTextAreaPrintStream(JTextArea area){
                super(new JTextAreaOutputStream(area));
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

        public void paint(Graphics g){

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
