package com.cardSwing;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;
import java.util.List;

public class CardGraficBeanInfo extends SimpleBeanInfo {

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(CardGrafic.class);
        bd.setDisplayName("CardGrafic");
        return bd;
    }
    
    @Override
    public java.awt.Image getIcon(int iconKind) {
        return CardMuryIcons.getIconFor("CardGrafic"); // Fallback to a default if missing
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            List<PropertyDescriptor> props = new ArrayList<>();
            String[] customProps = {
                "chartType", "values", "labels", "colors", "chartColor", "gridColor", "textColor", 
                "showGrid", "showLabels", "title", "titlePosition", "titleColor",
                "font", "foreground", "background",
                "themeColor", "shadowEnabled", "hoverEnabled"
            };

            for (String propName : customProps) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(propName, CardGrafic.class);
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
