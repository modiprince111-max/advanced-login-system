import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class AdvancedLoginSystem extends JFrame implements ActionListener {

    JTextField usernameField, otpField, captchaField;
    JPasswordField passwordField;
    JButton loginBtn, verifyBtn, resendBtn;

    JLabel captchaLabel, timerLabel, statusLabel;

    String generatedOTP = "";
    String captcha = "";

    int attempts = 3;
    int timeLeft = 30;

    javax.swing.Timer countdownTimer;
    long otpTime;

    public AdvancedLoginSystem() {
        setTitle("Advanced Secure Login System");
        setSize(400, 450);
        setLayout(new GridLayout(10, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        // CAPTCHA
        captchaLabel = new JLabel();
        generateCaptcha();
        add(captchaLabel);

        captchaField = new JTextField();
        add(captchaField);

        loginBtn = new JButton("Login (Generate OTP)");
        loginBtn.addActionListener(this);
        add(loginBtn);

        add(new JLabel("Enter OTP:"));
        otpField = new JTextField();
        add(otpField);

        verifyBtn = new JButton("Verify OTP");
        verifyBtn.addActionListener(this);
        add(verifyBtn);

        resendBtn = new JButton("Resend OTP");
        resendBtn.addActionListener(this);
        add(resendBtn);

        timerLabel = new JLabel("OTP expires in: 0 sec");
        add(timerLabel);

        statusLabel = new JLabel("Attempts left: 3");
        add(statusLabel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

public void actionPerformed(ActionEvent e) {

    // Login button
    if (e.getSource() == loginBtn) {

    if (!captchaField.getText().equals(captcha)) {
        JOptionPane.showMessageDialog(this, "Wrong Captcha!");
        generateCaptcha();
        return;
            }

        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

            if (user.equals("admin") && pass.equals("1234")) {
                generateOTP();
                JOptionPane.showMessageDialog(this,
                        "OTP: " + generatedOTP + "\n(Valid for 30 seconds)");
                startTimer();
                otpTime = System.currentTimeMillis();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username/Password");
            }
        }

        // Verify OTP
        if (e.getSource() == verifyBtn) {

            long currentTime = System.currentTimeMillis();

            if (currentTime - otpTime > 30000) {
                JOptionPane.showMessageDialog(this, "OTP Expired!");
                return;
            }

            if (otpField.getText().equals(generatedOTP)) {
                JOptionPane.showMessageDialog(this, "Login Successful 🎉");
                System.exit(0);
            } else {
                attempts--;
                statusLabel.setText("Attempts left: " + attempts);

                if (attempts == 0) {
                    JOptionPane.showMessageDialog(this, "Account Locked!");
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid OTP");
                }
            }
        }

        // Resend OTP
        if (e.getSource() == resendBtn) {
            generateOTP();
            JOptionPane.showMessageDialog(this,
                    "New OTP: " + generatedOTP);
            attempts = 3;
            statusLabel.setText("Attempts reset to 3");
            startTimer();
        }
    }

    // Generate CAPTCHA
    void generateCaptcha() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        StringBuilder cap = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            cap.append(chars.charAt((int)(Math.random() * chars.length())));
        }
        captcha = cap.toString();
        captchaLabel.setText("Captcha: " + captcha);
    }

    // Generate OTP
    void generateOTP() {
        Random rand = new Random();
        generatedOTP = String.valueOf(1000 + rand.nextInt(9000));
    }

    // timer
    void startTimer() {
        timeLeft = 30;

        if (countdownTimer != null) countdownTimer.stop();

        countdownTimer = new javax.swing.Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("OTP expires in: " + timeLeft + " sec");

            if (timeLeft <= 0) {
                countdownTimer.stop();
                generatedOTP = "";
                JOptionPane.showMessageDialog(this, "OTP Expired!");
            }
        });

        countdownTimer.start();
    }

    public static void main(String[] args) {
        new AdvancedLoginSystem();
    }
}