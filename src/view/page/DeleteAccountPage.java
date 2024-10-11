package view.page;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import core.Core;
import data.RenderData;
import view.browser.Browser;

public class DeleteAccountPage extends Page 
{
    public DeleteAccountPage(Browser browser) 
    {
        super(browser);
    }

    public Page render(RenderData renderData) 
    {
        JDialog dialog = new JDialog(browser, "Confirm Delete", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(browser);

        JLabel messageLabel = new JLabel("Are you sure you want to delete this account?", SwingConstants.CENTER);
        dialog.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(e -> {
            dialog.dispose();
            Core.instance().submit("/account/delete", browser.getSession());
            browser.resetSession();
            Core.instance().redirect("/login");
        });

        cancelButton.addActionListener(e -> {
            dialog.dispose();
        });

        dialog.setVisible(true);

        return browser.getCurrentPage();
    }
}
