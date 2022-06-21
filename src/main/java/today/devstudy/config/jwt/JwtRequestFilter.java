package today.devstudy.config.jwt;

import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import today.devstudy.domain.User;
import today.devstudy.repository.UserRepository;
import today.devstudy.service.JwtUserDetailService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String userid = null;
        String jwtToken = null;
        // JWT 토큰은 "Beare token"에 있다. Bearer단어를 제거하고 토큰만 받는다.
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                userid = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException ex) {
                logger.error("Unable to get JWT token", ex);
            } catch (Exception ex) {
                logger.error("token valid error:" + ex.getMessage(), ex);
                throw new RuntimeException("11 Username from token error");
            }
        } else {
            logger.warn("JWT token does not begin with Bearer String");
        }
// 토큰을 가져오면 검증을 한다.
        if (userid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = this.userRepository.findByUserId(userid).orElseThrow(() -> new UsernameNotFoundException("사용자 이름찾지못함"));

            // 토큰이 유효한 경우 수동으로 인증을 설정하도록 스프링 시큐리티를 구성한다.
            if (jwtTokenUtil.validateToken(jwtToken)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                null
                        );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 컨텍스트에 인증을 설정 한 후 현재 사용자가 인증되도록 지정한다.
                // 그래서 Spring Security 설정이 성공적으로 넘어간다.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
