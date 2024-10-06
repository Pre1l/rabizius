package view.page;

import javax.swing.JPanel;

import asset.AssetLoader;
import view.browser.Browser;

import java.awt.*;

public abstract class Page extends JPanel {
    protected Browser browser;

    protected static final Font POPPINS_FONT_MEDIUM_XL = AssetLoader.loadFont(AssetLoader.FONT_POPPINS_MEDIUM_PATH).deriveFont(90f);;
    protected static final Font POPPINS_FONT_MEDIUM_L = AssetLoader.loadFont(AssetLoader.FONT_POPPINS_MEDIUM_PATH).deriveFont(25f);;
    protected static final Font POPPINS_FONT_MEDIUM_M = AssetLoader.loadFont(AssetLoader.FONT_POPPINS_MEDIUM_PATH).deriveFont(15f);;
    protected static final Font POPPINS_FONT_MEDIUM_S = AssetLoader.loadFont(AssetLoader.FONT_POPPINS_MEDIUM_PATH).deriveFont(12f);;
    protected static final Font POPPINS_FONT_BOLD_L = AssetLoader.loadFont(AssetLoader.FONT_POPPINS_BOLD_PATH).deriveFont(50f);;
    protected static final Font POPPINS_FONT_BOLD_M = AssetLoader.loadFont(AssetLoader.FONT_POPPINS_BOLD_PATH).deriveFont(15f);;
    protected static final Font POPPINS_FONT_BOLD_S = AssetLoader.loadFont(AssetLoader.FONT_POPPINS_BOLD_PATH).deriveFont(12f);;
    protected static final Font FINDEL_FONT_S = AssetLoader.loadFont(AssetLoader.FONT_FINDEL_PATH).deriveFont(40f);;
    protected static final Font FINDEL_FONT_L =  AssetLoader.loadFont(AssetLoader.FONT_FINDEL_PATH).deriveFont(80f);;
    protected static final Font OCR_FONT_M = AssetLoader.loadFont(AssetLoader.FONT_OCR_PATH).deriveFont(35f);;

    protected static final Color PRIMARY_COLOR = new Color(23, 32, 48);
    protected static final Color SECONDARY_COLOR = new Color(186, 200, 203);
    protected static final Color TERTIARY_COLOR = new Color(7, 72, 82);
    protected static final Color QUATERNARY_COLOR = new Color(7, 10, 10);
    protected static final Color SOFT_GREEN = new Color(110, 193, 117);
    protected static final Color SOFT_RED = new Color(235,45,58);

    protected Page(Browser browser) {
        this.browser = browser;
    }

    protected static JPanel getTransparentPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setColor(PRIMARY_COLOR);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.dispose();
            }
        }; 
        panel.setOpaque(false);
        return panel;
    }
}
