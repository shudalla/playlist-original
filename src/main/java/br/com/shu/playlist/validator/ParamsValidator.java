package br.com.shu.playlist.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import br.com.shu.playlist.exception.BadRequestException;
import br.com.shu.playlist.exception.MessageError;
import br.com.shu.playlist.exception.Messages;
import br.com.shu.playlist.utils.LatLngUtility;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParamsValidator {

    private static final String CITY_NAME_FIELD = "cityName";
    private static final String LATITUDE_FIELD = "latitude";
    private static final String LONGITUDE_FIELD = "longitude";
    private static final String LATITUDE_LONGITUDE_FIELDS = "latitude/longitude";

    public static void validate(String cityName, Double latitude, Double longitude) {
        List<MessageError> errors = new ArrayList<>();

        if (StringUtils.isBlank(cityName) && latitude == null && longitude == null) {
            errors.add(new MessageError(Messages.FIELD_COMBINATION_REQUIRED_ERROR, CITY_NAME_FIELD,
                    LATITUDE_LONGITUDE_FIELDS));

        } else {
            validateCombination(cityName, latitude, longitude, errors);
        }

        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }
    }

    private static void validateCombination(String cityName, Double latitude, Double longitude,
            List<MessageError> errors) {

        if (!StringUtils.isBlank(cityName) && cityName.split(",").length > 2) {
            errors.add(new MessageError(Messages.INVALID_FORMAT_ERROR, CITY_NAME_FIELD));
        }

        if (StringUtils.isBlank(cityName)) {

            if (latitude == null && longitude != null) {
                errors.add(new MessageError(Messages.FIELD_REQUIRED_ERROR, LATITUDE_FIELD));

            } else if (longitude == null && latitude != null) {
                errors.add(new MessageError(Messages.FIELD_REQUIRED_ERROR, LONGITUDE_FIELD));
            }

            if (latitude != null && !LatLngUtility.isLatitudeValid(String.valueOf(latitude))) {
                errors.add(new MessageError(Messages.INVALID_FORMAT_ERROR,
                        LATITUDE_FIELD));
            }

            if (longitude != null && !LatLngUtility.isLongitudeValid(String.valueOf(longitude))) {
                errors.add(new MessageError(Messages.INVALID_FORMAT_ERROR,
                        LONGITUDE_FIELD));
            }
        }
    }

}
