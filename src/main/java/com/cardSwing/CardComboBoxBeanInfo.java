package com.cardSwing;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;
import java.util.List;

public class CardComboBoxBeanInfo extends SimpleBeanInfo {

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(CardComboBox.class);
        bd.setDisplayName("CardComboBox");
        return bd;
    }
    
    @Override
    public java.awt.Image getIcon(int iconKind) {
        return CardMuryIcons.getIconFor("CardComboBox");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            List<PropertyDescriptor> props = new ArrayList<>();
            String[] customProps = {
                "font", "foreground", "background", "radius", "borderColor", "focusColor", "model"
            };

            for (String propName : customProps) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(propName, CardComboBox.class);
                    pd.setPreferred(true);
                    pd.setValue("category", "CardMury Configs");
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
