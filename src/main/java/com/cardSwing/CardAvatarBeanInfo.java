package com.cardSwing;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;
import java.util.List;

public class CardAvatarBeanInfo extends SimpleBeanInfo {

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(CardAvatar.class);
        bd.setDisplayName("CardAvatar");
        bd.setValue("isContainer", Boolean.FALSE);
        return bd;
    }
    
    @Override
    public java.awt.Image getIcon(int iconKind) {
        return CardSwingIcons.getIconFor("CardAvatar");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            List<PropertyDescriptor> props = new ArrayList<>();
                        String[] customProps = {
                "text", "title", "titleIcon", "font", "foreground", "background", "icon", "selected", 
                "progress", "currentValue", "totalValue", "showPercentage",
                "barColor", "trackColor", "showLabel", "barHeight", 
                "lineColor", "thickness", "verticalMargin", "verticalPadding", 
                "label", "value", "valueColor", "showDot", 
                "tagColor", "tagTextColor", "tagRadius", "cornerRadius", "buttonRadius",
                "buttonColor", "hoverColor", "pressedColor", "textColor", "imagePath", 
                "imageHeight", "imageRadius", "placeholderColor",
                "horizontalGap", "verticalGap", "gap", "themeColor", "hoverBorderColor", 
                "shadowEnabled", "hoverEnabled", "cardWidth", "columns", "scrollSpeed",
                "checkColor", "titleColor", "subtitleColor", "fontSize",
                "collapsible", "clickable", "showSearch", "initials", "avatarColor", 
                "showOnlineDot", "avatarSize", "trackOnColor", "trackOffColor", "knobColor", "lineWrap",
                "borderColor", "chartColor", "chartType", "colors", "focusColor", "gridColor",
                "headerColor", "headerTextColor", "items", "labels", "localDate", "localDateTime",
                "onButtonClick", "onCardClick", "onScrollEnd", "onSearch", "placeholder", "radius",
                "rowHoverColor", "showGrid", "titlePosition", "unselectedTextColor", "values"
            };

            for (String propName : customProps) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(propName, CardAvatar.class);
                    pd.setPreferred(true);
                    pd.setValue("category", "CardAvatar Configs");
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
