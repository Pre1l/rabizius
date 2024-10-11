package view.page;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import asset.AssetLoader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import core.Core;
import data.RenderData;
import data.SubmitData;
import data.SubmitResult;
import view.browser.Browser;

public class RegisterPage extends Page 
{
    private JLabel status = new JLabel("");
    private boolean dataValid = true;
    PasswordTextField passwordTextField1;
    PasswordTextField passwordTextField2;
    protected BufferedImage backgroundImage = AssetLoader.loadBufferedImage(AssetLoader.BACKGROUND_CIRCULAR_LINES_PATH);
    JTextField usernameTextField;

    public RegisterPage(Browser browser) 
    {
        super(browser);
    }

    public Page render(RenderData renderData) 
    {
        status.setForeground(SOFT_RED);
        this.setLayout(new GridBagLayout());
        browser.setMinimumSize(new Dimension(600,700));

        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 100, 15);
        flowLayout.setAlignment(FlowLayout.CENTER);
        JPanel centerPnl = new JPanel(flowLayout) {
            @Override
            protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Dimension arcs = new Dimension(50,50);
               int width = getWidth();
               int height = getHeight();
               Graphics2D graphics = (Graphics2D) g;
               graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
       
               graphics.setColor(QUATERNARY_COLOR);
               graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);
               graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        };
        this.add(centerPnl, new GridBagConstraints());
        int centerPnlWidth = 500;
        int centerPnlHeight = 550;
        centerPnl.setPreferredSize(new Dimension(centerPnlWidth, centerPnlHeight));
        centerPnl.setBackground(new Color(0, 0, 0));
        centerPnl.setOpaque(false);

        JLabel headlineLbl = new JLabel("Rabizius");
        headlineLbl.setFont(FINDEL_FONT_L);
        headlineLbl.setPreferredSize(new Dimension(400, 150));
        centerPnl.add(headlineLbl);

        TitledBorder titledBorderUsername = BorderFactory.createTitledBorder(new LineBorder(SECONDARY_COLOR), "Username");
        titledBorderUsername.setTitleColor(SECONDARY_COLOR);
        titledBorderUsername.setTitleFont(POPPINS_FONT_BOLD_S);
        usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(new Dimension(250, 55));
        usernameTextField.setBorder(BorderFactory.createCompoundBorder(titledBorderUsername, BorderFactory.createEmptyBorder(0, 5, 3, 5)));
        usernameTextField.setFont(POPPINS_FONT_MEDIUM_M);
        usernameTextField.setOpaque(false);
        usernameTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleUpdate(e);
            }
            
            
        });
        centerPnl.add(usernameTextField);

        passwordTextField1 = new PasswordTextField("Password");
         passwordTextField2 = new PasswordTextField("Repeat Password");
        passwordTextField1.getPasswordField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleUpdate(e);
            }
            
            
        });
        passwordTextField2.getPasswordField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleUpdate(e);
            }
            
            
        });

        centerPnl.add(passwordTextField1);
        centerPnl.add(passwordTextField2);

        status.setPreferredSize(new Dimension(centerPnlWidth, 20));
        status.setFont(POPPINS_FONT_MEDIUM_S);
        status.setHorizontalAlignment(JLabel.CENTER);
        centerPnl.add(status);

        JButton registerBtn = new JButton("<html>Register</html>");
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setPreferredSize(new Dimension(240, 35));
        registerBtn.setFont(POPPINS_FONT_BOLD_M);
        registerBtn.setBorderPainted(false);
        registerBtn.setMargin(new Insets(0,0,0,0));
        registerBtn.addActionListener(e -> {
            if (!dataValid) {
                return;
            }
            SubmitData submitData = new SubmitData();
            submitData.setData("password", new String(passwordTextField1.getPasswordField().getPassword()));
            submitData.setData("username", usernameTextField.getText());
            SubmitResult result = Core.instance().submit("/register", submitData);
            if (result.getStatus()) {
                String instanceToken = result.getData("instanceToken"); 
                String userId = result.getData("userId");
                browser.setSession(userId, instanceToken);
                SubmitData validData = browser.getSession();
                Core.instance().redirect("/dashboard", validData);
            } else {
                browser.displayErrorDialog(result.getData("error"));
            }
        });
        centerPnl.add(registerBtn);
        
        JPanel div = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, -5));
        div.setPreferredSize(new Dimension(centerPnlWidth, 20));
        div.setOpaque(false);
        centerPnl.add(div);
        JLabel helptextLbl = new JLabel("<html>Already have an Account?</html>");
        helptextLbl.setOpaque(false);
        helptextLbl.setFont(POPPINS_FONT_MEDIUM_S);
        headlineLbl.setHorizontalAlignment(JLabel.CENTER);
        
        JButton loginBtn = new JButton("<html><u>login</u></html>");
        loginBtn.setOpaque(false);
        loginBtn.setFont(POPPINS_FONT_MEDIUM_S);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setMargin(new Insets(0, -3, 0, 0));
        loginBtn.addActionListener(e -> {
            Core.instance().redirect("/login");
        });
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        div.add(helptextLbl);
        div.add(loginBtn);
        return this;
    }
    
    private void handleUpdate(DocumentEvent e) 
    {
        if (usernameTextField.getText().length() > 18 && usernameTextField.getText().length() < 5) {
            status.setText("Username must be between 5 and 18 characters long.");
            dataValid = false;
        } else if (passwordTextField1.getPasswordField().getPassword().length < 8) {
            status.setText("Password must be at least 8 characters long.");
            dataValid = false;
        } else if (Arrays.equals(passwordTextField1.getPasswordField().getPassword(), passwordTextField2.getPasswordField().getPassword())) {
            dataValid = true;
            status.setText("");
        } else {
            dataValid = false;
            status.setText("Passwords dont match.");  
        }
    }
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    private class PasswordTextField extends JPanel 
    {
        private JPasswordField passwordTextField;
        private JButton togglePasswordBtn;

        public PasswordTextField(String borderTitle) {
            this.setLayout(null);
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(250, 55));

            TitledBorder titledBorderPassword = BorderFactory.createTitledBorder(new LineBorder(SECONDARY_COLOR), borderTitle);
            titledBorderPassword.setTitleFont(POPPINS_FONT_BOLD_S);
            titledBorderPassword.setTitleColor(SECONDARY_COLOR);

            passwordTextField = new JPasswordField();
            passwordTextField.setBorder(BorderFactory.createCompoundBorder(titledBorderPassword, BorderFactory.createEmptyBorder(0, 5, 3, 40)));
            passwordTextField.setFont(POPPINS_FONT_MEDIUM_M);
            passwordTextField.setOpaque(false);
            passwordTextField.setBounds(0, 0, 250, 55);
            

            int scale = 28;

            ImageIcon hidePassword = AssetLoader.loadImageIcon(AssetLoader.ICON_HIDE_PASSWORD_PATH);
            Image hidePasswordImg = hidePassword.getImage().getScaledInstance(scale, -1, Image.SCALE_SMOOTH);
            ImageIcon hhidePassword = new ImageIcon(hidePasswordImg);

            ImageIcon showPassword = AssetLoader.loadImageIcon(AssetLoader.ICON_SHOW_PASSWORD_PATH);
            Image showPasswordImg = showPassword.getImage().getScaledInstance(scale, -1, Image.SCALE_SMOOTH);
            ImageIcon sshowPassword = new ImageIcon(showPasswordImg);

            togglePasswordBtn = new JButton();
            togglePasswordBtn.setPreferredSize(new Dimension(50, 50));
            togglePasswordBtn.setMargin(new Insets(0, 0, 0, 0));
            togglePasswordBtn.setIcon(sshowPassword);
            togglePasswordBtn.setOpaque(false);
            togglePasswordBtn.setContentAreaFilled(false);
            togglePasswordBtn.setBounds(209, 17, scale, scale);
            togglePasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            togglePasswordBtn.addActionListener(new ActionListener() {
                boolean passwordBtnToggled = false;
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (passwordBtnToggled == false) {
                        passwordBtnToggled = true;
                        passwordTextField.setEchoChar((char) 0);
                        togglePasswordBtn.setIcon(hhidePassword);
                    } else {
                        passwordTextField.setEchoChar('\u2022');
                        togglePasswordBtn.setIcon(sshowPassword);
                        passwordBtnToggled = false;
                    }
                }
            });

            this.add(togglePasswordBtn);
            this.add(passwordTextField);
        }

        public JPasswordField getPasswordField() 
        {
            return passwordTextField;
        }
    }
}