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

import asset.AssetLoader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import core.Core;
import data.RenderData;
import data.SubmitData;
import data.SubmitResult;
import view.browser.Browser;

public class LoginPage extends Page 
{
    private JLabel status = new JLabel("");
    private boolean passwordBtnToggled = false;
    protected BufferedImage backgroundImage = AssetLoader.loadBufferedImage(AssetLoader.BACKGROUND_CIRCULAR_LINES_PATH);

    public LoginPage(Browser browser) 
    {
        super(browser);
    }

    public Page render(RenderData renderData) 
    {
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
        JTextField usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(new Dimension(250, 55));
        usernameTextField.setBorder(BorderFactory.createCompoundBorder(titledBorderUsername, BorderFactory.createEmptyBorder(0, 5, 3, 5)));
        usernameTextField.setFont(POPPINS_FONT_MEDIUM_M);
        usernameTextField.setOpaque(false);
        centerPnl.add(usernameTextField);

        JPanel passwordPanel = new JPanel(null); 
        passwordPanel.setOpaque(false);
        passwordPanel.setPreferredSize(new Dimension(250, 55));

        TitledBorder titledBorderPassword = BorderFactory.createTitledBorder(new LineBorder(SECONDARY_COLOR), "Password");
        titledBorderPassword.setTitleFont(POPPINS_FONT_BOLD_S);
        titledBorderPassword.setTitleColor(SECONDARY_COLOR);
        
        JPasswordField passwordTextField = new JPasswordField();
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

        JButton togglePasswordBtn = new JButton();
        togglePasswordBtn.setPreferredSize(new Dimension(50, 50));
        togglePasswordBtn.setMargin(new Insets(0, 0, 0, 0));
        togglePasswordBtn.setIcon(sshowPassword);
        togglePasswordBtn.setOpaque(false);
        togglePasswordBtn.setContentAreaFilled(false);
        togglePasswordBtn.setBounds(209, 17, scale, scale);
        togglePasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        togglePasswordBtn.addActionListener(new ActionListener() 
        {
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
        
        passwordPanel.add(togglePasswordBtn);
        passwordPanel.add(passwordTextField);

        centerPnl.add(passwordPanel);

        status.setPreferredSize(new Dimension(centerPnlWidth, 20));
        status.setFont(POPPINS_FONT_MEDIUM_S);
        status.setHorizontalAlignment(JLabel.CENTER);
        centerPnl.add(status);

        JButton signInBtn = new JButton("<html>Sign in</html>");
        signInBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signInBtn.setPreferredSize(new Dimension(240, 35));
        signInBtn.setFont(POPPINS_FONT_BOLD_M);
        signInBtn.setBorderPainted(false);
        signInBtn.setMargin(new Insets(0,0,0,0));
        signInBtn.addActionListener(e -> {
            SubmitData submitData = new SubmitData();
            submitData.setData("password", new String(passwordTextField.getPassword()));
            submitData.setData("username", usernameTextField.getText());
            SubmitResult result = Core.instance().submit("/login", submitData);

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
        centerPnl.add(signInBtn);
        
        JPanel div = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, -5));
        div.setPreferredSize(new Dimension(centerPnlWidth, 20));
        div.setOpaque(false);
        centerPnl.add(div);
        JLabel helptextLbl = new JLabel("<html>No Account?</html>");
        helptextLbl.setOpaque(false);
        helptextLbl.setFont(POPPINS_FONT_MEDIUM_S);
        headlineLbl.setHorizontalAlignment(JLabel.CENTER);
        
        JButton registerBtn = new JButton("<html><u>register</u></html>");
        registerBtn.setOpaque(false);
        registerBtn.setFont(POPPINS_FONT_MEDIUM_S);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setMargin(new Insets(0, -3, 0, 0));
        registerBtn.addActionListener(e -> {
            Core.instance().redirect("/register");
        });
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        div.add(helptextLbl);
        div.add(registerBtn);
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}