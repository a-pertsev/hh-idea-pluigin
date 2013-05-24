package ru.hh.plugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import  com.intellij.ide.util.PropertiesComponent;


public class XSLReferencesSettingsPage implements Configurable  {

    private JTextField appPathTextField;
    private JCheckBox enableXSLReferences;
    Project project;


    public XSLReferencesSettingsPage(Project project) {
        this.project = project;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "XSL References";
    }

    @Override
    public JComponent createComponent() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout
                (panel,  BoxLayout.Y_AXIS));
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

        enableXSLReferences = new JCheckBox("Enable XSL references for this project");

        panel1.add(enableXSLReferences);
        panel1.add(Box.createHorizontalGlue());

        JPanel panel2 = new JPanel();
        panel2.setLayout( new BoxLayout(panel2,  BoxLayout.X_AXIS));



        appPathTextField = new JTextField(30);
        JLabel label = new JLabel("XSL relative path:");
        panel2.add(label);
        label.setLabelFor(appPathTextField);

        panel2.add( appPathTextField );
        panel2.add(Box.createHorizontalGlue());


        appPathTextField.setMaximumSize( appPathTextField.getPreferredSize() );

        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        panel3.add(Box.createHorizontalGlue());

        panel.add(panel1);
        panel.add(Box.createVerticalStrut(8));
        
        panel.add(panel2);
        panel.add(Box.createVerticalStrut(8));
        panel.add(panel3);
        panel.add(Box.createVerticalGlue());
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        appPathTextField.setText(properties.getValue("xslPath", "xhh/xsl/"));
        enableXSLReferences.setSelected(properties.getBoolean("enableXSLReferences", true));


        return panel;
    }

    @Override
    public void apply() throws ConfigurationException {
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        properties.setValue("xslPath", appPathTextField.getText());
        properties.setValue("enableXSLReferences", String.valueOf(enableXSLReferences.isSelected()));

    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public void disposeUIResources() {

    }

    @Override
    public void reset() {

    }
}
