package view.page;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import asset.AssetLoader;
import core.Core;
import view.browser.Browser;

class SideMenuPanel extends JPanel 
{
    public SideMenuPanel(int index, Browser browser) 
    {
        buildSideMenuPanel(index, browser);
    }

    public void buildSideMenuPanel(int index, Browser browser) 
    {
        setOpaque(false);
        Dimension leftPanelDim = getPreferredSize();
        leftPanelDim.width = 250;
        setPreferredSize(leftPanelDim);
        setLayout(new BorderLayout());
        JPanel leftBottomPanel = new JPanel();
        leftBottomPanel.setOpaque(false);
        JPanel leftTopPanel = new JPanel();
        leftTopPanel.setOpaque(false);
        leftTopPanel.setPreferredSize(new Dimension(250, 600));

        JLabel rabizius = new JLabel("Rabizius");
        rabizius.setPreferredSize(new Dimension(230, 70));
        rabizius.setFont(Page.FINDEL_FONT_S);
        rabizius.setHorizontalAlignment(JLabel.CENTER);
        rabizius.setVerticalAlignment(JLabel.CENTER);
        leftTopPanel.add(rabizius);

        String[] buttonText = {
            " Dashboard",
            " Transfer",
            " Deposit",
            " Withdraw",
            " Delete Account",
            //" Change Account",
            //" Manage User",
            " Logout"
        };
        String[] buttonLinks = {
            "/dashboard",
            "/transfer",
            "/deposit",
            "/withdraw",
            "/account/delete",
            //"/account/change",
            //"/user/manage",
            "/logout"
        };
        String[] iconPaths = {
            AssetLoader.ICON_DASHBOARD,
            AssetLoader.ICON_TRANSFER,
            AssetLoader.ICON_DEPOSIT,
            AssetLoader.ICON_WITHDRAW,
            AssetLoader.ICON_DELETE,
            //AssetLoader.ICON_CHANGE,
            //AssetLoader.ICON_SETTINGS,
            AssetLoader.ICON_LOGOUT
        };
        for (int i = 0; i < buttonText.length; i++) {
            JButton button = new JButton(buttonText[i]);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setFont(Page.POPPINS_FONT_MEDIUM_M);
            button.setPreferredSize(new Dimension(230, 60));
            button.setBorderPainted(false);
            button.setHorizontalAlignment(JButton.LEFT);
            button.setBackground(Page.PRIMARY_COLOR);
            ImageIcon buttonIcon = AssetLoader.loadImageIcon(iconPaths[i]);
            Image buttonIconSized = buttonIcon.getImage().getScaledInstance(30, -1, Image.SCALE_SMOOTH);
            buttonIcon = new ImageIcon(buttonIconSized);
            button.setIcon(buttonIcon);
            String link = buttonLinks[i];
            if (buttonText[i].equals(" Logout")) {
                leftBottomPanel.add(button);
                button.addActionListener(e -> {
                    browser.resetSession();
                    Core.instance().redirect("/login");
                });
            } else {
                leftTopPanel.add(button);
                button.addActionListener(e -> {
                    Core.instance().redirect(link, browser.getSession());
                });
            }

            if (i == index) {
                button.setBackground(Page.TERTIARY_COLOR);
            }
        }
        add(leftTopPanel, BorderLayout.NORTH);
        add(leftBottomPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setColor(Page.PRIMARY_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }
}
