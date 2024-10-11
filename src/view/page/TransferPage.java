package view.page;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
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

public class TransferPage extends Page 
{
    private JLabel status = new JLabel("");
    protected BufferedImage backgroundImage = AssetLoader.loadBufferedImage(AssetLoader.BACKGROUND_SQUARED_PILLARS_PATH);

    public TransferPage(Browser browser) 
    {
        super(browser);
    }

    public Page render(RenderData renderData) 
    {
        this.setLayout(new BorderLayout());
        JPanel transferContainer = new JPanel(new GridBagLayout());
        transferContainer.setOpaque(false);
        browser.setMinimumSize(new Dimension(1280, 720));
        add(new SideMenuPanel(1, browser), BorderLayout.WEST);

        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 100, 15);
        flowLayout.setAlignment(FlowLayout.CENTER);
        JPanel centerPnl = getTransparentPanel();
        centerPnl.setLayout(flowLayout);
        transferContainer.add(centerPnl, new GridBagConstraints());
        add(transferContainer, BorderLayout.CENTER);

        int centerPnlWidth = 650;
        int centerPnlHeight = 650;
        centerPnl.setPreferredSize(new Dimension(centerPnlWidth, centerPnlHeight));
        centerPnl.setBackground(new Color(0, 0, 0));
        centerPnl.setOpaque(false);

        JLabel headlineLbl = new JLabel("Transfer");
        headlineLbl.setFont(POPPINS_FONT_BOLD_L);
        headlineLbl.setHorizontalAlignment(JLabel.CENTER);
        headlineLbl.setPreferredSize(new Dimension(500, 100));
        centerPnl.add(headlineLbl);

        TitledBorder titledBorderReceiver = BorderFactory.createTitledBorder(new LineBorder(SECONDARY_COLOR), "Transfer IBAN ");
        titledBorderReceiver.setTitleColor(SECONDARY_COLOR);
        titledBorderReceiver.setTitleFont(POPPINS_FONT_BOLD_S);
        JTextField ReceiverTextField = new JTextField();
        ReceiverTextField.setPreferredSize(new Dimension(250, 55));
        ReceiverTextField.setBorder(BorderFactory.createCompoundBorder(titledBorderReceiver, BorderFactory.createEmptyBorder(0, 5, 3, 5)));
        ReceiverTextField.setFont(POPPINS_FONT_MEDIUM_M);
        ReceiverTextField.setOpaque(false);
        centerPnl.add(ReceiverTextField);

        JPanel amountPanel = new JPanel(null); 
        amountPanel.setLayout(null);
        amountPanel.setOpaque(false);
        amountPanel.setPreferredSize(new Dimension(250, 55));
        
        JTextField AmountTextField = new JTextField(20);
        AmountTextField.setOpaque(false); 
        AmountTextField.setFont(POPPINS_FONT_MEDIUM_M);
        AmountTextField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        AmountTextField.setBounds(0, 0, 250, 40);
        AmountTextField.setHorizontalAlignment(JTextField.CENTER);
        Border underlineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE);
        AmountTextField.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 0, 0, 0), underlineBorder));

        amountPanel.add(AmountTextField);

        JLabel euroLabel = new JLabel("€");
        euroLabel.setFont(POPPINS_FONT_MEDIUM_L);
        euroLabel.setBounds(235,10,20,20);
        amountPanel.add(euroLabel);

        centerPnl.add(amountPanel);

        TitledBorder titledBorderReason = BorderFactory.createTitledBorder(new LineBorder(SECONDARY_COLOR), "Purpose");
        titledBorderReason.setTitleColor(SECONDARY_COLOR);
        titledBorderReason.setTitleFont(POPPINS_FONT_BOLD_S);

        JTextArea ReasonTextField = new JTextArea(7,20);
        ReasonTextField.setLineWrap(true);
        ReasonTextField.setWrapStyleWord(true);
        ReasonTextField.setPreferredSize(new Dimension(250, 195));
        ReasonTextField.setBorder(BorderFactory.createCompoundBorder(titledBorderReason, BorderFactory.createEmptyBorder(0, 5, 3, 5)));
        ReasonTextField.setFont(POPPINS_FONT_MEDIUM_M);
        ReasonTextField.setOpaque(false);
        centerPnl.add(ReasonTextField);

        status.setPreferredSize(new Dimension(centerPnlWidth, 20));
        status.setFont(POPPINS_FONT_MEDIUM_S);
        status.setHorizontalAlignment(JLabel.CENTER);
        centerPnl.add(status);

        JButton takeOverBtn = new JButton("Transfer");
        takeOverBtn.setPreferredSize(new Dimension(250, 55));
        takeOverBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        takeOverBtn.setFont(POPPINS_FONT_BOLD_M);
        takeOverBtn.setBorderPainted(false);
        takeOverBtn.setMargin(new Insets(0,0,0,0));
        takeOverBtn.addActionListener(e -> {
            SubmitData submitData = browser.getSession();
            submitData.setData("amount", AmountTextField.getText());
            submitData.setData("purpose", ReasonTextField.getText());
            submitData.setData("recievingAccountNumber", ReceiverTextField.getText());
            SubmitResult result = Core.instance().submit("/transfer", submitData);
            if (result.getStatus()) {
                AmountTextField.setText("");
                ReceiverTextField.setText("");
                ReasonTextField.setText("");
            } else {
                browser.displayErrorDialog(result.getData("error"));
            }
        });
        centerPnl.add(takeOverBtn);

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
