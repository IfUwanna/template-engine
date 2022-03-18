package com.template.domain;

import java.util.List;

/**
 * packageName    : com.template.domain
 * fileName       : User
 * author         : Jihun Park
 * date           : 2022/03/18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/18        Jihun Park       최초 생성
 */
public class User {

    private Info info;
    private Membership membership;

    public User(Info info, Membership membership) {
        this.info = info;
        this.membership = membership;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
}
