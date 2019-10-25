package br.com.shu.playlist.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import br.com.shu.playlist.exception.MessageError;
import br.com.shu.playlist.exception.Messages;
import br.com.shu.playlist.response.PlaylistResponse;
import br.com.shu.playlist.service.PlaylistService;

public class PlaylistControllerTest extends AbstractControllerTest {

    @Mock
    private PlaylistService playlistService;

    @InjectMocks
    private PlaylistController playlistController;

    private String queryParams;

    private static final String VALID_CITY_NAME = "cityName=Campinas";
    private static final String VALID_CITY_NAME_COUNTRY = "cityName=Campinas,BR";
    private static final String VALID_LATITUDE = "latitude=-22.9064";
    private static final String VALID_LONGITUDE = "longitude=-47.0616";
    private static final String INVALID_CITY_NAME = "cityName=Campinasx";
    private static final String INVALID_CITY_NAME_COUNTRY = "cityName=Campinas,BR,X";
    private static final String INVALID_LATITUDE = "latitude=-190.9064";
    private static final String INVALID_LONGITUDE = "longitude=-190.0616";
    private static final String CITY_NAME_FIELD = "cityName";
    private static final String LATITUDE_FIELD = "latitude";
    private static final String LONGITUDE_FIELD = "longitude";
    private static final String LATITUDE_LONGITUDE_FIELDS = "latitude/longitude";

    private static final String PLAYLISTS_RESOURCE = "/playlists";

    @Override
    protected Object controllerClass() {
        return playlistController;
    }

    @Test
    public void getPlaylistSuccessfullyWithCityNameParameter() throws Exception {
        givenValidQueryParamsWithCityName();
        givenPlaylistServiceReturnValues();
        whenCallFindPlaylists();
        thenExpectOkStatus();
        thenExpectServiceCall();
    }

    @Test
    public void getPlaylistSuccessfullyWithCityNameAndCountryParameter() throws Exception {
        givenValidQueryParamsWithCityNameAndCountry();
        givenPlaylistServiceReturnValues();
        whenCallFindPlaylists();
        thenExpectOkStatus();
        thenExpectServiceCall();
    }

    @Test
    public void getPlaylistSuccessfullyWithLatitudeAndLongitudeParameter() throws Exception {
        givenValidQueryParamsWithLatitudeAndLongitude();
        givenPlaylistServiceReturnValues();
        whenCallFindPlaylists();
        thenExpectOkStatus();
        thenExpectServiceCall();
    }

    @Test
    public void getPlaylistSuccessfullyWithCityNameAndLatitudeAndLongitudeParameter() throws Exception {
        givenValidQueryParamsWithCityNameAndLatitudeAndLongitude();
        givenPlaylistServiceReturnValues();
        whenCallFindPlaylists();
        thenExpectOkStatus();
        thenExpectServiceCall();
    }

    @Test
    public void getPlaylistEmptWithCityNameInvalid() throws Exception {
        givenValidQueryParamsWithCityNameInvalid();
        givenPlaylistServiceReturnNoValues();
        whenCallFindPlaylists();
        thenExpectOkStatus();
        thenExpectServiceCall();
    }

    @Test
    public void getPlaylistFailedWithCityAndCountryInvalidFormat() throws Exception {
        givenValidQueryParamsWithCityNameAndCountryInvalidFormat();
        whenCallFindPlaylists();
        thenExpectBadRequestStatus();
        thenExpectMessageContainingInvalidFormatError(CITY_NAME_FIELD);
        thenExpectServiceNoCall();
    }

    @Test
    public void getPlaylistFailedWithoutParams() throws Exception {
        givenValidQueryParamsWithoutParams();
        whenCallFindPlaylists();
        thenExpectBadRequestStatus();
        thenExpectMessageContainingFieldCombinationRequiredError(CITY_NAME_FIELD, LATITUDE_LONGITUDE_FIELDS);
        thenExpectServiceNoCall();
    }

    @Test
    public void getPlaylistFailedWithLatitudeParam() throws Exception {
        givenValidQueryParamsWithLatitudeParam();
        whenCallFindPlaylists();
        thenExpectBadRequestStatus();
        thenExpectMessageContainingFieldRequiredError(LONGITUDE_FIELD);
        thenExpectServiceNoCall();
    }

    @Test
    public void getPlaylistFailedWithLongitudeParam() throws Exception {
        givenValidQueryParamsWithLongitudeParam();
        whenCallFindPlaylists();
        thenExpectBadRequestStatus();
        thenExpectMessageContainingFieldRequiredError(LATITUDE_FIELD);
        thenExpectServiceNoCall();
    }

    @Test
    public void getPlaylistFailedWithLatitudeInvalidAndValidLongitudeParam() throws Exception {
        givenValidQueryParamsWithLatitudeInvalid();
        whenCallFindPlaylists();
        thenExpectBadRequestStatus();
        thenExpectMessageContainingInvalidFormatError(LATITUDE_FIELD);
        thenExpectServiceNoCall();
    }

    @Test
    public void getPlaylistFailedWithLatitudeValidAndInvalidLongitudeParam() throws Exception {
        givenValidQueryParamsWithLongitudeInvalid();
        whenCallFindPlaylists();
        thenExpectBadRequestStatus();
        thenExpectMessageContainingInvalidFormatError(LONGITUDE_FIELD);
        thenExpectServiceNoCall();
    }

    @Test
    public void getPlaylistFailedWithLatitudeInvalidWithoutLongitudeParam() throws Exception {
        givenValidQueryParamsWithInvalidLatitudeWithoutLongitude();
        whenCallFindPlaylists();
        thenExpectBadRequestStatus();
        thenExpectMessageContainingFieldRequiredError();
        thenExpectServiceNoCall();
    }

