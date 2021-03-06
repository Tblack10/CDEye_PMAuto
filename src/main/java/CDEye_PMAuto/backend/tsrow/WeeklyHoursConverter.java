package CDEye_PMAuto.backend.tsrow;

import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("weeklyHours")
public class WeeklyHoursConverter implements Converter<BigDecimal> {

    @Override
    public BigDecimal getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return BigDecimal.ZERO;
        }

        //return BigDecimal.valueOf(Long.parseLong(value));
        return new BigDecimal(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, BigDecimal value) {
        // TODO Auto-generated method stub
        return value.toPlainString();
    }
}