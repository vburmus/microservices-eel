package com.epam.esm.jwt.service;

import com.epam.esm.jwt.TokenType;
import com.epam.esm.auth.models.TokenDTO;
import com.epam.esm.utils.exceptionhandler.exceptions.CacheException;
import com.epam.esm.utils.exceptionhandler.exceptions.IncorrectTokenTypeException;
import com.epam.esm.utils.openfeign.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import static com.epam.esm.utils.Constants.*;


@Component
@RequiredArgsConstructor
@Slf4j
public class TokenGenerator {
    private final CacheManager cacheManager;
    private final JwtService jwtService;

    public String createAccessToken(User user) {
        try {
            jwtService.invalidateTokenIfExist(user.getId(), TokenType.ACCESS);
        } catch (IncorrectTokenTypeException e) {
            log.error(e.getMessage());
        }
        String jwt = jwtService.createSignedJwt(user, TokenType.ACCESS);
        return putTokenInCache(ACCESS_TOKENS, user, jwt);
    }

    public String createRefreshToken(User user) {
        try {
            jwtService.invalidateTokenIfExist(user.getId(), TokenType.REFRESH);
        } catch (IncorrectTokenTypeException e) {
            log.error(e.getMessage());
        }
        String jwt = jwtService.createSignedJwt(user, TokenType.REFRESH);
        return putTokenInCache(REFRESH_TOKENS, user, jwt);
    }

    public TokenDTO createToken(User user) {
        return TokenDTO.builder()
                .userId(user.getId())
                .accessToken(createAccessToken(user))
                .refreshToken(createRefreshToken(user)).build();
    }

    private String putTokenInCache(String refreshTokens, User user, String jwt) {
        var cache = cacheManager.getCache(refreshTokens);
        if (cache == null) throw new CacheException(CACHE_NOT_FOUND);
        cache.put(user.getId(), jwt);
        return jwt;
    }
}