package com.cardSwing;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class CardPasswordFieldBeanInfo extends SimpleBeanInfo {

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(CardPasswordField.class);
        bd.setDisplayName("CardPasswordField");
        return bd;
    }

    @Override
    public java.awt.Image getIcon(int iconKind) {
        return CardSwingIcons.getIconFor("CardPasswordField");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor radius = new PropertyDescriptor("radius", CardPasswordField.class);
            PropertyDescriptor borderColor = new PropertyDescriptor("borderColor", CardPasswordField.class);
            PropertyDescriptor focusColor = new PropertyDescriptor("focusColor", CardPasswordField.class);
            PropertyDescriptor placeholder = new PropertyDescriptor("placeholder", CardPasswordField.class);
            
            radius.setValue("category", "CardPasswordField Configs");
            borderColor.setValue("category", "CardPasswordField Configs");
            focusColor.setValue("category", "CardPasswordField Configs");
            placeholder.setValue("category", "CardPasswordField Configs");

            PropertyDescriptor[] props = {radius, borderColor, focusColor, placeholder};
            for (PropertyDescriptor pd : props) {
                pd.setPreferred(true);
            }
            return props;
        } catch (Exception e) {
            e.printStackTrace();
            return super.getPropertyDescriptors();
        }
    }
}

