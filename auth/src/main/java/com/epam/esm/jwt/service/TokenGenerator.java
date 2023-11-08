package com.epam.esm.jwt.service;

import com.epam.esm.auth.models.TokenDTO;
import com.epam.esm.jwt.TokenType;
import com.epam.esm.model.UserDTO;
import com.epam.esm.utils.exceptionhandler.exceptions.CacheException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import static com.epam.esm.utils.Constants.*;


@Component
@RequiredArgsConstructor
public class TokenGenerator {
    private final CacheManager cacheManager;
    private final JwtService jwtService;

    public String createAccessToken(UserDTO user) {
        jwtService.invalidateTokenIfExist(user.getId(), TokenType.ACCESS);
        String jwt = jwtService.createSignedJwt(user, TokenType.ACCESS);
        return putTokenInCache(ACCESS_TOKENS, user, jwt);
    }

    public String createRefreshToken(UserDTO user) {
        jwtService.invalidateTokenIfExist(user.getId(), TokenType.REFRESH);
        String jwt = jwtService.createSignedJwt(user, TokenType.REFRESH);
        return putTokenInCache(REFRESH_TOKENS, user, jwt);
    }

    public TokenDTO createToken(UserDTO user) {
        return TokenDTO.builder()
                .userId(user.getId())
                .accessToken(createAccessToken(user))
                .refreshToken(createRefreshToken(user)).build();
    }

    private String putTokenInCache(String refreshTokens, UserDTO user, String jwt) {
        var cache = cacheManager.getCache(refreshTokens);
        if (cache == null) throw new CacheException(CACHE_NOT_FOUND);
        cache.put(user.getId(), jwt);
        return jwt;
    }

    public String createValidationToken(UserDTO user) {
        return jwtService.createSignedJwt(user, TokenType.EMAIL_VALIDATION);
    }
}