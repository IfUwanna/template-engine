package com.template.constant;

/**
 * packageName    : com.template.constant
 * fileName       : Syntax
 * author         : Jihun Park
 * date           : 2022/03/20
 * description    : Syntax Enum
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/20        Jihun Park       최초 생성
 */
public enum Syntax {

    PREFIX("<?"),
    SUFFIX("?>"),
    REPLACE_PREFIX("<?="),
    ITERATION_PREFIX("<? for"),
    ITERATION_SUFFIX("<? endfor ?>");

    private final String ex;

    Syntax(String ex) {
        this.ex = ex;
    }

    public String getEx() {
        return this.ex;
    }

    public int getExLength() {
        return this.ex.length();
    }

}
