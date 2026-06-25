package com.cardSwing;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;
import java.util.List;

public class CardTextFieldBeanInfo extends SimpleBeanInfo {

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(CardTextField.class);
        bd.setDisplayName("CardTextField");
        return bd;
    }
    
    @Override
    public java.awt.Image getIcon(int iconKind) {
        return CardMuryIcons.getIconFor("CardTextField");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            List<PropertyDescriptor> props = new ArrayList<>();
            String[] customProps = {
                "text", "font", "foreground", "background", "radius", "borderColor", "focusColor"
            };

            for (String propName : customProps) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(propName, CardTextField.class);
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
