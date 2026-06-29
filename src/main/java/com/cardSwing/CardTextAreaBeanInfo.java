package com.cardSwing;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;
import java.util.List;

public class CardTextAreaBeanInfo extends SimpleBeanInfo {

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(CardTextArea.class);
        bd.setDisplayName("CardTextArea");
        return bd;
    }
    
    @Override
    public java.awt.Image getIcon(int iconKind) {
        return CardSwingIcons.getIconFor("CardTextArea");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            List<PropertyDescriptor> props = new ArrayList<>();
            String[] customProps = {
                "text", "font", "foreground", "background", "lineWrap", "wrapStyleWord",
                "fontSize", "editable", "columns", "rows", "labelMode",
                "radius", "borderColor", "focusColor", "hoverColor", "placeholder"
            };

            for (String propName : customProps) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(propName, CardTextArea.class);
                    pd.setPreferred(true);
                    pd.setValue("category", "CardTextArea Configs");
                    props.add(pd);
                } catch (Exception ignored) { }
            }

            return props.toArray(new PropertyDescriptor[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return super.getPropertyDescriptors();
        }
    }
}
