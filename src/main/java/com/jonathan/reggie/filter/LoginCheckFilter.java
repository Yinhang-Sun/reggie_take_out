package com.jonathan.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.jonathan.reggie.common.BaseContext;
import com.jonathan.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Check if the user has completed the login
 */

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    // Path matcher, supports wildcards
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1. Get the URI of this request
        String requestURI = request.getRequestURI();// /backend/index.html
        log.info("Intercepted request: {}", requestURI);

        //Define request paths that do not need to be processed
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };


        //2. Determine whether this request needs to be processed
        boolean check = check(urls, requestURI);

        //3. If no treatment is required, release it directly.
        if (check) {
            log.info("This request {} does not need to be processed", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //4. Determine the login status. If logged in, directly release
        if (request.getSession().getAttribute("employee") != null) {
            log.info("The user is logged in and the user ID is: {}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            long id = Thread.currentThread().getId();
            log.info("Thread id is: {}", id);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("User is not logged in");
        //5. If you are not logged in, return the result of not logged in, and
        // respond to the client page with data through the output stream.
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * Path matching, check whether this request needs to be released
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
