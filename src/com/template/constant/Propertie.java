package com.template.constant;

/**
 * packageName    : com.template.constant
 * fileName       : Propertie
 * author         : Jihun Park
 * date           : 2022/03/19
 * description    : template.properties Key
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/19        Jihun Park       최초 생성
 */
public enum Propertie {

    PATH("./template.properties"),
    PREFIX_TEMPLATE("prefix.template"),
    PREFIX_DATA("prefix.data"),
    PREFIX_RESULT("prefix.result"),
    TEMPLATE("template"),
    DATA("data"),
    RESULT("result");

    private final String _key;

    Propertie(String key) {
        this._key = key;
    }

    public String getKey() {
        return this._key;
    }
}
