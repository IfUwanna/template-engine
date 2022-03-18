package com.template;

/**
 * packageName    : com.template
 * fileName       : ENUM
 * author         : Jihun Park
 * date           : 2022/03/19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/19        Jihun Park       최초 생성
 */
public enum PropertieType {

    PROPERTIE_PREFIX_TEMPLATE("prefix.template"),
    PROPERTIE_PREFIX_DATA("prefix.data"),
    PROPERTIE_TEMPLATE("template"),
    PROPERTIE_DATA("data");

    private final String _key;

    PropertieType(String key) {
        this._key = key;
    }

    public String getKey() {
        return this._key;
    }
}
