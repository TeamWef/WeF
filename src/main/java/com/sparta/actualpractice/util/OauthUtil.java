package com.sparta.actualpractice.util;

import com.sparta.actualpractice.dto.TokenDto;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.security.JwtFilter;
import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OauthUtil {

    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;

    public void forceLogin(Member kakaoUser) {

        UserDetails userDetails = new MemberDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
    public TokenDto generateTokenDto(Member member) {

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);

        redisTemplate.opsForValue()
                .set("RefreshToken:" + member.getEmail(), tokenDto.getRefreshToken(),
                        tokenProvider.decodeRefreshTokenExpiration(tokenDto.getRefreshToken()) - new Date().getTime(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    public HttpHeaders setHeaders(TokenDto tokenDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        headers.set("Refresh-Token", tokenDto.getRefreshToken());
        headers.set("Access-Token-Expires", tokenDto.getAccessTokenExpireIn().toString());

        return headers;
    }
}
