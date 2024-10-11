package view.page;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import asset.AssetLoader;
import core.Core;
import data.RenderData;
import data.SubmitData;
import data.SubmitResult;
import view.browser.Browser;

public class DepositPage extends Page 
{
    protected BufferedImage backgroundImage = AssetLoader.loadBufferedImage(AssetLoader.BACKGROUND_SQUARED_PILLARS_PATH);

    public DepositPage(Browser browser) 
    {
        super(browser);
    }

    public Page render(RenderData renderData) 
    {
        setLayout(new BorderLayout());
        browser.setMinimumSize(new Dimension(1280, 720));
        add(new SideMenuPanel(2, browser), BorderLayout.WEST);

        JPanel middlePanel = new JPanel();
        middlePanel.setOpaque(false);
        middlePanel.setLayout(new GridBagLayout());
        add(middlePanel, BorderLayout.CENTER);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 10, 10);

        JPanel balancePanel = getTransparentPanel();
        String balanceString = renderData.getData("balance");
        Double balanceDouble = Double.parseDouble(balanceString);
        JLabel balance = new JLabel(balanceString + "€");
        balance.setVerticalAlignment(JLabel.CENTER);
        balance.setHorizontalAlignment(JLabel.CENTER);
        balance.setFont(POPPINS_FONT_MEDIUM_XL);
        if (balanceDouble >= 0) {
            balance.setBorder(new MatteBorder(0, 0, 8, 0, SOFT_GREEN));
        } else {
            balance.setBorder(new MatteBorder(0, 0, 8, 0, SOFT_RED));
        }
        balance.setPreferredSize(new Dimension(700, 120));
        balancePanel.setPreferredSize(new Dimension(800, 170));
        balancePanel.add(balance);
        middlePanel.add(balancePanel, c);
        c.gridy++;

        JPanel money = getTransparentPanel();
        money.setPreferredSize(new Dimension(980, 385));
        money.setLayout(new GridBagLayout());
        GridBagConstraints cMoney = new GridBagConstraints();
        cMoney.fill = GridBagConstraints.VERTICAL;
        cMoney.gridx = 0;
        cMoney.gridy = 0;
        cMoney.insets = new Insets(5, 5, 5, 5);

        String[] buttonIcons = {
            AssetLoader.ICON_FIVE_BILL,
            AssetLoader.ICON_TEN_BILL,
            AssetLoader.ICON_TWENTY_BILL,
            AssetLoader.ICON_FIFTY_BILL,
            AssetLoader.ICON_HUNDREAD_BILL,
            AssetLoader.ICON_TWO_HUNDREAD_BILL
        };

        String[] buttonAmounts = {
            "5",
            "10",
            "20",
            "50",
            "100",
            "200"
        };

        for (int i = 0; i < buttonIcons.length; i++) {
            JButton button = new JButton();
            int scaleX = 290;
            int scaleY = 150;
            ImageIcon buttonIcon = AssetLoader.loadImageIcon(buttonIcons[i]);
            Image buttonIconScaled = buttonIcon.getImage().getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
            buttonIcon = new ImageIcon(buttonIconScaled);
            button.setIcon(buttonIcon);
            button.setBorderPainted(false);
            button.setPreferredSize(new Dimension(310, 170));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            money.add(button, cMoney);
            cMoney.gridx++;
            if (cMoney.gridx == 3) {
                cMoney.gridx = 0;
                cMoney.gridy++;
            }
            int index = i;
            button.addActionListener(e -> {
                SubmitData submit = browser.getSession();
                submit.setData("amount", buttonAmounts[index]);
                SubmitResult result = Core.instance().submit("/deposit", submit);
                if (result.getStatus()) {
                    String balanceStringX = result.getData("balance");
                    Double balanceDoubleX = Double.parseDouble(balanceString);
                    balance.setText(balanceStringX + "€");
                    if (balanceDoubleX >= 0) {
                        balance.setBorder(new MatteBorder(0, 0, 8, 0, SOFT_GREEN));
                    } else {
                        balance.setBorder(new MatteBorder(0, 0, 8, 0, SOFT_RED));
                    }

                    balance.revalidate();
                    balance.repaint();
                } else {
                    browser.displayErrorDialog(result.getData("error"));
                }
            });
        }
        middlePanel.add(money, c);
        c.gridy++;

        JPanel depositPanel = new JPanel();
        depositPanel.setOpaque(false);
        depositPanel.setPreferredSize(new Dimension(400, 65));
        JLabel depositLabel = new JLabel("Deposit");
        depositLabel.setForeground(PRIMARY_COLOR);
        depositLabel.setHorizontalAlignment(JLabel.CENTER);
        depositLabel.setFont(POPPINS_FONT_BOLD_L);
        depositPanel.add(depositLabel);
        middlePanel.add(depositPanel, c);

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
