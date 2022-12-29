package com.carrot.global.jwt.repository;

public interface TokenRepository {

    String save(final String id, final String refreshToken);

    void deleteAll();

    void deleteByMemberId(final String id);

    boolean exist(final String id);

    String getToken(final String id);
}
