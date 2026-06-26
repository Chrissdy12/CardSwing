package com.cardSwing;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class CardTabsBeanInfo extends SimpleBeanInfo {

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(CardTabs.class);
        bd.setDisplayName("CardTabs");
        return bd;
    }

    @Override
    public java.awt.Image getIcon(int iconKind) {
        return CardSwingIcons.getIconFor("CardTabs");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor themeColor = new PropertyDescriptor("themeColor", CardTabs.class);
            PropertyDescriptor unselectedTextColor = new PropertyDescriptor("unselectedTextColor", CardTabs.class);
            PropertyDescriptor trackColor = new PropertyDescriptor("trackColor", CardTabs.class);
            
            themeColor.setValue("category", "CardTabs Configs");
            unselectedTextColor.setValue("category", "CardTabs Configs");
            trackColor.setValue("category", "CardTabs Configs");

            PropertyDescriptor[] props = {themeColor, unselectedTextColor, trackColor};
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

