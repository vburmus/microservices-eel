package com.epam.esm.jwt.service;


import com.epam.esm.credentials.model.Credentials;
import com.epam.esm.jwt.TokenType;
import com.epam.esm.utils.exceptionhandler.exceptions.CacheException;
import com.epam.esm.utils.exceptionhandler.exceptions.IncorrectTokenTypeException;
import com.epam.esm.utils.exceptionhandler.exceptions.InvalidTokenException;
import com.epam.esm.utils.exceptionhandler.exceptions.TokenRequiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static com.epam.esm.utils.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final CacheManager cacheManager;
    @Value("${jwt.secret}")
    private String secret;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public TokenType extractType(String token) {
        return TokenType.valueOf(extractClaim(token, claims -> claims.get(TYPE, String.class)));
    }

    public long extractId(String token) {
        return extractClaim(token, claims -> claims.get(ID, Long.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    public void validateToken(String bearerToken) {
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(AUTHENTICATION_BEARER_TOKEN))
            throw new TokenRequiredException(TOKEN_NEEDED);
        String jwt = bearerToken.substring(7);
        if (isTokenBanned(jwt))
            throw new InvalidTokenException(TOKEN_HAS_BEEN_BANNED);
        extractAllClaims(jwt);
    }

    public boolean isTokenBanned(String token) {
        final long id = extractId(token);
        Cache blackList = cacheManager.getCache(BLACK_LIST);
        if (blackList == null) {
            throw new CacheException(CACHE_NOT_FOUND);
        }
        var bannedUserTokensCache = blackList.get(id);
        List<String> bannedJwt = (List<String>) (bannedUserTokensCache != null ? bannedUserTokensCache.get() : null);
        if (bannedJwt == null) {
            return false;
        }
        return bannedJwt.stream().anyMatch(o -> o.equals(token));
    }

    public void invalidateTokenIfExist(long userId, TokenType tokenType) throws IncorrectTokenTypeException {
        Cache cache;
        if (tokenType == TokenType.ACCESS) {
            cache = cacheManager.getCache(ACCESS_TOKENS);
        } else if (tokenType == TokenType.REFRESH) {
            cache = cacheManager.getCache(REFRESH_TOKENS);
        } else {
            throw new IncorrectTokenTypeException();
        }
        if (cache == null) throw new CacheException(CACHE_NOT_FOUND);

        var oldTokens = cache.get(userId);

        if (oldTokens != null) {
            String cachedJwt = (String) oldTokens.get();
            addTokenToBlackList(userId, cachedJwt);
        }
    }

    private void addTokenToBlackList(long userId, String cachedJwt) {
        Cache blackList = cacheManager.getCache(BLACK_LIST);
        if (blackList == null) throw new CacheException(CACHE_NOT_FOUND);

        var bannedUserTokensCache = blackList.get(userId);
        List<String> jwtList = new ArrayList<>();
        if (bannedUserTokensCache != null) {
            var bannedTokens = bannedUserTokensCache.get();
            jwtList = bannedTokens == null ? new ArrayList<>() : (List<String>) bannedTokens;
        }
        jwtList.add(cachedJwt);
        blackList.put(userId, jwtList);
    }

    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createSignedJwt(Credentials user, TokenType tokenType) {
        return Jwts.builder()
                .claim(ID, user.getId())
                .setSubject(user.getUsername())
                .claim(ROLE, user.getRole())
                .claim(TYPE, tokenType)
                .setIssuer(EPAM)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(tokenType.getExpiryDate())
                .signWith(getSignKey()).compact();
    }
}