package asset;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import core.Core;

public class AssetLoader {
    public static final String FONT_POPPINS_MEDIUM_PATH = "font/Poppins/Poppins-Medium.ttf";
    public static final String FONT_POPPINS_BOLD_PATH = "font/Poppins/Poppins-Bold.ttf";
    public static final String FONT_FINDEL_PATH = "font/Findel/Findel-Display-Regular.otf";
    public static final String FONT_OCR_PATH = "font/OCR/ocr-a.ttf";
    public static final String BACKGROUND_CIRCULAR_LINES_PATH = "image/background_circular_lines.png";
    public static final String BACKGROUND_SQUARE_PATTERN_PATH = "image/background_square_pattern.png";
    public static final String BACKGROUND_GEOMETRY_WINDOW_PATH = "image/background_geometry_window.png";
    public static final String BACKGROUND_WAVES_PATH = "image/background_waves.png";
    public static final String BACKGROUND_SQUARED_PILLARS_PATH = "image/background_squared_pillars.png";
    public static final String ICON_HIDE_PASSWORD_PATH = "image/hide_password.png";
    public static final String ICON_SHOW_PASSWORD_PATH = "image/show_password.png";
    public static final String ICON_LOGO_ROUNDED_CORNERS = "image/r_rounded_corners.png";
    public static final String ICON_CREDIT_CARD = "image/credit_card.png";
    public static final String ICON_DASHBOARD = "image/dashboard.png";
    public static final String ICON_WITHDRAW = "image/withdraw.png";
    public static final String ICON_TRANSFER = "image/transfer.png";
    public static final String ICON_DEPOSIT = "image/deposit.png";
    public static final String ICON_LOGOUT = "image/logout.png";
    public static final String ICON_DELETE = "image/delete.png";
    public static final String ICON_CHANGE = "image/change.png";
    public static final String ICON_SETTINGS = "image/settings.png";
    public static final String ICON_FIVE_BILL = "image/5.png";
    public static final String ICON_TEN_BILL = "image/10.png";
    public static final String ICON_TWENTY_BILL = "image/20.png";
    public static final String ICON_FIFTY_BILL = "image/50.png";
    public static final String ICON_HUNDREAD_BILL = "image/100.png";
    public static final String ICON_TWO_HUNDREAD_BILL = "image/200.png";

    private static final Map<String, ImageIcon> imageIconCache = new HashMap<>();
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();

    public static ImageIcon loadImageIcon(String path) {
        if (imageIconCache.containsKey(path)) {
            return imageIconCache.get(path);
        }

        try {
            URL imageUrl = AssetLoader.class.getResource(path);
            if (imageUrl == null) {
                throw new IOException("Resource not found: " + path);
            }
            Image image = ImageIO.read(imageUrl);
            ImageIcon imageIcon = new ImageIcon(image);
            imageIconCache.put(path, imageIcon);
            return imageIcon;
        } catch (IOException e) {
            Core.instance().displayErrorDialog("Image file not found: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage loadBufferedImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        try {
            URL imageUrl = AssetLoader.class.getResource(path);
            if (imageUrl == null) {
                throw new IOException("Resource not found: " + path);
            }
            BufferedImage bufferedImage = ImageIO.read(imageUrl);
            imageCache.put(path, bufferedImage);
            return bufferedImage;
        } catch (IOException e) {
            Core.instance().displayErrorDialog("Image file not found: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public static Font loadFont(String fontPath) {
        try {
            URL fontUrl = AssetLoader.class.getResource(fontPath);
            if (fontUrl == null) {
                throw new IOException("Resource not found: " + fontPath);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (Exception e) {
            Core.instance().displayErrorDialog("Font file not found or could not be loaded: " + fontPath);
            e.printStackTrace();
            return null;
        }
    }
}
