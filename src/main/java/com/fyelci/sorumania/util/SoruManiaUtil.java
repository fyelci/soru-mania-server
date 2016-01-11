package com.fyelci.sorumania.util;

import com.fyelci.sorumania.domain.User;
import com.fyelci.sorumania.web.rest.dto.UserDTO;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Created by fatih on 21/12/15.
 */
public class SoruManiaUtil {

    public static String getFullName(String firstName, String lastName, String login) {
        if (StringUtils.isBlank(firstName)) {
            return login;
        } else {
            return firstName + " " + lastName;
        }
    }

    public static String getFullName(User user) {
        return getFullName(user.getFirstName(), user.getLastName(), user.getLogin());
    }

    public static String getFullName(UserDTO user) {
        return getFullName(user.getFirstName(), user.getLastName(), user.getLogin());
    }

    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

}
