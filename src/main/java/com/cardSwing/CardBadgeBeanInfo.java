package com.cardSwing;

import java.beans.BeanDescriptor;
import java.beans.SimpleBeanInfo;

public class CardBadgeBeanInfo extends SimpleBeanInfo {
    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor desc = new BeanDescriptor(CardBadge.class);
        desc.setDisplayName("CardBadge");
        desc.setShortDescription("Contador circular de notificaÃ§Ãµes");
        return desc;
    }
    @Override
    public java.awt.Image getIcon(int iconKind) {
        return CardSwingIcons.getIconFor("CardBadge");
    }

    @Override
    public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
        try {
            java.beans.PropertyDescriptor pd1 = new java.beans.PropertyDescriptor("text", CardBadge.class);
            pd1.setPreferred(true);
            pd1.setValue("category", "CardBadge Configs");
            
            java.beans.PropertyDescriptor pd2 = new java.beans.PropertyDescriptor("badgeColor", CardBadge.class);
            pd2.setPreferred(true);
            pd2.setValue("category", "CardBadge Configs");
            
            java.beans.PropertyDescriptor pd3 = new java.beans.PropertyDescriptor("badgeTextColor", CardBadge.class);
            pd3.setPreferred(true);
            pd3.setValue("category", "CardBadge Configs");

            return new java.beans.PropertyDescriptor[]{pd1, pd2, pd3};
        } catch (Exception e) {
            return super.getPropertyDescriptors();
        }
    }
}
