package br.com.shu.enumeration;

import java.util.List;

public enum CitySpotifyEnum {
    AD, AE, AR, AT, AU, BE, BG, BH, BO, BR, CA, CH, CL, CO, CR, CY, CZ, DE, DK, DO, DZ, EC, EE, EG, ES, FI, FR, GB, GR, GT, HK, HN, HU, ID, IE, IL, IN, IS, IT, JO, JP, KW, LB, LI, LT, LU, LV, MA, MC, MT, MX, MY, NI, NL, NZ, OM, PA, PE, PH, PL, PS, PT, PY, QA, RO, SA, SE, SG, SJ, SK, SV, TH, TN, TR, TW, US, UY, VN, ZA;

    public static boolean isSpotifyAvailable(String country) {
        return List.of(CategoryPlaylisSpotifyEnum.values())
                .stream()
                .anyMatch(c -> c.name().equals(country));
    }

}
