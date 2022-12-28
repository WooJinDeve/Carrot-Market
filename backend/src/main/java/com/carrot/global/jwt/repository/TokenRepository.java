package com.carrot.global.jwt.repository;

public interface TokenRepository {

    String save(final String email, final String refreshToken);

    void deleteAll();

    void deleteByMemberId(final String email);

    boolean exist(final String email);

    String getToken(final String email);
}
