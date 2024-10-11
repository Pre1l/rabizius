package view.page;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import asset.AssetLoader;
import data.RenderData;
import view.browser.Browser;

public class DashboardPage extends Page 
{
    protected BufferedImage backgroundImage = AssetLoader.loadBufferedImage(AssetLoader.BACKGROUND_SQUARED_PILLARS_PATH);

    public DashboardPage(Browser browser) 
    {
        super(browser);
    }

    public Page render(RenderData renderData) 
    {
        setLayout(new BorderLayout());
        browser.setMinimumSize(new Dimension(1280, 720));
        add(new SideMenuPanel(0, browser), BorderLayout.WEST);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 10, 10);
        
        middlePanel.setOpaque(false);
        add(middlePanel, BorderLayout.CENTER);

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

        JPanel infoPanel = getTransparentPanel();
        infoPanel.setPreferredSize(new Dimension(800, 400));
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints cInfo = new GridBagConstraints();
        cInfo.fill = GridBagConstraints.VERTICAL;
        cInfo.gridx = 0;
        cInfo.gridy = 0;
        cInfo.insets = new Insets(10, 10, 10, 10);
        cInfo.anchor = GridBagConstraints.NORTH;
        Dimension d = new Dimension(560, 50);
        JLabel accountNumber = new JLabel("<html><b>IBAN</b> " + renderData.getData("accountNumber") + "</html>");
        accountNumber.setFont(POPPINS_FONT_MEDIUM_L);
        accountNumber.setPreferredSize(d);
        JLabel status = new JLabel("<html><b>Status</b> Active</html>");
        status.setFont(POPPINS_FONT_MEDIUM_L);
        status.setPreferredSize(d);
        JLabel type = new JLabel("<html><b>Type</b> " + renderData.getData("accountType") + "</html>");
        type.setFont(POPPINS_FONT_MEDIUM_L);
        type.setPreferredSize(d);
        infoPanel.add(accountNumber, cInfo);
        cInfo.gridy++;
        infoPanel.add(status, cInfo);
        cInfo.gridy++;
        infoPanel.add(type, cInfo);
        cInfo.gridy++;
        middlePanel.add(infoPanel, c);

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
