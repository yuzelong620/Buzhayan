package com.kratos.game.herphone;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.kratos.engine.framework.net.socket.exception.BusinessException;
import lombok.extern.log4j.Log4j;


@Log4j
@Component
public class WebContextFilter  implements Filter {

	
	    public void init(FilterConfig filterConfig) throws ServletException {
	    
	    }

	    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {	   
	        if(servletRequest instanceof HttpServletRequest) {
		    	HttpServletRequest request=(HttpServletRequest)servletRequest;		
		        long time = System.currentTimeMillis();
		    	actionDoFilter(servletRequest, servletResponse, filterChain);
		        time =  System.currentTimeMillis()-time;
		        if (time > 200) {
		        	log.error("接口名："+request.getServletPath()+"响应时间："+time);
				}
	        }
	        else {
	        	actionDoFilter(servletRequest, servletResponse, filterChain);
	        }
	        //禁止浏览器缓存
	        if(servletResponse instanceof HttpServletResponse) {
		        HttpServletResponse res=(HttpServletResponse)servletResponse;
		    	res.setHeader("Pragma", "No-cache");
		    	res.setHeader("Cache-Control", "no-cache");
		    	res.setDateHeader("Expires", 0);
	        }
	    }

		private void actionDoFilter(ServletRequest servletRequest, ServletResponse servletResponse,
				FilterChain filterChain) {
			try{
			    filterChain.doFilter(servletRequest, servletResponse);
			}
			catch(Exception e){
				log.error("",e);
				throw new BusinessException("服务器未知错误");
			}
		}

		@Override
		public void destroy() {
			
		}
		public void jugeSession() {
			
		}

	}
