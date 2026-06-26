package com.cardSwing;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class CardCalendarBeanInfo extends SimpleBeanInfo {
    
    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor desc = new BeanDescriptor(CardCalendar.class);
        desc.setDisplayName("CardCalendar");
        desc.setShortDescription("CalendÃ¡rio visual e interativo");
        return desc;
    }

    @Override
    public java.awt.Image getIcon(int iconKind) {
        return CardSwingIcons.getIconFor("CardCalendar");
    }

    @Override
    public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
        try {
            java.beans.PropertyDescriptor pd1 = new java.beans.PropertyDescriptor("localDate", CardCalendar.class);
            pd1.setPreferred(true);
            pd1.setValue("category", "CardCalendar Configs");
            return new java.beans.PropertyDescriptor[]{pd1};
        } catch (Exception e) {
            return super.getPropertyDescriptors();
        }
    }
}