    @Test
    public void getPlaylistFailedWithInvalidLongitudeWithoutLatitudeParam() throws Exception {
        givenValidQueryParamsWithInvalidLongitudeWithoutLatitude();
        whenCallFindPlaylists();
        thenExpectBadRequestStatus();
        thenExpectMessageContainingFieldRequiredError();
        thenExpectServiceNoCall();
    }

    /*
     * given methods
     */

    private void givenValidQueryParamsWithCityName() {
        queryParams = new StringBuilder().append("?" + VALID_CITY_NAME).toString();
    }

    private void givenValidQueryParamsWithCityNameAndCountry() {
        queryParams = new StringBuilder().append("?" + VALID_CITY_NAME_COUNTRY).toString();
    }

    private void givenValidQueryParamsWithLatitudeAndLongitude() {
        queryParams = new StringBuilder()
                .append("?" + VALID_LATITUDE)
                .append("&" + VALID_LONGITUDE)
                .toString();
    }

    private void givenValidQueryParamsWithCityNameAndLatitudeAndLongitude() {
        queryParams = new StringBuilder()
                .append("?" + VALID_CITY_NAME)
                .append("&" + VALID_LATITUDE)
                .append("&" + VALID_LONGITUDE)
                .toString();
    }

    private void givenValidQueryParamsWithCityNameInvalid() {
        queryParams = new StringBuilder()
                .append("?" + INVALID_CITY_NAME)
                .toString();
    }

    private void givenValidQueryParamsWithCityNameAndCountryInvalidFormat() {
        queryParams = new StringBuilder()
                .append("?" + INVALID_CITY_NAME_COUNTRY)
                .toString();
    }

    private void givenValidQueryParamsWithoutParams() {
        queryParams = "";
    }

    private void givenValidQueryParamsWithLatitudeParam() {
        queryParams = new StringBuilder()
                .append("?" + VALID_LATITUDE)
                .toString();
    }

    private void givenValidQueryParamsWithLongitudeParam() {
        queryParams = new StringBuilder()
                .append("?" + VALID_LONGITUDE)
                .toString();
    }

    private void givenValidQueryParamsWithLatitudeInvalid() {
        queryParams = new StringBuilder()
                .append("?" + INVALID_LATITUDE)
                .append("&" + VALID_LONGITUDE)
                .toString();
    }

    private void givenValidQueryParamsWithLongitudeInvalid() {
        queryParams = new StringBuilder()
                .append("?" + INVALID_LONGITUDE)
                .append("&" + VALID_LATITUDE)
                .toString();
    }

    private void givenValidQueryParamsWithInvalidLongitudeWithoutLatitude() {
        queryParams = new StringBuilder()
                .append("?" + INVALID_LONGITUDE)
                .toString();
    }

    private void givenValidQueryParamsWithInvalidLatitudeWithoutLongitude() {
        queryParams = new StringBuilder()
                .append("?" + INVALID_LATITUDE)
                .toString();
    }

    private void givenPlaylistServiceReturnValues() {
        doReturn(new PlaylistResponse()).when(playlistService).findPlaylists(anyString(),
                any(Double.class),
                any(Double.class));
    }

    private void givenPlaylistServiceReturnNoValues() {
        doReturn(null).when(playlistService).findPlaylists(anyString(),
                any(Double.class),
                any(Double.class));
    }

    /*
     * when methods
     */

    private void whenCallFindPlaylists() throws Exception {
        response = mockMvc.perform(get(PLAYLISTS_RESOURCE + queryParams))
                .andReturn()
                .getResponse();
    }

    /*
     * then methods
     */

    private void thenExpectOkStatus() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private void thenExpectBadRequestStatus() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void thenExpectServiceCall() {
        verify(playlistService, times(1)).findPlaylists(any(), any(), any());
    }

    private void thenExpectServiceNoCall() {
        verify(playlistService, times(0)).findPlaylists(any(), any(), any());
    }

    // private void then

    private void thenExpectMessageContainingFieldCombinationRequiredError(String field1,
            String field2)
            throws UnsupportedEncodingException {
        String json = response.getContentAsString();
        List<MessageError> errors = stringJsonToList(json, MessageError.class);
        assertThat(errors.size()).isEqualTo(1);
        MessageError expectedMessageError = new MessageError(Messages.FIELD_COMBINATION_REQUIRED_ERROR, field1, field2);
        assertThat(errors.get(0)).isEqualTo(expectedMessageError);
    }

    private void thenExpectMessageContainingInvalidFormatError(String field)
            throws UnsupportedEncodingException {
        String json = response.getContentAsString();
        List<MessageError> errors = stringJsonToList(json, MessageError.class);
        assertThat(errors.size()).isEqualTo(1);
        MessageError expectedMessageError = new MessageError(Messages.INVALID_FORMAT_ERROR, field);
        assertThat(errors.get(0)).isEqualTo(expectedMessageError);
    }

    private void thenExpectMessageContainingFieldRequiredError(String field1)
            throws UnsupportedEncodingException {
        String json = response.getContentAsString();
        List<MessageError> errors = stringJsonToList(json, MessageError.class);
        assertThat(errors.size()).isEqualTo(1);
        MessageError expectedMessageError = new MessageError(Messages.FIELD_REQUIRED_ERROR, field1);
        assertThat(errors.get(0)).isEqualTo(expectedMessageError);
    }

    private void thenExpectMessageContainingFieldRequiredError()
            throws UnsupportedEncodingException {
        String json = response.getContentAsString();
        List<MessageError> errors = stringJsonToList(json, MessageError.class);
        assertThat(errors.size()).isLessThanOrEqualTo(2);
    }

}
