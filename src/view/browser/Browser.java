package view.browser;

import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

import asset.AssetLoader;
import core.Core;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import data.RenderData;
import data.SubmitData;
import view.page.Page;

/**
 * Browser for the user to see or interact with the pages/views
 */
public class Browser extends JFrame {
    private Page currentPage = null;
    private Image frameImage;
    private String instanceToken = null;
    private String userId = null;

    public static void init() {
        FlatCarbonIJTheme.setup();
    }

    public Browser() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Core.shutdown();
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frameImage = AssetLoader.loadBufferedImage(AssetLoader.ICON_LOGO_ROUNDED_CORNERS);
        setIconImage(frameImage);

        setVisible(true);
    }

    public void setSession(String userId, String instanceToken) {
        System.out.println("Set Browser Session");
        this.instanceToken = instanceToken;
        this.userId = userId;
    }

    public void resetSession() {
        System.out.println("Reset Browser Session");
        userId = null;
        instanceToken = null;
    }

    public SubmitData getSession() {
        SubmitData data = new SubmitData();
        data.setData("instanceToken", instanceToken);
        data.setData("userId", userId);
        return data;
    }

    public void displayErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
 
    public Page getCurrentPage() {
        return currentPage;
    }

    /**
     * Renders the desired page into the browser
     * @param page the page that should be rendered
     * @param renderData the render data for the page to use
     */
    public void renderPage(String page, RenderData renderData) {
        String title = renderData.getData("title");
        if (title != null) {
            setTitle(title);
        }
        try {
            Class<?> cls = Class.forName("view.page." + page);
            Object instance = cls.getDeclaredConstructor(getClass()).newInstance(this);
            
            Method method = cls.getMethod("render", renderData.getClass());
            Page loadedPage = (Page) method.invoke(instance, renderData);
            currentPage = loadedPage;
            setContentPane(currentPage);

            revalidate();
            repaint();

            System.out.println("Rendered " + page);
        } catch (ClassNotFoundException e) {
            Core.instance().displayErrorDialog("Page not found: " + page);
        } catch (NoSuchMethodException e) {
            Core.instance().displayErrorDialog("Page render method not found: " + page);
        } catch (Exception e) {
            Core.instance().displayErrorDialog("Error invoking method: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
