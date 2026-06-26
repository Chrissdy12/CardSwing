package com.cardSwing;

import java.beans.BeanDescriptor;
import java.beans.SimpleBeanInfo;

public class CardChipBeanInfo extends SimpleBeanInfo {
    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor desc = new BeanDescriptor(CardChip.class);
        desc.setDisplayName("CardChip");
        desc.setShortDescription("Filtro interativo com botÃ£o de fechar");
        return desc;
    }
    @Override
    public java.awt.Image getIcon(int iconKind) {
        return CardSwingIcons.getIconFor("CardChip");
    }

    @Override
    public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
        try {
            java.beans.PropertyDescriptor pd1 = new java.beans.PropertyDescriptor("text", CardChip.class);
            pd1.setPreferred(true);
            pd1.setValue("category", "CardChip Configs");
            
            java.beans.PropertyDescriptor pd2 = new java.beans.PropertyDescriptor("chipColor", CardChip.class);
            pd2.setPreferred(true);
            pd2.setValue("category", "CardChip Configs");
            
            java.beans.PropertyDescriptor pd3 = new java.beans.PropertyDescriptor("chipTextColor", CardChip.class);
            pd3.setPreferred(true);
            pd3.setValue("category", "CardChip Configs");

            return new java.beans.PropertyDescriptor[]{pd1, pd2, pd3};
        } catch (Exception e) {
            return super.getPropertyDescriptors();
        }
    }
}
