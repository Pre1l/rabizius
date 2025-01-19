package view.page;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import asset.AssetLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import core.Core;
import data.RenderData;
import data.SubmitData;
import data.SubmitResult;
import view.browser.Browser;

public class DatabasePage extends Page 
{
    protected BufferedImage backgroundImage = AssetLoader.loadBufferedImage(AssetLoader.BACKGROUND_CIRCULAR_LINES_PATH);

    public DatabasePage(Browser browser) 
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

        TitledBorder titledBorderURL = BorderFactory.createTitledBorder(new LineBorder(SECONDARY_COLOR), "URL");
        titledBorderURL.setTitleColor(SECONDARY_COLOR);
        titledBorderURL.setTitleFont(POPPINS_FONT_BOLD_S);
        JTextField urlTextField = new JTextField();
        urlTextField.setPreferredSize(new Dimension(250, 55));
        urlTextField.setBorder(BorderFactory.createCompoundBorder(titledBorderURL, BorderFactory.createEmptyBorder(0, 5, 3, 5)));
        urlTextField.setFont(POPPINS_FONT_MEDIUM_M);
        urlTextField.setOpaque(false);
        centerPnl.add(urlTextField);

        TitledBorder titledBorderUser = BorderFactory.createTitledBorder(new LineBorder(SECONDARY_COLOR), "User");
        titledBorderUser.setTitleColor(SECONDARY_COLOR);
        titledBorderUser.setTitleFont(POPPINS_FONT_BOLD_S);
        JTextField userTextField = new JTextField();
        userTextField.setPreferredSize(new Dimension(250, 55));
        userTextField.setBorder(BorderFactory.createCompoundBorder(titledBorderUser, BorderFactory.createEmptyBorder(0, 5, 3, 5)));
        userTextField.setFont(POPPINS_FONT_MEDIUM_M);
        userTextField.setOpaque(false);
        centerPnl.add(userTextField);

        TitledBorder titledBorderPassword = BorderFactory.createTitledBorder(new LineBorder(SECONDARY_COLOR), "Password");
        titledBorderPassword.setTitleColor(SECONDARY_COLOR);
        titledBorderPassword.setTitleFont(POPPINS_FONT_BOLD_S);
        JTextField passwordTextField = new JTextField();
        passwordTextField.setPreferredSize(new Dimension(250, 55));
        passwordTextField.setBorder(BorderFactory.createCompoundBorder(titledBorderPassword, BorderFactory.createEmptyBorder(0, 5, 3, 5)));
        passwordTextField.setFont(POPPINS_FONT_MEDIUM_M);
        passwordTextField.setOpaque(false);
        centerPnl.add(passwordTextField);

        JButton connectBtn = new JButton("<html>Connect</html>");
        connectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        connectBtn.setPreferredSize(new Dimension(240, 35));
        connectBtn.setFont(POPPINS_FONT_BOLD_M);
        connectBtn.setBorderPainted(false);
        connectBtn.setMargin(new Insets(0,0,0,0));
        connectBtn.addActionListener(e -> {
            SubmitData submitData = new SubmitData();
            submitData.setData("url", urlTextField.getText());
            submitData.setData("user", userTextField.getText());
            submitData.setData("password", passwordTextField.getText());
            SubmitResult result = Core.instance().submit("/database", submitData);

            if (result.getStatus()) {
                Core.instance().redirect("/login");
            } else {
                browser.displayErrorDialog("Connection failed");
            }
        });
        centerPnl.add(connectBtn);

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