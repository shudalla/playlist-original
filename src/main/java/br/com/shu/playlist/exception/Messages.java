package br.com.shu.playlist.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Messages {

    public static final String DEFAULT_INTERNAL_SERVER_ERROR = "500.000";

    public static final String INVALID_BODY_ERROR = "400.000";
    public static final String FIELD_REQUIRED_ERROR = "400.001";
    public static final String INVALID_LENGTH_ERROR = "400.002";
    public static final String INVALID_FORMAT_ERROR = "400.003";
    public static final String FIELD_COMBINATION_REQUIRED_ERROR = "400.004";
    public static final String INVALID_EXACT_LENGTH_ERROR = "400.005";

}