
package eu.doppel_helix.dev.blaesing.applicationrestart;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Register the application for the restart functionality. The binary
        // image is determined from the running process, the arguments need
        // to be set manually.
        //
        // "-XX:+UseOSErrorReporting" is required to disable the OpenJDK error
        // handling, which prevents the windows handling to kick in
        //
        // "-jar c:\\temp\\ApplicationRestart.jar" is the path to this
        // application. For demonstration purposes, the example is packed as
        // a fat jar and it is expected to be placed in the folder c:\temp
        //
        // "restarted" is a sample argument
        HRESULT hres = Kernel32.INSTANCE.RegisterApplicationRestart(Native.toCharArray("-XX:+UseOSErrorReporting -jar c:\\temp\\ApplicationRestart.jar restarted"), 0);
        // Ensure registration was successfull
        COMUtils.checkRC(hres);
        // Remove crash protection, that JNA instally by default
        Native.setProtected(false);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ApplicationRestart Sample");
            frame.setSize(800, 600);
            frame.setLayout(new GridBagLayout());

            JComponent label, value;

            GridBagConstraints labelConstraints = new GridBagConstraints();
            labelConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
            labelConstraints.insets = new Insets(2, 2, 2, 2);
            labelConstraints.weightx = 0;
            labelConstraints.weighty = 0;
            labelConstraints.gridx = 0;
            labelConstraints.fill = GridBagConstraints.BOTH;

            GridBagConstraints valueConstraints = new GridBagConstraints();
            valueConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
            valueConstraints.insets = new Insets(2, 2, 2, 2);
            valueConstraints.weightx = 1;
            labelConstraints.weighty = 0;
            valueConstraints.gridx = 1;
            valueConstraints.fill = GridBagConstraints.BOTH;

            label = new JLabel("Binary image: ");
            value = new JLabel(Kernel32Util.QueryFullProcessImageName(Kernel32.INSTANCE.GetCurrentProcess(), 0));

            labelConstraints.gridy = 0;
            valueConstraints.gridy = 0;
            frame.add(label, labelConstraints);
            frame.add(value, valueConstraints);

            label = new JLabel("Arguments: ");
            JTextPane argumentsPanel = new JTextPane();
            argumentsPanel.setEditable(false);
            StringBuilder argumentsText = new StringBuilder();
            for(String argument: args) {
                argumentsText.append(argument);
                argumentsText.append("\n");
            }
            argumentsPanel.setText(argumentsText.toString());
            value = new JScrollPane(argumentsPanel);

            labelConstraints.gridy = 1;
            valueConstraints.gridy = 1;
            valueConstraints.weighty = 1;
            frame.add(label, labelConstraints);
            frame.add(value, valueConstraints);

            // The automatic restart requires the application to run for at
            // least 60s.
            label = new JLabel("Kill Application:\n(Wait 60s!)");
            value = new JButton("Kill");
            ((JButton) value).addActionListener((ae) -> {
                // Without the JNA protection (see above), the query below
                // dereferences a null pointer and tries to read from it,
                // causing a segementation fault
                new Pointer(0).getByteArray(0, 1024);
            });

            labelConstraints.gridy = 2;
            valueConstraints.gridy = 2;
            valueConstraints.weighty = 0;
            frame.add(label, labelConstraints);
            frame.add(value, valueConstraints);

            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}
