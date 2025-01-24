
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Main {

    JFrame window;
    JLabel counterLabel;
    Font counterFont = new Font("Arial", Font.PLAIN, 32);


    // Elements
    JButton startStop = new JButton();
    JRadioButton optionShutdown, optionRestart;
    ButtonGroup group;

    Timer timer;
    int second, minute, hour;
    String ddSecond, ddMinute, ddHour;
    DecimalFormat decimalFormat = new DecimalFormat("00");

    public static void main(String[] args) {
        new Main();
    }


    public Main() {
        window = new JFrame();
        window.setSize(new Dimension(500, 280));
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("AutoShutDown");
        window.setLocationRelativeTo(null);
        window.setResizable(false);

        counterLabel = new JLabel();
        counterLabel.setBounds(40, 30, 130, 80);
        counterLabel.setHorizontalAlignment(JLabel.CENTER);
        counterLabel.setFont(counterFont);

        window.setVisible(true);

        // Adding Elements Here
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/shutdownicon.png")));
        window.setIconImage(icon.getImage());
        counterLabel.setText("00:00:00");
        second = 0;
        minute = 0;
        hour = 0;


        // Time setter
        JSpinner time_spinner;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        SpinnerDateModel sm = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);

        time_spinner = new javax.swing.JSpinner(sm);
        time_spinner.setBounds(250, 50, 120, 20);

        JSpinner.DateEditor te = new JSpinner.DateEditor(time_spinner, "HH:mm:ss");
        time_spinner.setEditor(te);


        // StartStop button
        startStop.setText("Start");
        startStop.setBounds(250, 120, 120, 30);
        startStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object value = time_spinner.getValue();
                if (value instanceof Date date) {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String time = format.format(date);

                    String[] array = time.split(":");
                    hour = Integer.parseInt(array[0]);
                    minute = Integer.parseInt(array[1]);
                    second = Integer.parseInt(array[2]);

                }

                String action = e.getActionCommand();
                if (action.equals("Start")) {
                    startStop.setText("Stop");
                    countdownTimer();
                    timer.start();

                }
                if (action.equals("Stop")) {
                    startStop.setText("Start");
                    timer.stop();
                }


            }
        });


        // Add radio buttons for shutdown and restart
        optionShutdown = new JRadioButton("Shutdown");
        optionShutdown.setBounds(250, 160, 100, 30);

        optionRestart = new JRadioButton("Restart");
        optionRestart.setBounds(250, 190, 100, 30);

        // Group radio buttons so only one can be selected at a time
        group = new ButtonGroup();
        group.add(optionShutdown);
        group.add(optionRestart);

        // Add radio buttons to window
        window.add(optionShutdown);
        window.add(optionRestart);

        JLabel footerLabel = new JLabel("PrleSoft®");
        footerLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
        footerLabel.setBounds(420, 220, 120, 20);

        // Add Into The Frame
        window.add(counterLabel);
        window.add(startStop);
        window.add(time_spinner);
        window.add(footerLabel);
        window.revalidate();
        window.repaint();

    }

    public void countdownTimer() {

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                second--;

                ddSecond = decimalFormat.format(second);
                ddMinute = decimalFormat.format(minute);
                ddHour = decimalFormat.format(hour);
                counterLabel.setText(ddHour + ":" + ddMinute + ":" + ddSecond);


                if (second == -1) {
                    second = 59;
                    minute--;
                    if (minute == 0 && hour != 0) {
                        minute = 59;
                    }
                    hour--;
                }


                if (hour == 1 && minute == 0 && second == 0) {
                    hour = 0;
                    minute = 59;
                    second = 59;
                }

                ddSecond = decimalFormat.format(second);
                ddMinute = decimalFormat.format(minute);
                ddHour = decimalFormat.format(hour);
                counterLabel.setText(ddHour + ":" + ddMinute + ":" + ddSecond);

                if (minute == 0 && second == 0 && hour == 0) {
                    timer.stop();
                    startStop.setText("Start");

                    if (optionShutdown.isSelected()) {
                        try {
                            new ProcessBuilder().command("cmd", "/c", "shutdown /s /f /t 0").start();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else if (optionRestart.isSelected()) {
                        try {
                            new ProcessBuilder().command("cmd", "/c", "shutdown /r /f /t 0").start();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            }


        });
    }

}