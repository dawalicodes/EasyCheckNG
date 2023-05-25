package com.dawalisoftwares.easycheck;

public class Bank {
    String name, code;
    int logo;

    public Bank(String name, String code, int logo) {
        this.name = name;
        this.code = code;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getLogo() {
        return logo;
    }

    public static final String ACCESS_BANK = "Access Bank";
    public static final String STANBIC_IBTC_BANK = "Stanbic Bank";
    public static final String UNION_BANK = "Union Bank";
    public static final String ECO_BANK = "ECO Bank";
    public static final String FIDELITY_BANK = "Fidelity Bank";
    public static final String FIRST_BANK = "First Bank";
    public static final String FCMB_BANK = "FCMB Bank";
    public static final String GT_BANK = "GT Bank";
    public static final String HERITAGE_BANK = "Heritage Bank";
    public static final String KEYSTONE_BANK = "Keystone Bank";
    public static final String POLARIS_BANK = "Polaris Bank";
    public static final String STERLING_BANK = "Sterling Bank";
    public static final String UBA_BANK = "UBA Bank";
    public static final String UNITY_BANK = "Unity Bank";
    public static final String WEMA_BANK = "Wema Bank";
    public static final String ZENITH_BANK = "Zenith Bank";
    public static final String OPAY = "Opay";
    public static final String PALMPAY = "Palmpay";

    public static String getCode(String bank) {
        switch (bank) {
            case Bank.ACCESS_BANK:
                return "*901#";
            case ECO_BANK:
                return "*326#";
            case STANBIC_IBTC_BANK:
                return "*909#";
            case UNION_BANK:
                return "*826#";
            case FIDELITY_BANK:
                return "*770#";
            case FIRST_BANK:
                return "*894#";
            case FCMB_BANK:
                return "*329#";
            case GT_BANK:
                return "*737#";
            case HERITAGE_BANK:
                return "*745#";
            case KEYSTONE_BANK:
                return "*7111#";
            case POLARIS_BANK:
                return "*833#";
            case STERLING_BANK:
                return "*822#";
            case UBA_BANK:
                return "*919#";
            case UNITY_BANK:
                return "*7799#";
            case WEMA_BANK:
                return "*945#";
            case ZENITH_BANK:
                return "*966#";
            case OPAY:
                return "*955#";
            case PALMPAY:
                return "*652#";
            default: return "";
        }
    }
}
