package com.cardSwing;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class CardTableBeanInfo extends SimpleBeanInfo {

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(CardTable.class);
        bd.setDisplayName("CardTable");
        return bd;
    }

    @Override
    public java.awt.Image getIcon(int iconKind) {
        return CardMuryIcons.getIconFor("CardTable");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor headerColor = new PropertyDescriptor("headerColor", CardTable.class);
            PropertyDescriptor headerTextColor = new PropertyDescriptor("headerTextColor", CardTable.class);
            PropertyDescriptor rowHoverColor = new PropertyDescriptor("rowHoverColor", CardTable.class);
            
            headerColor.setValue("category", "CardMury Configs");
            headerTextColor.setValue("category", "CardMury Configs");
            rowHoverColor.setValue("category", "CardMury Configs");

            PropertyDescriptor[] props = {headerColor, headerTextColor, rowHoverColor};
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

